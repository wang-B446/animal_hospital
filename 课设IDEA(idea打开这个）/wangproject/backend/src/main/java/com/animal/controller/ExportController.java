package com.animal.controller;

import com.animal.entity.Animal;
import com.animal.entity.MedicalRecord;
import com.animal.mapper.AnimalMapper;
import com.animal.mapper.MedicalRecordMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/api/export")
public class ExportController {

    @Autowired
    private AnimalMapper animalMapper;

    @Autowired
    private MedicalRecordMapper medicalRecordMapper;

    @GetMapping("/animals")
    public ResponseEntity<byte[]> exportAnimals() throws IOException {
        List<Animal> list = animalMapper.findAll();
        Workbook workbook = createAnimalExcel(list);
        return buildResponse(workbook, "动物列表.xlsx");
    }

    @GetMapping("/records")
    public ResponseEntity<byte[]> exportRecords() throws IOException {
        List<MedicalRecord> list = medicalRecordMapper.findAll();
        Workbook workbook = createRecordExcel(list);
        return buildResponse(workbook, "就诊记录.xlsx");
    }

    private Workbook createAnimalExcel(List<Animal> list) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("动物列表");
        String[] headers = {"ID", "名称", "种类", "品种", "性别", "年龄(月)", "体重(kg)", "主人", "症状"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }
        for (int i = 0; i < list.size(); i++) {
            Animal a = list.get(i);
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(a.getId());
            row.createCell(1).setCellValue(a.getName() != null ? a.getName() : "");
            row.createCell(2).setCellValue(a.getSpecies() != null ? a.getSpecies() : "");
            row.createCell(3).setCellValue(a.getBreed() != null ? a.getBreed() : "");
            row.createCell(4).setCellValue(a.getGender() != null ? a.getGender() : "");
            row.createCell(5).setCellValue(a.getAge() != null ? a.getAge() : 0);
            row.createCell(6).setCellValue(a.getWeight() != null ? a.getWeight().doubleValue() : 0);
            row.createCell(7).setCellValue(a.getOwnerName() != null ? a.getOwnerName() : "");
            row.createCell(8).setCellValue(a.getSymptom() != null ? a.getSymptom() : "");
        }
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        return workbook;
    }

    private Workbook createRecordExcel(List<MedicalRecord> list) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("就诊记录");
        String[] headers = {"ID", "动物", "医生", "就诊日期", "症状", "诊断", "治疗方案", "费用(元)", "缴费状态"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }
        for (int i = 0; i < list.size(); i++) {
            MedicalRecord r = list.get(i);
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(r.getId());
            row.createCell(1).setCellValue(r.getAnimalName() != null ? r.getAnimalName() : "");
            row.createCell(2).setCellValue(r.getDoctorName() != null ? r.getDoctorName() : "");
            row.createCell(3).setCellValue(r.getVisitDate() != null ? r.getVisitDate() : "");
            row.createCell(4).setCellValue(r.getSymptom() != null ? r.getSymptom() : "");
            row.createCell(5).setCellValue(r.getDiagnosis() != null ? r.getDiagnosis() : "");
            row.createCell(6).setCellValue(r.getTreatment() != null ? r.getTreatment() : "");
            row.createCell(7).setCellValue(r.getCost() != null ? r.getCost().doubleValue() : 0);
            row.createCell(8).setCellValue(r.getPaymentStatus() != null ? r.getPaymentStatus() : "未缴费");
        }
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        return workbook;
    }

    private ResponseEntity<byte[]> buildResponse(Workbook workbook, String filename) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", URLEncoder.encode(filename, "UTF-8"));
        return ResponseEntity.ok().headers(headers).body(out.toByteArray());
    }
}
