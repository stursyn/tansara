package kz.greetgo.sandbox.db.register_impl;

import com.google.common.collect.Lists;
import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.mvc.interfaces.BinResponse;
import kz.greetgo.sandbox.controller.model.*;
import kz.greetgo.sandbox.controller.register.DictRegister;
import kz.greetgo.sandbox.controller.register.FloraRegister;
import kz.greetgo.sandbox.controller.util.FileUtil;
import kz.greetgo.sandbox.db.dao.DictDao;
import kz.greetgo.sandbox.db.jdbc.FloraReportJdbc;
import kz.greetgo.sandbox.db.jdbc.dict.DictCountJdbc;
import kz.greetgo.sandbox.db.jdbc.dict.DictListJdbc;
import kz.greetgo.sandbox.db.jdbc.dict.DictReportJdbc;
import kz.greetgo.sandbox.db.report.ReportView;
import kz.greetgo.sandbox.db.report.dict.DictFooterData;
import kz.greetgo.sandbox.db.report.dict.DictHeaderData;
import kz.greetgo.sandbox.db.report.dict.DictViewXlsx;
import kz.greetgo.sandbox.db.report.main.MainFooterData;
import kz.greetgo.sandbox.db.report.main.MainHeaderData;
import kz.greetgo.sandbox.db.report.main.MainViewXlsx;
import kz.greetgo.sandbox.db.util.JdbcSandbox;
import org.fest.util.Strings;

import java.io.IOException;
import java.util.List;

@Bean
public class DictRegisterImpl implements DictRegister {
  public BeanGetter<JdbcSandbox> jdbcSandbox;
  public BeanGetter<DictDao> dictDao;
  public BeanGetter<FloraRegister> floraRegister;

  @Override
  public List<AdminDictRecord> getList(AdminDictToFilter toFilter) {
    return jdbcSandbox.get().execute(new DictListJdbc(toFilter));
  }

  @Override
  public int getCount(AdminDictToFilter toFilter) {
    return jdbcSandbox.get().execute(new DictCountJdbc(toFilter));
  }

  @Override
  public AdminDictDetail detail(String dictId) {
    return dictDao.get().loadDict(dictId);
  }

  @Override
  public void save(AdminDictDetail toSave) {
    dictDao.get().insertDict(toSave);
    if(toSave.fileModel!=null && !Strings.isNullOrEmpty(toSave.fileModel.src)) {
      dictDao.get().updateImage(toSave.code, toSave.fileModel.name, FileUtil.base64ToBytes(toSave.fileModel.src), toSave.description);
    } else {
      dictDao.get().updateImage(toSave.code, null, new byte[0], toSave.description);
    }
  }

  @Override
  public List<DictRecord> dictTypeDict() {
    return DictType.listWithTitle();
  }

  @Override
  public List<DictRecord> parentDict(DictSimpleToFilter toFilter) {
    if(!Strings.isNullOrEmpty(toFilter.dictType) && DictType.isHasParent(toFilter.dictType)){
      toFilter.dictType = DictType.valueOf(toFilter.dictType).parent.name();
      return floraRegister.get().dictSimple(toFilter);
    }
    return Lists.newArrayList();
  }

  @Override
  public void downloadReport(AdminDictToFilter toFilter, BinResponse binResponse) {
    try(ReportView view = new DictViewXlsx(binResponse.out())){
      DictHeaderData headerData = new DictHeaderData();
      view.start(headerData);

      jdbcSandbox.get().execute(new DictReportJdbc(toFilter, view));

      view.finish(new DictFooterData());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void remove(String code) {
    dictDao.get().deleteDict(code);
  }
}
