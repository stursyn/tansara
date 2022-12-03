package kz.greetgo.sandbox.db.report.main;

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

public class MainViewXlsx implements ReportView {
  SXSSFWorkbook workbook = new SXSSFWorkbook(500);
  private OutputStream out;
  private Sheet sheet;
  private XSSFCellStyle cellStyle;
  private XSSFCellStyle headerStyle;

  public MainViewXlsx(OutputStream out) {
    this.out = out;
  }

  private XSSFCellStyle getHeaderStyle() {
    if(headerStyle != null) return headerStyle;
    headerStyle = (XSSFCellStyle) workbook.createCellStyle();
    return headerStyle;
  }

  private XSSFCellStyle createCellStyle() {
    if(cellStyle != null) return cellStyle;
    cellStyle = (XSSFCellStyle) workbook.createCellStyle();
    return cellStyle;
  }

  @Override
  public void start(ReportHeaderData headerData) {
    if (headerData instanceof MainHeaderData) {
      MainHeaderData data = (MainHeaderData) headerData;
      sheet = workbook.createSheet(data.sheetName);
      sheet.setColumnWidth(0,6000);
      sheet.setColumnWidth(1,6000);
      sheet.setColumnWidth(2,6000);
      sheet.setColumnWidth(3,6000);
      sheet.setColumnWidth(4,6000);
      sheet.setColumnWidth(5,6000);
      sheet.setColumnWidth(6,6000);
      sheet.setColumnWidth(7,6000);
      sheet.setColumnWidth(8,6000);
      sheet.setColumnWidth(9,6000);
      sheet.setColumnWidth(10,6000);
      sheet.setColumnWidth(11,6000);
      sheet.setColumnWidth(12,6000);
      sheet.setColumnWidth(13,6000);
      sheet.setColumnWidth(14,6000);
      sheet.setColumnWidth(15,6000);

      {
        Row headerRow2 = sheet.createRow(0);
        setStrCellValue(headerRow2, 0, "Нумерация", false, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)),
            new XSSFColor(new java.awt.Color(191,191,191)), HorizontalAlignment.CENTER, true);
        setStrCellValue(headerRow2, 1, "Каталог", false, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)),
            new XSSFColor(new java.awt.Color(191,191,191)), HorizontalAlignment.CENTER, true);
        setStrCellValue(headerRow2, 2, "Коллекция и Хранения в таре, мл", false, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)), new XSSFColor(new java.awt.Color(191,191,191)), HorizontalAlignment.CENTER, true);
        setStrCellValue(headerRow2, 3, "Название семейства", false, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)), new XSSFColor(new java.awt.Color(191,191,191)), HorizontalAlignment.CENTER, true);
        setStrCellValue(headerRow2, 4, "Название рода", false, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)), new XSSFColor(new java.awt.Color(191,191,191)), HorizontalAlignment.CENTER, true);
        setStrCellValue(headerRow2, 5, "Название вида", false, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)), new XSSFColor(new java.awt.Color(191,191,191)), HorizontalAlignment.CENTER, true);
        setStrCellValue(headerRow2, 6, "№ флор р-на", false, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)), new XSSFColor(new java.awt.Color(191,191,191)), HorizontalAlignment.CENTER, true);
        setStrCellValue(headerRow2, 7, "Место сбора, административный район", false, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)), new XSSFColor(new java.awt.Color(191,191,191)), HorizontalAlignment.CENTER, true);
        setStrCellValue(headerRow2, 8, "Координаты сбора (N,E)", false, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)), new XSSFColor(new java.awt.Color(191,191,191)), HorizontalAlignment.CENTER, true);
        setStrCellValue(headerRow2, 9, "Высота над уров-нем моря, м", false, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)), new XSSFColor(new java.awt.Color(191,191,191)), HorizontalAlignment.CENTER, true);
        setStrCellValue(headerRow2, 10, "Важное хозяйс. значение", false, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)), new XSSFColor(new java.awt.Color(191,191,191)), HorizontalAlignment.CENTER, true);
        setStrCellValue(headerRow2, 11, "Год сбора", false, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)), new XSSFColor(new java.awt.Color(191,191,191)), HorizontalAlignment.CENTER, true);
        setStrCellValue(headerRow2, 12, "Масса 1000 семян, г", false, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)), new XSSFColor(new java.awt.Color(191,191,191)), HorizontalAlignment.CENTER, true);
        setStrCellValue(headerRow2, 13, "Всхож %", false, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)), new XSSFColor(new java.awt.Color(191,191,191)), HorizontalAlignment.CENTER, true);
        setStrCellValue(headerRow2, 14, "Кем собрано", false, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)), new XSSFColor(new java.awt.Color(191,191,191)), HorizontalAlignment.CENTER, true);
        setStrCellValue(headerRow2, 15, "Жизненная форма", false, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)), new XSSFColor(new java.awt.Color(191,191,191)), HorizontalAlignment.CENTER, true);
        setStrCellValue(headerRow2, 16, "Вид описания", false, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)), new XSSFColor(new java.awt.Color(191,191,191)), HorizontalAlignment.CENTER, true);
        setStrCellValue(headerRow2, 17, "Вид фотография", false, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)), new XSSFColor(new java.awt.Color(191,191,191)), HorizontalAlignment.CENTER, true);
      }
    }
  }

  private Cell setStrCellValue(Row row, int cellIndex, String value, boolean setCellStyle){
    return setStrCellValue(row, cellIndex, value,false, 0, null, null, setCellStyle);
  }


  private Cell setStrCellValue(Row row, int cellIndex, String value, int border){
    return setStrCellValue(row, cellIndex, value, false, border, null, null, false);
  }

  private Cell setStrCellValue(Row row, int cellIndex, String value, int border, HorizontalAlignment horizontalAlignment, boolean setCellStyle){
    return setStrCellValue(row, cellIndex, value, false, border, null, horizontalAlignment, setCellStyle);
  }

  private Cell setStrCellValue(Row row, int cellIndex, String value, boolean bold,  int border, boolean setCellStyle){
    return setStrCellValue(row, cellIndex, value, bold, border, null, null, setCellStyle);
  }

  private Cell setStrCellValue(Row row, int cellIndex, String value, boolean bold,  int border, HorizontalAlignment horizontalAlignment, boolean setCellStyle){
    return setStrCellValue(row, cellIndex, value, bold, border, null, horizontalAlignment, setCellStyle);
  }

  private Cell setStrCellValue(Row row, int cellIndex, String value, boolean bold, int border, XSSFColor foregroundColor, HorizontalAlignment horizontalAlignment, boolean setCellStyle) {
    if (value == null) value="";

    Cell cell = row.createCell(cellIndex);
    setCellStyle(bold, border, cell, foregroundColor, horizontalAlignment, setCellStyle);
    cell.setCellValue(value);
    return cell;
  }

  private XSSFCellStyle setCellStyle(boolean bold, int border, Cell cell, XSSFColor foregroundColor, HorizontalAlignment horizontalAlignment, boolean setCellStyle) {
    XSSFCellStyle cellStyleInner = createCellStyle();
    if(setCellStyle) cellStyleInner = getHeaderStyle();

    if(foregroundColor!=null) {
      cellStyleInner.setFillForegroundColor(foregroundColor);
      cellStyleInner.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    if(horizontalAlignment!=null) cellStyleInner.setAlignment(horizontalAlignment);

    Font font = workbook.createFont();
    font.setFontName("Liberation Sans");
    font.setFontHeightInPoints((short) 10);
    if(bold) {
      font.setBold(bold);
    }
    cellStyleInner.setFont(font);
    if(border>0) {
      if ((2&border) == 2){
        cellStyleInner.setBorderRight(BorderStyle.THIN);
      }
      if ((4&border) == 4){
        cellStyleInner.setBorderTop(BorderStyle.THIN);
      }
      if ((8&border) == 8){
        cellStyleInner.setBorderLeft(BorderStyle.THIN);
      }
      if ((16&border) == 16){
        cellStyleInner.setBorderBottom(BorderStyle.THIN);
      }
    }

    cell.setCellStyle(cellStyleInner);
    return cellStyleInner;
  }

  private Cell setIntCellValue(Row row, int cellIndex, Integer value, boolean setCellStyle){
    return setIntCellValue(row, cellIndex, value,false, 0, null, setCellStyle);
  }

  private Cell setIntCellValue(Row row, int cellIndex, Integer value, boolean bold, boolean setCellStyle){
    return setIntCellValue(row, cellIndex, value,bold, 0, null, setCellStyle);
  }

  private Cell setIntCellValue(Row row, int cellIndex, Integer value, boolean bold, int border, boolean setCellStyle){
    return setIntCellValue(row, cellIndex, value, bold, border, null, setCellStyle);
  }

  private Cell setIntCellValue(Row row, int cellIndex, Integer value, int border, boolean setCellStyle){
    return setIntCellValue(row, cellIndex, value, false, border, null, setCellStyle);
  }

  private Cell setIntCellValue(Row row, int cellIndex, Integer value, int border, HorizontalAlignment horizontalAlignment, boolean setCellStyle){
    return setIntCellValue(row, cellIndex, value, false, border, horizontalAlignment, setCellStyle);
  }

  private Cell setIntCellValue(Row row, int cellIndex, Integer value, boolean bold, int border, HorizontalAlignment horizontalAlignment, boolean setCellStyle) {
    if (value == null) value = 0;

    Cell cell = row.createCell(cellIndex);
    setCellStyle(bold, border, cell, null, horizontalAlignment, setCellStyle);
    cell.setCellValue(value);

    return cell;
  }

  @Override
  public void addRow(ReportRow row) {
    if (row instanceof MainRow) {
      MainRow use = (MainRow) row;
      Row sheetRow = sheet.createRow(sheet.getPhysicalNumberOfRows());

      setStrCellValue(sheetRow, 0, use.num + "", (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)));
      setStrCellValue(sheetRow, 1, use.catalog, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)));
      setStrCellValue(sheetRow, 2, use.collection, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)));
      setStrCellValue(sheetRow, 3, use.familyName, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)));
      setStrCellValue(sheetRow, 4, use.generateName, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)));
      setStrCellValue(sheetRow, 5, use.typeName, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)), HorizontalAlignment.CENTER, false);
      setStrCellValue(sheetRow, 6, use.floraRegionNumber, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)), HorizontalAlignment.CENTER, false);
      setStrCellValue(sheetRow, 7, use.collectPlace, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)), HorizontalAlignment.CENTER, false);
      setStrCellValue(sheetRow, 8, use.collectCoordinate, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)), HorizontalAlignment.CENTER, false);
      setStrCellValue(sheetRow, 9, use.heightFromWater, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)), HorizontalAlignment.CENTER, false);
      setStrCellValue(sheetRow, 10, use.importantStaff, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)), HorizontalAlignment.CENTER, false);
      setStrCellValue(sheetRow, 11, use.collectYear, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)), HorizontalAlignment.CENTER, false);
      setStrCellValue(sheetRow, 12, use.seedWeight, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)), HorizontalAlignment.CENTER, false);
      setStrCellValue(sheetRow, 13, use.accuracyRate, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)), HorizontalAlignment.CENTER, false);
      setStrCellValue(sheetRow, 14, use.whoIsCollect, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)), HorizontalAlignment.CENTER, false);
      setStrCellValue(sheetRow, 15, use.lifeForm, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)), HorizontalAlignment.CENTER, false);
      setStrCellValue(sheetRow, 16, use.description, (int)(Math.pow(2.0, 1) + Math.pow(2.0, 2)), HorizontalAlignment.CENTER, false);

      if(use.hasImage && use.image.length > 0) {
        final CreationHelper helper = workbook.getCreationHelper();
        final Drawing drawing = sheet.createDrawingPatriarch();
        final ClientAnchor anchor = helper.createClientAnchor();
        final int pictureIndex =
            workbook.addPicture(use.image, Workbook.PICTURE_TYPE_PNG);

        anchor.setCol1(17);
        anchor.setRow1(sheetRow.getRowNum()); // same row is okay
        final Picture pict = drawing.createPicture(anchor, pictureIndex);
        pict.resize();
      }
    }
  }

  @Override
  public void finish(ReportFooterData footerData) throws IOException {
    if (footerData instanceof MainFooterData) {
      MainFooterData use = (MainFooterData) footerData;
      Row sheetRow = sheet.createRow(sheet.getPhysicalNumberOfRows());

      setStrCellValue(sheetRow, 0, "", (int)(Math.pow(2.0, 2)));
      setStrCellValue(sheetRow, 1, "", (int)(Math.pow(2.0, 2)));
      setStrCellValue(sheetRow, 2, "", (int)(Math.pow(2.0, 2)));
      setStrCellValue(sheetRow, 3, "", (int)(Math.pow(2.0, 2)));
      setStrCellValue(sheetRow, 4, "", (int)(Math.pow(2.0, 2)));
      setStrCellValue(sheetRow, 5, "", (int)(Math.pow(2.0, 2)), HorizontalAlignment.CENTER, true);
      setStrCellValue(sheetRow, 6, "", (int)(Math.pow(2.0, 2)), HorizontalAlignment.CENTER, true);
      setStrCellValue(sheetRow, 7, "", (int)(Math.pow(2.0, 2)), HorizontalAlignment.CENTER, true);
      setStrCellValue(sheetRow, 8, "", (int)(Math.pow(2.0, 2)), HorizontalAlignment.CENTER, true);
      setStrCellValue(sheetRow, 9, "", (int)(Math.pow(2.0, 2)), HorizontalAlignment.CENTER, true);
      setStrCellValue(sheetRow, 10, "", (int)(Math.pow(2.0, 2)), HorizontalAlignment.CENTER, true);
      setStrCellValue(sheetRow, 11, "", (int)(Math.pow(2.0, 2)), HorizontalAlignment.CENTER, true);
      setStrCellValue(sheetRow, 12, "", (int)(Math.pow(2.0, 2)), HorizontalAlignment.CENTER, true);
      setStrCellValue(sheetRow, 13, "", (int)(Math.pow(2.0, 2)), HorizontalAlignment.CENTER, true);
      setStrCellValue(sheetRow, 14, "", (int)(Math.pow(2.0, 2)), HorizontalAlignment.CENTER, true);
      setStrCellValue(sheetRow, 15, "", (int)(Math.pow(2.0, 2)), HorizontalAlignment.CENTER, true);
      setStrCellValue(sheetRow, 16, "", (int)(Math.pow(2.0, 2)), HorizontalAlignment.CENTER, true);
      setStrCellValue(sheetRow, 17, "", (int)(Math.pow(2.0, 2)), HorizontalAlignment.CENTER, true);
    }
  }

  @Override
  public void close() throws IOException {
    workbook.write(out);
    workbook.dispose();
  }
}
