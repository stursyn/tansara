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
import kz.greetgo.sandbox.db.jdbc.dict.DictCountJdbc;
import kz.greetgo.sandbox.db.jdbc.dict.DictListJdbc;
import kz.greetgo.sandbox.db.jdbc.dict.DictReportJdbc;
import kz.greetgo.sandbox.db.report.ReportView;
import kz.greetgo.sandbox.db.report.dict.DictFooterData;
import kz.greetgo.sandbox.db.report.dict.DictHeaderData;
import kz.greetgo.sandbox.db.report.dict.DictViewXlsx;
import kz.greetgo.sandbox.db.util.JdbcSandbox;
import org.fest.util.Strings;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
  public AdminDictDetail detail(String dictId, String dictType) {
    return dictDao.get().loadDict(dictId, dictType);
  }

  @Override
  public void save(AdminDictDetail toSave) {
    dictDao.get().insertDict(toSave);
    if (toSave.fileModel != null && !Strings.isNullOrEmpty(toSave.fileModel.src)) {
      dictDao.get().updateImage(toSave.code, toSave.dictType, toSave.fileModel.name, FileUtil.bytesToBase64(toSave.fileModel.src), toSave.description);
    } else {
      dictDao.get().updateImage(toSave.code, toSave.dictType, null, new byte[0], toSave.description);
    }
  }

  @Override
  public List<DictRecord> dictTypeDict() {
    return DictType.listWithTitle();
  }

  @Override
  public List<DictRecord> parentDict(DictSimpleToFilter toFilter) {
    if (!Strings.isNullOrEmpty(toFilter.dictType) && DictType.isHasParent(toFilter.dictType)) {
      toFilter.dictType = DictType.valueOf(toFilter.dictType).parent.name();
      return floraRegister.get().dictSimple(toFilter);
    }
    return Lists.newArrayList();
  }

  @Override
  public void downloadReport(AdminDictToFilter toFilter, BinResponse binResponse) {
    try (ReportView view = new DictViewXlsx(binResponse.out())) {
      DictHeaderData headerData = new DictHeaderData();
      view.start(headerData);

      jdbcSandbox.get().execute(new DictReportJdbc(toFilter, view));

      view.finish(new DictFooterData());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String floraImage(String code, String dictType) {
    return jdbcSandbox.get().execute(con -> {
      try (PreparedStatement ps = con.prepareStatement("select image from table_of_dicts where code = ? and dictType = ? and image is not null")) {
        ps.setString(1, code);
        ps.setString(2, dictType);
        try (ResultSet resultSet = ps.executeQuery()) {
          while (resultSet.next()) {
            return "data:image/jpeg;base64, " + FileUtil.bytesToBase64(resultSet.getBytes("image"));
          }
        }
      }
      return null;
    });
  }

  @Override
  public void remove(String code, String dictType) {
    dictDao.get().deleteDict(code, dictType);
  }
}
