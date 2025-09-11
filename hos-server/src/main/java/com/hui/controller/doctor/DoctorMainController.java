package com.hui.controller.doctor;

import com.hui.constant.JwtClaimsConstant;
import com.hui.context.BaseContext;
import com.hui.dto.*;
import com.hui.entity.DetailInfo;
import com.hui.entity.DocLoginResult;
import com.hui.mapper.DocMainMapper;
import com.hui.properties.JwtProperties;
import com.hui.result.PageResult;
import com.hui.result.Result;
import com.hui.service.DocMainService;
import com.hui.service.ManagerService;
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
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/doc/main")
@Slf4j
public class DoctorMainController {

    @Autowired
    private DocMainService docMainService;

    @Autowired
    private DocMainMapper docMainMapper;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private ManagerService managerService;

    //医生登录账号
    @GetMapping("/login")
    public Result<DocLoginResult> login(DocLoginDTO docLoginDTO){
        log.info("医生登录:{}",docLoginDTO);
        DocLoginResult docLoginResult=docMainService.login(docLoginDTO);

        //登录成功后，生成jwt令牌,包含医生id
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.DOC_ID, docLoginResult.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getDocSecretKey(),
                jwtProperties.getDocTtl(),
                claims);

        docLoginResult.setToken(token);
        return Result.success(docLoginResult);
    }

    //医生帮助别人注册
    @PostMapping("/register")
    public Result<DocRegisterVO> register(@RequestBody DocRegisterDTO docRegisterDTO){
        //向表中加入简要信息后,将id也插入,以便后续操作
        DocRegisterVO docRegisterVO=docMainService.register(docRegisterDTO);

        //两次主键返回
        InsertIdDTO insertIdDTO = InsertIdDTO.builder()
                .doctorId(docRegisterVO.getDoctorId())
                .id(docRegisterVO.getId()).build();
        docMainMapper.insertDocId(insertIdDTO);

        BaseContext.setCurrentId(Long.valueOf(docRegisterVO.getDoctorId()));
        return Result.success(docRegisterVO);
    }

    //医生忘记密码,更新密码
    @PutMapping("/update")
    public Result updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO){
        log.info("更新密码:{}",updatePasswordDTO);
         docMainService.updatePassword(updatePasswordDTO);
        return Result.success("更新密码成功");
    }

    //医生设置个人详细信息
    @PostMapping("/detail")
    public Result<SetInfoVO> setDetailInfo(@RequestBody DetailInfo detailInfo){
        log.info("设置信息:{}",detailInfo);
        detailInfo.setDoctorId(Math.toIntExact(BaseContext.getCurrentId()));
        SetInfoVO setInfoVO=docMainService.setDetailInfo(detailInfo);

        return Result.success(setInfoVO);
    }

    //医生查看个人详细信息
    @GetMapping("/detail")
    public Result<DetailInfo> getDetailInfo(){
        Long doctorId = BaseContext.getCurrentId();
        DetailInfo detailInfo = docMainMapper.getDetailInfo(doctorId);
        return Result.success(detailInfo);
    }

    //医生设置排班信息
    @PostMapping("/scheduling")
    public Result<SetBanVO> setBanInfo(@RequestBody BanInfoDTO banInfoDTO){
        banInfoDTO.setDoctorId(Math.toIntExact(BaseContext.getCurrentId()));
        SetBanVO setBanDTO=docMainService.setBanInfo(banInfoDTO);
        return Result.success(setBanDTO);
    }

    //医生分页查看排班信息
    @GetMapping("/scheduling")
    public Result<PageResult> getBanInfo(@RequestBody TimeInfoDTO timeInfoDTO){
        log.info("查询排班信息:{}",timeInfoDTO);
        timeInfoDTO.setDoctorId(Math.toIntExact(BaseContext.getCurrentId()));
        PageResult pageResult=docMainService.getBanInfo(timeInfoDTO);
        return Result.success(pageResult);
    }

    //医生叫号
    @PostMapping("/callNumber")
    public Result<CallNumberVO> callNumber(CallNumberDTO callNumberDTO){
        CallNumberVO callNumberVO=docMainService.callNumber(callNumberDTO);
        return Result.success(callNumberVO);
    }

    //患者就诊
    @PostMapping("/callNumber/case")
    public Result<CasesVO> patientArrived(CasesDTO casesDTO){
        CasesVO casesVO=docMainService.patientArrived(casesDTO);
        return Result.success(casesVO);
    }

    //导出排班信息excel
    @GetMapping("/timeinfo/export")//这两个数据可以不用传,也可以查
    public void exportAllTime(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate time,
                              HttpServletResponse response) throws IOException {
        String name=docMainMapper.getNameById(Math.toIntExact(BaseContext.getCurrentId()));
        createExcel(time, response, name);
    }

    //创建excel
    public void createExcel(LocalDate time, HttpServletResponse response, String name) throws IOException {
        log.info("导出排班信息:name={}, time={}", name, time);

        // 构造查询条件
        AllTimeDTO allTimeDTO = AllTimeDTO.builder()
                .name(name)
                .time(time.atStartOfDay())
                .build();

        // 获取数据
        List<AllTimeVO> timeList = managerService.getAllTimeList(allTimeDTO);

        // 设置响应头
        // 设置响应头 - 修复文件名编码问题
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        // 中文文件名需要进行URL编码
        String filename = "排班信息.xlsx";
        String encodedFilename = URLEncoder.encode(filename, "UTF-8").replace("+", "%20");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFilename);


        // 创建Excel工作簿
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("排班信息");

        // 创建表头
        Row headerRow = sheet.createRow(0);
        String[] headers = {"医生姓名", "开始时间", "结束时间", "预计时间", "剩余号数"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // 填充数据
        for (int i = 0; i < timeList.size(); i++) {
            Row row = sheet.createRow(i + 1);
            AllTimeVO vo = timeList.get(i);

            row.createCell(0).setCellValue(vo.getName());
            row.createCell(1).setCellValue(vo.getBeginTime().toString());
            row.createCell(2).setCellValue(vo.getEndTime().toString());
            row.createCell(3).setCellValue(String.valueOf(vo.getPredictTime()));
            row.createCell(4).setCellValue(vo.getRemain());
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
