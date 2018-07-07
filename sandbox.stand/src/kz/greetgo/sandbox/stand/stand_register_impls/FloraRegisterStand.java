package kz.greetgo.sandbox.stand.stand_register_impls;

import com.google.common.collect.Lists;
import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.sandbox.controller.errors.AuthError;
import kz.greetgo.sandbox.controller.model.AuthInfo;
import kz.greetgo.sandbox.controller.model.FloraRecord;
import kz.greetgo.sandbox.controller.model.FloraToFilter;
import kz.greetgo.sandbox.controller.model.UserInfo;
import kz.greetgo.sandbox.controller.register.AuthRegister;
import kz.greetgo.sandbox.controller.register.FloraRegister;
import kz.greetgo.sandbox.controller.register.model.SessionInfo;
import kz.greetgo.sandbox.controller.register.model.UserParamName;
import kz.greetgo.sandbox.controller.security.SecurityError;
import kz.greetgo.sandbox.controller.util.FileReadUtil;
import kz.greetgo.sandbox.controller.util.resources.UtilResources;
import kz.greetgo.sandbox.db.stand.beans.StandDb;
import kz.greetgo.sandbox.db.stand.model.PersonDot;
import kz.greetgo.util.RND;
import kz.greetgo.util.ServerUtil;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.*;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@Bean
public class FloraRegisterStand implements FloraRegister {

  @Override
  public List<FloraRecord> getList(FloraToFilter toFilter) {
    List<FloraRecord> list = Lists.newArrayList();

    Function<XWPFTableRow, String> testFunction = (XWPFTableRow row) -> {
      FloraRecord floraRecord = new FloraRecord();
      floraRecord.id = RND.plusInt(1000000);
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
      list.add(floraRecord);
      return null;
    };

    try {
      FileReadUtil.readDocxTable(UtilResources.class.getResourceAsStream("test1.docx"), testFunction);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return list;
  }

  @Override
  public int getCount(FloraToFilter toFilter) {
    return 500;
  }

  @Override
  public FloraRecord detail(Integer floraId) {
    FloraRecord record = new FloraRecord();
    record.id = RND.plusInt(100000);
    record.num = RND.str(14);
    record.catalog = RND.str(14);
    record.collectedBy = RND.str(14);
    record.useReason = RND.str(14);
    record.collectPlace = RND.str(14);
    record.collectCoordinate = RND.str(14);
    record.collectAltitude = RND.str(14);
    record.typeTitle = RND.str(14);
    record.familyTitle = RND.str(14);
    record.floraNum = RND.str(14);
    record.collectDate = RND.str(14);
    record.floraWeight = RND.str(14);
    record.behaviorPercent = RND.str(14);
    return record;
  }

  @Override
  public void save(FloraRecord toSave) {
    System.out.println(toSave.toString());
  }
}
