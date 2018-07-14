package kz.greetgo.sandbox.db.test.util;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import kz.greetgo.sandbox.controller.model.DictRecord;
import kz.greetgo.sandbox.controller.model.FloraRecord;
import kz.greetgo.sandbox.controller.util.FileReadUtil;
import kz.greetgo.sandbox.controller.util.resources.UtilResources;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.testng.annotations.Test;
import org.testng.collections.Lists;

import java.io.IOException;
import java.util.Dictionary;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class UtilTest extends ParentTestNg {

  @Test
  public void xlsxFileReadTest1() {
    List<FloraRecord> list = Lists.newArrayList();

    Function<XWPFTableRow, String> testFunction = (XWPFTableRow row) -> {
      FloraRecord floraRecord = new FloraRecord();
      AtomicInteger cellCount = new AtomicInteger(0);
      row.getTableCells().forEach( xwpfTableCell -> {
        switch (cellCount.getAndIncrement()){
          case 1:
            floraRecord.num = xwpfTableCell.getText();
            break;
          case 2:
            floraRecord.catalog = xwpfTableCell.getText();
            break;
          case 3:
            floraRecord.typeTitle = xwpfTableCell.getText();
            break;
          case 4:
            floraRecord.familyTitle = xwpfTableCell.getText();
            break;
          case 5:
            floraRecord.floraNum = xwpfTableCell.getText();
            break;
          case 6:
            floraRecord.collectPlace = xwpfTableCell.getText();
            break;
          case 7:
            floraRecord.collectCoordinate = xwpfTableCell.getText();
            break;
          case 8:
            floraRecord.collectAltitude = xwpfTableCell.getText();
            break;
          case 9:
            floraRecord.collectDate = xwpfTableCell.getText();
            break;
          case 10:
            floraRecord.floraWeight = xwpfTableCell.getText();
            break;
          case 11:
            floraRecord.collectedBy = xwpfTableCell.getText();
            break;
          case 12:
            floraRecord.behaviorPercent = xwpfTableCell.getText();
            break;
        }
      });
      System.out.println("insert into flora(id, num, catalog, collectedBy, typeTitle, familyTitle, " +
          "floraNum, collectPlace, collectCoordinate, collectAltitude, collectDate, floraWight, behaviorPercent) values(nextval('num'), "  +
          "'"+floraRecord.num+"','"+floraRecord.catalog+"','"+floraRecord.collectedBy+"','"+floraRecord.typeTitle+"','"+floraRecord.familyTitle+"'," +
          "'"+floraRecord.floraNum+"','"+floraRecord.collectPlace+"','"+floraRecord.collectCoordinate.replaceAll("'","''")+"','"+floraRecord.collectAltitude+"','"+floraRecord.collectDate+"'," +
          "'"+floraRecord.floraWeight+"','"+floraRecord.behaviorPercent+"');;");
      return null;
    };

    try {
      FileReadUtil.readDocxTable(UtilResources.class.getResourceAsStream("test1.docx"), testFunction);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void xlsxFileReadTest2() {
    List<FloraRecord> list = Lists.newArrayList();

    Function<XWPFTableRow, String> testFunction = (XWPFTableRow row) -> {
      FloraRecord floraRecord = new FloraRecord();
      AtomicInteger cellCount = new AtomicInteger(0);
      row.getTableCells().forEach( xwpfTableCell -> {
        switch (cellCount.getAndIncrement()){
          case 1:
            floraRecord.num = xwpfTableCell.getText();
            break;
          case 2:
            floraRecord.catalog = xwpfTableCell.getText();
            break;
          case 3:
            floraRecord.typeTitle = xwpfTableCell.getText();
            break;
          case 4:
            floraRecord.familyTitle = xwpfTableCell.getText();
            break;
          case 5:
            floraRecord.useReason = xwpfTableCell.getText();
            break;
          case 6:
            floraRecord.floraNum = xwpfTableCell.getText();
            break;
          case 7:
            floraRecord.collectPlace = xwpfTableCell.getText();
            break;
          case 8:
            floraRecord.collectCoordinate = xwpfTableCell.getText();
            break;
          case 9:
            floraRecord.collectAltitude = xwpfTableCell.getText();
            break;
          case 10:
            floraRecord.collectDate = xwpfTableCell.getText();
            break;
          case 11:
            floraRecord.collectedBy = xwpfTableCell.getText();
            break;
        }
      });
      System.out.println("insert into flora(id, num, catalog, collectedBy, typeTitle, familyTitle, useReason, " +
          "floraNum, collectPlace, collectCoordinate, collectAltitude, collectDate) values(nextval('num'), "  +
          "'"+floraRecord.num+"','"+floraRecord.catalog+"','"+floraRecord.collectedBy+"','"+floraRecord.typeTitle+"','"+floraRecord.familyTitle+"','" + floraRecord.useReason+"'," +
          "'"+floraRecord.floraNum+"','"+floraRecord.collectPlace+"','"+floraRecord.collectCoordinate.replaceAll("'","´")+"','"+floraRecord.collectAltitude+"','"+floraRecord.collectDate+"'" +
          ");;");
      return null;
    };

    try {
      FileReadUtil.readDocxTable(UtilResources.class.getResourceAsStream("test2.docx"), testFunction);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void xlsxFileReadTest3() {
    Set<String> familyCode = Sets.newHashSet();
    Set<String> genusCode = Sets.newHashSet();
    Set<String> typeCode = Sets.newHashSet();

    Function<XWPFTableRow, String> testFunction = (XWPFTableRow row) -> {
      if(!familyCode.contains(row.getTableCells().get(1).getText().trim())) {
        System.out.println("insert into table_of_dicts(code, title, dictType) values('" + row.getTableCells().get(1).getText().trim() + "', " +
          "'" + row.getTableCells().get(1).getText().trim() + "', 'FAMILY');;");
        familyCode.add(row.getTableCells().get(1).getText().trim());
      }

      if(!genusCode.contains(row.getTableCells().get(3).getText().trim())) {
        System.out.println("insert into table_of_dicts(code, title, dictType, parent) values('" + row.getTableCells().get(3).getText().trim() + "', " +
          "'" + row.getTableCells().get(5).getText().trim() + "', " +
          "'GENUS', '" + row.getTableCells().get(1).getText().trim() + "');;");
        genusCode.add(row.getTableCells().get(3).getText().trim());
      }

      if(!typeCode.contains(row.getTableCells().get(4).getText().trim())) {
        System.out.println("insert into table_of_dicts(code, title, dictType, parent) values('" + row.getTableCells().get(4).getText().trim() + "', " +
          "'" + row.getTableCells().get(6).getText().trim() + "', 'TYPE', " +
          "'" + row.getTableCells().get(3).getText().trim() + "');;");
        typeCode.add(row.getTableCells().get(4).getText().trim());
      }

      return null;
    };

    try {
      FileReadUtil.readDocxTable(UtilResources.class.getResourceAsStream("test3.docx"), testFunction);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
