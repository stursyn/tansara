package kz.greetgo.sandbox.db.report.dict;

import kz.greetgo.sandbox.db.report.ReportFooterData;
import kz.greetgo.sandbox.db.report.ReportHeaderData;
import kz.greetgo.sandbox.db.report.ReportRow;
import kz.greetgo.sandbox.db.report.ReportView;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;

public class DictViewXlsx implements ReportView {
  SXSSFWorkbook workbook = new SXSSFWorkbook(500);
  private OutputStream out;
  private Sheet sheet;

  public DictViewXlsx(OutputStream out) {
    this.out = out;
  }

  @Override
  public void start(ReportHeaderData headerData) {
    if (headerData instanceof DictHeaderData) {
      DictHeaderData data = (DictHeaderData) headerData;
      sheet = workbook.createSheet(data.sheetName);
      sheet.setColumnWidth(0,6000);
      sheet.setColumnWidth(1,6000);
      sheet.setColumnWidth(2,6000);
      sheet.setColumnWidth(3,6000);
      sheet.setColumnWidth(4,6000);

      {
        Row headerRow2 = sheet.createRow(0);
        setStrCellValue(headerRow2, 0, "Код", false, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)),
            new XSSFColor(new java.awt.Color(191,191,191)), HorizontalAlignment.CENTER);
        setStrCellValue(headerRow2, 1, "Наименование", false, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)),
            new XSSFColor(new java.awt.Color(191,191,191)), HorizontalAlignment.CENTER);
        setStrCellValue(headerRow2, 2, "Код родителя", false, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)), new XSSFColor(new java.awt.Color(191,191,191)), HorizontalAlignment.CENTER);
        setStrCellValue(headerRow2, 3, "Наименование родителя", false, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)), new XSSFColor(new java.awt.Color(191,191,191)), HorizontalAlignment.CENTER);
        setStrCellValue(headerRow2, 4, "Тип справочника", false, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)), new XSSFColor(new java.awt.Color(191,191,191)), HorizontalAlignment.CENTER);
      }
    }
  }

  private Cell setStrCellValue(Row row, int cellIndex, String value){
    return setStrCellValue(row, cellIndex, value,false, 0, null, null);
  }

  private Cell setStrCellValue(Row row, int cellIndex, String value, boolean bold){
    return setStrCellValue(row, cellIndex, value,bold, 0, null, null);
  }

  private Cell setStrCellValue(Row row, int cellIndex, String value, int border){
    return setStrCellValue(row, cellIndex, value, false, border, null, null);
  }

  private Cell setStrCellValue(Row row, int cellIndex, String value, int border, HorizontalAlignment horizontalAlignment){
    return setStrCellValue(row, cellIndex, value, false, border, null, horizontalAlignment);
  }

  private Cell setStrCellValue(Row row, int cellIndex, String value, boolean bold,  int border){
    return setStrCellValue(row, cellIndex, value, bold, border, null, null);
  }

  private Cell setStrCellValue(Row row, int cellIndex, String value, boolean bold,  int border, HorizontalAlignment horizontalAlignment){
    return setStrCellValue(row, cellIndex, value, bold, border, null, horizontalAlignment);
  }

  private Cell setStrCellValue(Row row, int cellIndex, String value, boolean bold, int border, XSSFColor foregroundColor, HorizontalAlignment horizontalAlignment) {
    if (value == null) value="";

    Cell cell = row.createCell(cellIndex);
    setCellStyle(bold, border, cell, foregroundColor, horizontalAlignment);
    cell.setCellValue(value);
    return cell;
  }

  private XSSFCellStyle setCellStyle(boolean bold, int border, Cell cell, XSSFColor foregroundColor, HorizontalAlignment horizontalAlignment) {
    XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();
    if(foregroundColor!=null) {
      cellStyle.setFillForegroundColor(foregroundColor);
      cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    if(horizontalAlignment!=null) cellStyle.setAlignment(horizontalAlignment);

    Font font = workbook.createFont();
    font.setFontName("Liberation Sans");
    font.setFontHeightInPoints((short) 10);
    if(bold) {
      font.setBold(bold);
    }
    cellStyle.setFont(font);
    if(border>0) {
      if ((2&border) == 2){
        cellStyle.setBorderRight(BorderStyle.THIN);
      }
      if ((4&border) == 4){
        cellStyle.setBorderTop(BorderStyle.THIN);
      }
      if ((8&border) == 8){
        cellStyle.setBorderLeft(BorderStyle.THIN);
      }
      if ((16&border) == 16){
        cellStyle.setBorderBottom(BorderStyle.THIN);
      }
    }

    cell.setCellStyle(cellStyle);
    return cellStyle;
  }

  private Cell setIntCellValue(Row row, int cellIndex, Integer value){
    return setIntCellValue(row, cellIndex, value,false, 0, null);
  }

  private Cell setIntCellValue(Row row, int cellIndex, Integer value, boolean bold){
    return setIntCellValue(row, cellIndex, value,bold, 0, null);
  }

  private Cell setIntCellValue(Row row, int cellIndex, Integer value, boolean bold, int border){
    return setIntCellValue(row, cellIndex, value, bold, border, null);
  }

  private Cell setIntCellValue(Row row, int cellIndex, Integer value, int border){
    return setIntCellValue(row, cellIndex, value, false, border, null);
  }

  private Cell setIntCellValue(Row row, int cellIndex, Integer value, int border, HorizontalAlignment horizontalAlignment){
    return setIntCellValue(row, cellIndex, value, false, border, horizontalAlignment);
  }

  private Cell setIntCellValue(Row row, int cellIndex, Integer value, boolean bold, int border, HorizontalAlignment horizontalAlignment) {
    if (value == null) value = 0;

    Cell cell = row.createCell(cellIndex);
    setCellStyle(bold, border, cell, null, horizontalAlignment);
    cell.setCellValue(value);

    return cell;
  }

  private void setDoubleCellValue(Row row, int cellIndex, Double value, boolean bold, int border,  HorizontalAlignment horizontalAlignmen) {
    if (value == null) value = 0.0;

    Cell cell = row.createCell(cellIndex);
    XSSFCellStyle xssfCellStyle = setCellStyle(bold, border, cell, null, horizontalAlignmen);
    DataFormat format = workbook.createDataFormat();
    xssfCellStyle.setDataFormat(format.getFormat("###,###.00"));
    cell.setCellValue(value);
  }

  private void setBigDecimalCellValue(Row row, int cellIndex, BigDecimal value, int border, HorizontalAlignment horizontalAlignment) {
    if (value == null) value = BigDecimal.ZERO;

    Cell cell = row.createCell(cellIndex);
    XSSFCellStyle xssfCellStyle = setCellStyle(false, border, cell, null, horizontalAlignment);
    DataFormat format = workbook.createDataFormat();
    xssfCellStyle.setDataFormat(format.getFormat("###,###.00"));
    cell.setCellValue(value.doubleValue());
  }

  @Override
  public void addRow(ReportRow row) {
    if (row instanceof DictRow) {
      DictRow use = (DictRow) row;
      Row sheetRow = sheet.createRow(sheet.getPhysicalNumberOfRows());

      setStrCellValue(sheetRow, 0, use.code, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)));
      setStrCellValue(sheetRow, 1, use.title, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)));
      setStrCellValue(sheetRow, 2, use.parentCode, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)));
      setStrCellValue(sheetRow, 3, use.parentTitle, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)));
      setStrCellValue(sheetRow, 4, use.dictType, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)));
    }
  }

  @Override
  public void finish(ReportFooterData footerData) throws IOException {
    if (footerData instanceof DictFooterData) {
      DictFooterData use = (DictFooterData) footerData;
      Row sheetRow = sheet.createRow(sheet.getPhysicalNumberOfRows());

      setStrCellValue(sheetRow, 0, "", (int)(Math.pow(2.0, 2)), HorizontalAlignment.CENTER);
      setStrCellValue(sheetRow, 1, "", (int)(Math.pow(2.0, 2)), HorizontalAlignment.CENTER);
      setStrCellValue(sheetRow, 2, "", (int)(Math.pow(2.0, 2)), HorizontalAlignment.CENTER);
      setStrCellValue(sheetRow, 3, "", (int)(Math.pow(2.0, 2)), HorizontalAlignment.CENTER);
      setStrCellValue(sheetRow, 4, "", (int)(Math.pow(2.0, 2)), HorizontalAlignment.CENTER);
    }
  }

  @Override
  public void close() throws IOException {
    workbook.write(out);
    workbook.dispose();
  }
}
