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
          "'" + row.getTableCells().get(3).getText().trim()+"/"+row.getTableCells().get(5).getText().trim() + "', " +
          "'GENUS', '" + row.getTableCells().get(1).getText().trim() + "');;");
        genusCode.add(row.getTableCells().get(3).getText().trim());
      }

      if(!typeCode.contains(row.getTableCells().get(4).getText().trim())) {
        System.out.println("insert into table_of_dicts(code, title, dictType, parent) values('" + row.getTableCells().get(4).getText().trim() + "', " +
          "'" + row.getTableCells().get(4).getText().trim() + "/" + row.getTableCells().get(6).getText().trim() + "', 'TYPE', " +
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
