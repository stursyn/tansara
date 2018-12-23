package kz.greetgo.sandbox.db.register_impl;

import com.google.common.collect.Lists;
import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.sandbox.controller.errors.RestError;
import kz.greetgo.sandbox.controller.model.*;
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
  public FloraDetail detail(Long floraId) {
    FloraDetail floraDetail = floraDao.get().loadFlora(floraId);
    floraDetail.collectionList = floraDao.get().loadCollectionList(floraId);
    return floraDetail;
  }

  @Override
  public void save(FloraDetail toSave) {
    if(toSave.num == null) toSave.num = floraDao.get().loadFloraId();

    if(!toSave.edit) {
      if(floraDao.get().checkFloraNum(toSave.num)!=null) new RestError("Нумерация уже существует");
    }

    floraDao.get().insertFlora(toSave);
    floraDao.get().deleteFloraCollectionList(toSave.num);
    toSave.collectionList.forEach( collection->{
      floraDao.get().insertFloraCollectionRelation(toSave.num, collection);
    });
  }

  @Override
  public List<DictRecord> dictSimple(DictSimpleToFilter toFilter) {
    if(Strings.isNullOrEmpty(toFilter.parentCode)) return floraDao.get().loadDictSimpleList(toFilter.dictType);
    return floraDao.get().loadDictSimpleListByParentCode(toFilter.dictType, toFilter.parentCode);
  }

  @Override
  public List<EmptyNumsRecord> emptyNums() {
    List<EmptyNumsRecord> ret = Lists.newArrayList();
    List<String> longs = floraDao.get().loadFloraNumByOrder();
    if(!longs.isEmpty()) {
      for (long i = 1; i < Long.parseLong(longs.get(longs.size() - 1)); i++) {
        if (longs.contains(i + "")) continue;
        ret.add(new EmptyNumsRecord(i));
      }
      ret.add(new EmptyNumsRecord(Long.parseLong(longs.get(longs.size() - 1)) + 1));
    }
    return ret;
  }

  @Override
  public void remove(String floraId) {
    floraDao.get().deleteFlora(Long.parseLong(floraId));
  }
}
