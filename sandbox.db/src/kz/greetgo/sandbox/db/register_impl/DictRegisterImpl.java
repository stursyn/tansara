package kz.greetgo.sandbox.db.register_impl;

import com.google.common.collect.Lists;
import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.sandbox.controller.errors.RestError;
import kz.greetgo.sandbox.controller.model.*;
import kz.greetgo.sandbox.controller.register.DictRegister;
import kz.greetgo.sandbox.controller.register.FloraRegister;
import kz.greetgo.sandbox.db.dao.DictDao;
import kz.greetgo.sandbox.db.dao.FloraDao;
import kz.greetgo.sandbox.db.jdbc.FloraCountJdbc;
import kz.greetgo.sandbox.db.jdbc.FloraListJdbc;
import kz.greetgo.sandbox.db.jdbc.dict.DictCountJdbc;
import kz.greetgo.sandbox.db.jdbc.dict.DictListJdbc;
import kz.greetgo.sandbox.db.util.JdbcSandbox;
import org.fest.util.Strings;

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
}
