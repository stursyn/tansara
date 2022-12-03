package kz.greetgo.sandbox.db.register_impl;

import kz.greetgo.sandbox.controller.util.Modules;
import kz.greetgo.sandbox.db.report.ReportView;
import kz.greetgo.sandbox.db.report.main.MainFooterData;
import kz.greetgo.sandbox.db.report.main.MainHeaderData;
import kz.greetgo.sandbox.db.report.main.MainRow;
import kz.greetgo.sandbox.db.report.main.MainViewXlsx;
import kz.greetgo.util.RND;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainViewTest {

  @Test
  public void generateView() throws IOException {
    {
      String dir = Modules.dbDir() + "/build/report/";
      String filename = "partner_dict.xlsx";

      File file = new File(dir + filename);
      file.getParentFile().mkdirs();

      OutputStream out = new FileOutputStream(file);
      try (ReportView v = new MainViewXlsx(out)) {
        {
          MainHeaderData headerData = new MainHeaderData();
          headerData.sheetName = "Реестр по оплатам";

          v.start(headerData);

          for (int i = 0; i < 10000; i++) {
            v.addRow(createRow());
          }
          v.finish(new MainFooterData());
        }
      }
      out.close();
    }
    System.out.println("Complete");
  }

  public MainRow createRow() {
    MainRow row = new MainRow();
    row.num = RND.plusLong(10_000);
    row.accuracyRate = RND.str(12);
    row.catalog = RND.str(12);
    row.collection = RND.str(12);
    row.collectCoordinate = RND.str(12);
    row.collectPlace = RND.str(12);
    row.description = RND.str(12);
    row.familyName = RND.str(12);
    row.floraRegionNumber = RND.str(12);
    row.typeName = RND.str(12);
    return row;
  }
}
