package kz.greetgo.sandbox.db.register_impl;

import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.sandbox.controller.model.DictRecord;
import kz.greetgo.sandbox.controller.model.DictSimpleToFilter;
import kz.greetgo.sandbox.controller.model.FloraRecord;
import kz.greetgo.sandbox.controller.model.FloraToFilter;
import kz.greetgo.sandbox.controller.register.FloraRegister;
import kz.greetgo.sandbox.db.dao.FloraDao;
import kz.greetgo.sandbox.db.jdbc.FloraCountJdbc;
import kz.greetgo.sandbox.db.jdbc.FloraListJdbc;
import kz.greetgo.sandbox.db.util.JdbcSandbox;
import org.fest.util.Strings;

import java.util.List;

@Bean
public class FloraRegisterImpl implements FloraRegister {
  public BeanGetter<JdbcSandbox> jdbcSandbox;
  public BeanGetter<FloraDao> floraDao;

  @Override
  public List<FloraRecord> getList(FloraToFilter toFilter) {
    return jdbcSandbox.get().execute(new FloraListJdbc(toFilter));
  }

  @Override
  public int getCount(FloraToFilter toFilter) {
    return jdbcSandbox.get().execute(new FloraCountJdbc(toFilter));
  }

  @Override
  public FloraRecord detail(Integer floraId) {
    return floraDao.get().loadFlora(floraId);
  }

  @Override
  public void save(FloraRecord toSave) {
    if(toSave.id == null) toSave.id = floraDao.get().loadFloraId();
    floraDao.get().insertFlora(toSave);
  }

  @Override
  public List<DictRecord> dictSimple(DictSimpleToFilter toFilter) {
    if(Strings.isNullOrEmpty(toFilter.parentCode)) return floraDao.get().loadDictSimpleList(toFilter.dictType);
    return floraDao.get().loadDictSimpleListByParentCode(toFilter.dictType, toFilter.parentCode);
  }
}
