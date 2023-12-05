package com.founder.easy_route_assistant.Controller;

import com.founder.easy_route_assistant.Entity.ExcelEntity;
import com.founder.easy_route_assistant.Repository.ExcelRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ExcelController {
    private final ExcelRepository excelRepository;
    @GetMapping("/excel")
    public String main() {
        return "excel";
    }

    @PostMapping("/excel/read")
    public String readExcel(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet worksheet = workbook.getSheetAt(0);

        for(int i=1; i<worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);
            ExcelEntity excelEntity = ExcelEntity.builder()
                    .opr_code(row.getCell(0).getStringCellValue())
                    .lineCode(row.getCell(2).getStringCellValue())
                    .lineNum(row.getCell(3).getStringCellValue())
                    .stationCode(row.getCell(4).getStringCellValue())
                    .stationName(row.getCell(5).getStringCellValue())
                    .build();

            excelRepository.save(excelEntity);
        }

        return "excelList";
    }
}
