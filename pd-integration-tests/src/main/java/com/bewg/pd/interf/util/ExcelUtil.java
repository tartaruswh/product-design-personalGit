package com.bewg.pd.interf.util;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExcelUtil {

    private String fileName;
    // private static Logger logger = Logger.getLogger(ExcelUtil.class);

    public ExcelUtil(String fileName) {
        this.fileName = fileName;
        log.info("得到当前的表格" + fileName);
    }

    public XSSFWorkbook getExcel() throws Exception {
        // 找到一个excel表格
        XSSFWorkbook excelvalue = new XSSFWorkbook(this.fileName);
        // 打印log日志
        log.info("得到文件" + excelvalue);
        return excelvalue;
    }

    public XSSFSheet getsheet(int sheetIndex) throws Exception {
        XSSFWorkbook excelvalue = getExcel();
        // 指定一个当前表格的sheet页
        XSSFSheet sheetvalue = excelvalue.getSheetAt(sheetIndex);
        log.info("得到当前的sheet" + sheetvalue);
        return sheetvalue;
    }

    public Object getValue(int sheetIndex, int rowIndex, int cellIndex) throws Exception {
        Object value = null;
        XSSFSheet sheetvalue = getsheet(sheetIndex);
        // 指定当前sheet的某一行
        XSSFRow rowvalue = sheetvalue.getRow(rowIndex);
        // 指定当前行的某一个单元格
        XSSFCell cellvalue = rowvalue.getCell(cellIndex);
        // 获取此单元格的类型 枚举值 有空 数字 字符串 表达式 空格 等
        value = getCell(cellvalue);

        return value;
    }

    public Object getCell(XSSFCell cellvalue) {
        Object value = null;
        // CellType celltype =cellvalue.getCellTypeEnum();
        // 5.0.0 的 poi 没有 getCellTypeEnum 方法，替换为 getCellType
        CellType celltype = cellvalue.getCellType();
        switch (celltype) {
            case _NONE:
                value = "";
                break;
            case NUMERIC:
                value = cellvalue.getNumericCellValue();
                break;
            case STRING:
                value = cellvalue.getStringCellValue();
                break;
            case FORMULA:
                value = cellvalue.getCellFormula();
                break;
            case BLANK:
                value = "";
                break;
            case BOOLEAN:
                value = cellvalue.getBooleanCellValue();
                break;
            case ERROR:
                value = "ERROR";
                break;
            default:
                value = cellvalue.getDateCellValue();
                break;
        }

        return value;
    }

    public Object[][] fromCellTypeGetCellValue(int sheetIndex) throws Exception {
        // 得到一个sheet
        XSSFSheet sheet = getsheet(sheetIndex);
        // 得到这个sheet中最后一行
        int lastRomnum = sheet.getLastRowNum();
        // 定义一个二维数组，数字11是控制用例模板的长度
        Object[][] caseDate = new Object[lastRomnum][11];
        // 进行每一行的编列
        for (int rowIndex = 1; rowIndex <= lastRomnum; rowIndex++) {
            XSSFRow row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }
            // 当前行的每一个单元格进行遍历
            for (int cellIndex = 0; cellIndex < row.getLastCellNum(); cellIndex++) {
                XSSFCell cellvalue = row.getCell(cellIndex);
                if (cellvalue != null) {
                    Object value = getCell(cellvalue);
                    caseDate[rowIndex - 1][cellIndex] = value;
                } else {
                    caseDate[rowIndex - 1][cellIndex] = "";
                }

            }
        }
        return caseDate;
    }

    public static void main(String[] args) throws Exception {

        ExcelUtil excel = new ExcelUtil("E:\\test.xlsx");
        Object[][] value = excel.fromCellTypeGetCellValue(0);
        System.out.println(value[0][1]);
        System.out.println(value[0][5]);

    }

}
