package kz.greetgo.sandbox.stand.stand_register_impls;

import com.google.common.collect.Lists;
import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.sandbox.controller.errors.AuthError;
import kz.greetgo.sandbox.controller.errors.RestError;
import kz.greetgo.sandbox.controller.model.*;
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
      floraRecord.num = RND.plusLong(1000000);
      AtomicInteger cellCount = new AtomicInteger(0);
      row.getTableCells().forEach( xwpfTableCell -> {
        switch (cellCount.getAndIncrement()){
          case 2:
            floraRecord.catalog = xwpfTableCell.getText();
            break;
          case 3:
            floraRecord.typeTitle = xwpfTableCell.getText();
            break;
          case 4:
            floraRecord.familyTitle = xwpfTableCell.getText();
            break;
          case 9:
            floraRecord.collectDate = xwpfTableCell.getText();
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
  public FloraDetail detail(Long floraId) {
    FloraDetail record = new FloraDetail();
    record.catalog = RND.str(14);
    record.collectedBy = RND.str(14);
    record.collectPlace = RND.str(14);
    record.collectCoordinate = RND.str(14);
    record.collectAltitude = RND.str(14);
    record.floraWeight = RND.str(14);
    record.behaviorPercent = RND.str(14);
    return record;
  }

  @Override
  public void save(FloraDetail toSave) {
    throw new RestError("Net asdasd");
  }

  @Override
  public List<DictRecord> dictSimple(DictSimpleToFilter toFilter) {
    List<DictRecord> ret = Lists.newArrayList();
    ret.add(new DictRecord(RND.str(15), RND.str(15)));
    ret.add(new DictRecord(RND.str(15), RND.str(15)));
    ret.add(new DictRecord(RND.str(15), RND.str(15)));
    ret.add(new DictRecord(RND.str(15), RND.str(15)));
    ret.add(new DictRecord(RND.str(15), RND.str(15)));
    return ret;
  }

  @Override
  public List<EmptyNumsRecord> emptyNums() {
    List<EmptyNumsRecord> ret = Lists.newArrayList();
    ret.add(new EmptyNumsRecord(RND.plusLong(1500)));
    ret.add(new EmptyNumsRecord(RND.plusLong(1500)));
    ret.add(new EmptyNumsRecord(RND.plusLong(1500)));
    ret.add(new EmptyNumsRecord(RND.plusLong(1500)));
    return ret;
  }
}
