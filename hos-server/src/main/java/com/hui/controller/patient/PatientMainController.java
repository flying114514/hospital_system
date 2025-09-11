package com.hui.controller.patient;

import com.hui.constant.JwtClaimsConstant;
import com.hui.constant.RegisteredStatusConstant;
import com.hui.context.BaseContext;
import com.hui.dto.*;
import com.hui.entity.PayHistory;
import com.hui.entity.ResultDetail;
import com.hui.mapper.DocMainMapper;
import com.hui.mapper.PatientMainMapper;
import com.hui.mapper.RegisterMapper;
import com.hui.properties.JwtProperties;
import com.hui.result.PageResult;
import com.hui.result.Result;
import com.hui.service.ManagerService;
import com.hui.service.PatientMainService;
import com.hui.service.RegisterService;
import com.hui.utils.JwtUtil;
import com.hui.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 患者主页面
 * */
@RequestMapping("/user/main")
@RestController
@Slf4j
public class PatientMainController {

    @Autowired
    private PatientMainService patientMainService;

    @Autowired
    private JwtProperties jwtProperties;


    @Autowired
    private RegisterMapper registerMapper;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private PatientMainMapper patientMainMapper;

    @Autowired
    private DocMainMapper docMainMapper;

    @Autowired
    private ManagerService managerService;

    //患者登录
    @PostMapping("/login")
    public Result<PatientLoginVO> login(@RequestBody LoginDTO loginDTO){
        log.info("患者登录:{}",loginDTO);
        String name = loginDTO.getName();
        LoginVO loginVO =patientMainService.checkPassword(loginDTO);
            //登录成功后，生成jwt令牌,包含患者id
            Map<String, Object> claims = new HashMap<>();
            claims.put(JwtClaimsConstant.PAT_ID, loginVO.getId());
            String token = JwtUtil.createJWT(
                    jwtProperties.getPatSecretKey(),
                    jwtProperties.getPatTtl(),
                    claims);

        PatientLoginVO patientLoginVO = PatientLoginVO.builder()
                    .id(Long.valueOf(loginVO.getId()))
                    .name(name)
                    .token(token)
                    .build();
            return Result.success(patientLoginVO);

    }

    //查询用户是否有医保卡,如果没有,要添加医保卡账户,如果有,展示医保卡信息
    @PostMapping("/login/checkcard")
    public Result<MedicalCardVO> checkCard(MedicalCardDTO medicalCardDTO){
        Long patientId = BaseContext.getCurrentId();
        medicalCardDTO.setId(Math.toIntExact(patientId));
        MedicalCardVO medicalCardVO=patientMainService.checkCard(medicalCardDTO);
        return Result.success(medicalCardVO);
    }

    //患者充值  xxx你已成功向医保卡充值xxx元/充值失败,微信/现金余额不足
    @PostMapping("/recharge")
    public Result<ResultDetail> recharge(@RequestBody RechargeDTO rechargeDTO){
        //先查询用户余额是否足够,已经在用户建档时创建了银行账户
        ResultDetail resultDetail=patientMainService.checkMoney(rechargeDTO);
        return Result.success(resultDetail);
    }

    //患者查看历史缴费记录
    @GetMapping("/history")
    public Result<PageResult> selectPayHistory(@RequestBody PayHistoryPageDTO payHistoryPageDTO){
        Long patientId = BaseContext.getCurrentId();
        payHistoryPageDTO.setPatientId(Math.toIntExact(patientId));
        PageResult pageResult=patientMainService.selectPayHistory(payHistoryPageDTO);
        return Result.success(pageResult);
    }

    //患者导出历史缴费记录excel
    @GetMapping("/history/export")
    public void exportAllPay(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate time,
                              HttpServletResponse response) throws IOException {
        String name=patientMainMapper.getNameById(BaseContext.getCurrentId());
        createPayExcel(time, response, name);
    }

    //患者查看历史挂号记录
    @GetMapping("/guahistory")
    public Result<PageResult> selectGuaHistory(@RequestBody GuaHistoryPageDTO guaHistoryPageDTO){
        Long patientId = BaseContext.getCurrentId();
        guaHistoryPageDTO.setPatientId(Math.toIntExact(patientId));
        PageResult pageResult=patientMainService.selectGuaHistory(guaHistoryPageDTO);
        return Result.success(pageResult);
    }

    //导出历史挂号信息excel
    @GetMapping("/guahistory/export")//这两个数据可以不用传,也可以查
    public void exportAllTime(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate time,
                              HttpServletResponse response) throws IOException {
        String name=patientMainMapper.getNameById(BaseContext.getCurrentId());
        createPayExcel(time, response, name);
    }

    //患者取消挂号
    @PostMapping("/cancel")
    public Result<CancelOrderVO> cancelOrder(CancelOrderDTO cancelOrderDTO){

        Long patientId = BaseContext.getCurrentId();
        if (patientId==null){
            return Result.error("请先完成上述任务");
        }
        cancelOrderDTO.setPatientId(Math.toIntExact(patientId));

        //获取前端传递来的number
        Integer number = cancelOrderDTO.getNumber();

        if(number==null){
            return Result.error("挂号单不存在,请重新选择挂号单");
        }

        //为了防止重复选号带来的问题
        CancelIngDTO cancelingDTO = CancelIngDTO.builder()
                .status(RegisteredStatusConstant.CANCELING)
                .number(number)
                .patientId(Math.toIntExact(patientId)).build();

        //将order状态设置为取消中
        patientMainMapper.setStatus(cancelingDTO);

        //检查时间
        CancelOrderVO cancelOrderVO=checkTime(cancelingDTO);
        if(cancelOrderVO!=null){
            //将状态改回来
            cancelingDTO.setStatus(RegisteredStatusConstant.WAIT_FOR_CALL);
            patientMainMapper.resrtStstus(cancelingDTO);
            return Result.success(cancelOrderVO);
        }


        //删除前先把支付方式获取出来
        String paymentMethod=patientMainMapper.getPaymentMethod(cancelingDTO);
        if(paymentMethod==null){
            return Result.error("该挂号单未支付,十五分钟后自动取消");
        }

        //从orders里获取money
        Double money = registerMapper.getMoney(cancelingDTO);

        if(paymentMethod.equals("现金")){
            paymentMethod="cash";
        }
        ReturnMoneyDTO returnMoneyDTO = ReturnMoneyDTO.builder()
                .paymentMethod(paymentMethod)
                .money(money)
                .patientId(Math.toIntExact(patientId)).build();
        //患者取消挂号单后,将钱原路返回
        registerService.returnMoney(returnMoneyDTO);

        //将医生的号也还回去
        ReturnDocNumDTO returnDocNumDTO = ReturnDocNumDTO.builder()
                .number(number).build();
        registerMapper.returnDocNum(returnDocNumDTO);

        //删除数据
        cancelOrderVO=registerService.cancelOrder(cancelingDTO);

        return Result.success(cancelOrderVO);

    }

    //为医院评分,患者必须在至少经过一次就诊后才可以评分,患者每天只能评一次,防止水军(用定时任务类)
    @PutMapping("/star")
    public Result<String> setStar(StarDTO starDTO){
        log.info("患者评分: star={}", starDTO);

        Long patientId = BaseContext.getCurrentId();
        starDTO.setPatientId(Math.toIntExact(patientId));

        starDTO.setName(patientMainMapper.getNameById(patientId));

        String result=patientMainService.setStar(starDTO);
        return Result.success(result);
    }



    public CancelOrderVO checkTime(CancelIngDTO cancelingDTO){
        //判断当前时间距离预计就诊时间是否还有15分钟,只查询取消中的挂号单

        CancelOrderVO cancelOrderVO = new CancelOrderVO();
        //前端已经为我们传递的只可能是一个数据,返回的不可能是list
        LocalDateTime time=registerMapper.getPreTime(cancelingDTO);

        if(time==null){
            cancelOrderVO.setDetail("未找到挂号单");
            return cancelOrderVO;
        }

        //在15分钟内,不可取消
        if (time.isBefore(LocalDateTime.now().plusMinutes(15)) && time.isAfter(LocalDateTime.now())){
            cancelOrderVO.setDetail("距离预计就诊时间不足15分钟,取消挂号失败");
            return cancelOrderVO;
        }
        return null;

    }
    //创建excel
    public void createExcel(LocalDate time, HttpServletResponse response, String name ) throws IOException {
        log.info("导出挂号信息:name={}, time={}", name, time);

        // 构造查询条件
        AllTimeDTO allTimeDTO = AllTimeDTO.builder()
                .name(name)
                .time(time.atStartOfDay())
                .patientId(Math.toIntExact(BaseContext.getCurrentId()))
                .build();

        // 获取数据
        List<GuaHistoryVO> timeList = patientMainService.getAllTimeList(allTimeDTO);


        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String filename = "挂号信息.xlsx";
        String encodedFilename = URLEncoder.encode(filename, "UTF-8").replace("+", "%20");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFilename);


        // 创建Excel工作簿
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("挂号信息");

        // 创建表头
        Row headerRow = sheet.createRow(0);
        String[] headers = {"医生姓名", "科室", "科室位置", "创建时间", "价格", "支付方式", "级别", "号数", "医嘱"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // 填充数据
        for (int i = 0; i < timeList.size(); i++) {
            Row row = sheet.createRow(i + 1);
            GuaHistoryVO vo = timeList.get(i);

            row.createCell(0).setCellValue(vo.getDoctorName());
            row.createCell(1).setCellValue(vo.getDepartment());
            row.createCell(2).setCellValue(vo.getDepartmentLocation());
            row.createCell(3).setCellValue(vo.getCreateTime());
            row.createCell(4).setCellValue(vo.getPrice());
            row.createCell(5).setCellValue(vo.getPaymentMethod());
            row.createCell(6).setCellValue(vo.getLevel());
            row.createCell(7).setCellValue(vo.getNumber());
            row.createCell(8).setCellValue(vo.getCases());
        }

        // 调整列宽
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // 写入响应
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    //创建excel
    public void createPayExcel(LocalDate time, HttpServletResponse response, String name) throws IOException {
        log.info("导出充值信息:name={}, time={}", name, time);

        // 构造查询条件
        AllTimeDTO allTimeDTO = AllTimeDTO.builder()
                .name(name)
                .time(time.atStartOfDay())
                .patientId(Math.toIntExact(BaseContext.getCurrentId()))
                .build();

        // 获取数据
        List<PayHistory> timeList = patientMainService.getAllPayList(allTimeDTO);


        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String filename = "充值信息.xlsx";
        String encodedFilename = URLEncoder.encode(filename, "UTF-8").replace("+", "%20");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFilename);


        // 创建Excel工作簿
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("充值信息");

        // 创建表头
        Row headerRow = sheet.createRow(0);
        String[] headers = {"充值金额", "充值时间", "充值方式"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // 填充数据
        for (int i = 0; i < timeList.size(); i++) {
            Row row = sheet.createRow(i + 1);
            PayHistory vo = timeList.get(i);

            row.createCell(0).setCellValue(vo.getMoney());
            row.createCell(1).setCellValue(vo.getTime());
            row.createCell(2).setCellValue(vo.getPaymentMethod());
        }

        // 调整列宽
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // 写入响应
        workbook.write(response.getOutputStream());
        workbook.close();
    }


}
