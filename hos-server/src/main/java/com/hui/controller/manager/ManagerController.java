package com.hui.controller.manager;

import com.hui.constant.JwtClaimsConstant;
import com.hui.dto.AllTimeDTO;
import com.hui.dto.ManagerLoginDTO;
import com.hui.entity.DoctorVisitStats;
import com.hui.properties.JwtProperties;
import com.hui.result.PageResult;
import com.hui.result.Result;
import com.hui.service.ManagerService;
import com.hui.utils.JwtUtil;
import com.hui.vo.AllTimeVO;
import com.hui.vo.ManagerLoginVO;
import com.hui.vo.PatientLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.net.URLEncoder;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/manager")
@Slf4j
public class ManagerController {

    @Autowired
    public ManagerService managerService;

    @Autowired
    public JwtProperties jwtProperties;

    //管理员登录
    @PostMapping("/login")
    public Result<PatientLoginVO> login(@RequestBody ManagerLoginDTO managerLoginDTO){
        log.info("管理员登录:{}",managerLoginDTO);
        String name = managerLoginDTO.getName();
        ManagerLoginVO managerLoginVO=managerService.login(managerLoginDTO);
        //登录成功后，生成jwt令牌,包含患者id
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.MAN_ID, managerLoginVO.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getManSecretKey(),
                jwtProperties.getManTtl(),
                claims);

        PatientLoginVO patientLoginVO = PatientLoginVO.builder()
                .id(Long.valueOf(managerLoginVO.getId()))
                .name(name)
                .token(token)
                .build();
        return Result.success(patientLoginVO);
    }

    //根据时间,姓名模糊分页得到所有排班信息
    @GetMapping("/timeinfo")
    public Result<PageResult> getAllTime(@RequestBody AllTimeDTO allTimeDTO){
        PageResult pageResult=managerService.getAllTime(allTimeDTO);
        return Result.success(pageResult);
    }


    //获取每天top5医生,可以传递给前端做图
    @GetMapping("/statistics/topDoctors")
    public Result<List<DoctorVisitStats>> getTopDoctorsStatistics() {
        List<DoctorVisitStats> topDoctors = managerService.getTopDoctorsByVisits(5);
        return Result.success(topDoctors);
    }

    //导出排班信息excel
    @GetMapping("/timeinfo/export")//这两个数据可以不用传,也可以查
    public void exportAllTime(@RequestParam(required = false) String name,
                              @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate time,
                              HttpServletResponse response) throws IOException {
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
