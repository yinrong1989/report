package com.yinrong.report.enums;

import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Classname FileType
 * @Description
 * @Date 2020/6/16 8:43 下午
 * @Created by yinrong
 */
public enum FileType {
    CSV("csv"){
        @Override
        public byte[] createFile(List<Map> mapList,String fileName) {
            return null;
        }
    },
    XLS("xls"){
        @Override
        public byte[] createFile(List<Map> mapList,String fileName) {
            List<String >head =new ArrayList<>();
            HSSFWorkbook hssfWorkbook = null;
            if (mapList.isEmpty()){
                hssfWorkbook = getBlankHSSFWorkbook(fileName);
            }else{
                mapList.get(0).forEach((k,v) ->head.add(k.toString()));
                List<List<String>> rowList = new ArrayList<>();
                for (Map m:mapList){
                    List<String> row =  new ArrayList<>();
                    try {
                        m.forEach((k,v) ->{
                            if (v instanceof BigDecimal){
                                row.add(((BigDecimal)v).toPlainString());
                            }else if (v == null){
                                row.add("");
                            }
                            else{
                                row.add(String.valueOf(v));
                            }

                           // rowList.add(row);
                        });
                        rowList.add(row);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(m);
                    }
                }
                hssfWorkbook = getHSSFWorkbook(fileName,head.toArray(new String[head.size()]),rowList);
            }

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                hssfWorkbook.write(os);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return os.toByteArray();
        }

    },
    XLSX("xlsx"){
        @Override
        public byte[] createFile(List<Map> mapList,String fileName) {
            return null;
        }
    }
    ;

    String type;

    FileType(String type) {
        this.type = type;
    }

    public abstract byte[] createFile(List<Map> mapList,String fileName);
    public String buildFileName(String fileName){
       return fileName+"."+type;
    }


    public static FileType  getFileType(String type){
        for (FileType fileType:FileType.values()){
            if (fileType.name().equalsIgnoreCase(type)){
                return fileType;
            }
        }

       return  FileType.XLS;
    }

    public static class FileContext{
        public String[] columnsNames;

        public List<Object[]> valueList;

        public String[] getColumnsNames() {
            return columnsNames;
        }

        public void setColumnsNames(String[] columnsNames) {
            this.columnsNames = columnsNames;
        }

        public List<Object[]> getValueList() {
            return valueList;
        }

        public void setValueList(List<Object[]> valueList) {
            this.valueList = valueList;
        }
    }
    public static HSSFWorkbook getBlankHSSFWorkbook(String title){
        //创建一个HSSFWorkbook，对应一个Excel文件
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();

        //在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet hssfSheet = hssfWorkbook.createSheet(title);

        //创建标题合并行
        hssfSheet.addMergedRegion(new CellRangeAddress(0,(short)0,0,10));

        //设置标题样式
        HSSFCellStyle style = hssfWorkbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);   //设置居中样式
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置标题字体
        Font titleFont = hssfWorkbook.createFont();
        titleFont.setFontHeightInPoints((short) 14);
        style.setFont(titleFont);

        //设置值表头样式 设置表头居中
        HSSFCellStyle hssfCellStyle = hssfWorkbook.createCellStyle();
        hssfCellStyle.setAlignment(HorizontalAlignment.CENTER);   //设置居中样式
        hssfCellStyle.setBorderBottom(BorderStyle.THIN);
        hssfCellStyle.setBorderLeft(BorderStyle.THIN);
        hssfCellStyle.setBorderRight(BorderStyle.THIN);
        hssfCellStyle.setBorderTop(BorderStyle.THIN);

        //设置表内容样式
        //创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style1 = hssfWorkbook.createCellStyle();
        style1.setBorderBottom(BorderStyle.THIN);
        style1.setBorderLeft(BorderStyle.THIN);
        style1.setBorderRight(BorderStyle.THIN);
        style1.setBorderTop(BorderStyle.THIN);

        //产生标题行
        HSSFRow hssfRow = hssfSheet.createRow(0);
        HSSFCell cell = hssfRow.createCell(0);
        cell.setCellValue(title);
        cell.setCellStyle(style);



        //产生表头
        HSSFRow row1 = hssfSheet.createRow(1);
        return hssfWorkbook;
    }
    public static HSSFWorkbook getHSSFWorkbook(String title, String headers[], List<List<String>> values){

        //创建一个HSSFWorkbook，对应一个Excel文件
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();

        //在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet hssfSheet = hssfWorkbook.createSheet(title);

        //创建标题合并行
        hssfSheet.addMergedRegion(new CellRangeAddress(0,(short)0,0,(short)headers.length - 1));

        //设置标题样式
        HSSFCellStyle style = hssfWorkbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);   //设置居中样式
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置标题字体
        Font titleFont = hssfWorkbook.createFont();
        titleFont.setFontHeightInPoints((short) 14);
        style.setFont(titleFont);

        //设置值表头样式 设置表头居中
        HSSFCellStyle hssfCellStyle = hssfWorkbook.createCellStyle();
        hssfCellStyle.setAlignment(HorizontalAlignment.CENTER);   //设置居中样式
        hssfCellStyle.setBorderBottom(BorderStyle.THIN);
        hssfCellStyle.setBorderLeft(BorderStyle.THIN);
        hssfCellStyle.setBorderRight(BorderStyle.THIN);
        hssfCellStyle.setBorderTop(BorderStyle.THIN);

        //设置表内容样式
        //创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style1 = hssfWorkbook.createCellStyle();
        style1.setBorderBottom(BorderStyle.THIN);
        style1.setBorderLeft(BorderStyle.THIN);
        style1.setBorderRight(BorderStyle.THIN);
        style1.setBorderTop(BorderStyle.THIN);

        //产生标题行
        HSSFRow hssfRow = hssfSheet.createRow(0);
        HSSFCell cell = hssfRow.createCell(0);
        cell.setCellValue(title);
        cell.setCellStyle(style);



        //产生表头
        HSSFRow row1 = hssfSheet.createRow(1);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell hssfCell = row1.createCell(i);
            hssfCell.setCellValue(headers[i]);
            hssfCell.setCellStyle(hssfCellStyle);
        }

        //创建内容
        for (int i = 0; i <values.size(); i++){
            row1 = hssfSheet.createRow(i +2);
            for (int j = 0; j < values.get(i).size(); j++){
                //将内容按顺序赋给对应列对象
                HSSFCell hssfCell = row1.createCell(j);
                hssfCell.setCellValue(values.get(i).get(j));
                hssfCell.setCellStyle(style1);
            }
        }
        return hssfWorkbook;
    }
    private static Workbook getWorkbook(ExcelType type, int size) {
        if (ExcelType.HSSF.equals(type)) {
            return new HSSFWorkbook();
        } else if (size < 100000) {
            return new XSSFWorkbook();
        } else {
            return new SXSSFWorkbook();
        }
    }

}

