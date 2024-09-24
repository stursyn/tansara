package kz.greetgo.sandbox.db.dao.postgres;

import kz.greetgo.depinject.core.Bean;
import kz.greetgo.sandbox.controller.model.AdminDictDetail;
import kz.greetgo.sandbox.controller.model.CollectionRecord;
import kz.greetgo.sandbox.controller.model.FloraDetail;
import kz.greetgo.sandbox.db.dao.DictDao;
import kz.greetgo.sandbox.db.dao.FloraDao;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Bean
public interface DictDaoPostgres extends DictDao {
  @Override
  @Insert("insert into table_of_dicts(code, title, dictType, parentCode) values (#{dict.code}, #{dict.title}, #{dict.dictType}, #{dict.parentCode})" +
      " on conflict (code, dictType) do update set title = #{dict.title}, parentCode = #{dict.parentCode}")
  void insertDict(@Param("dict") AdminDictDetail floraRecord);


  @Override
  @Select("select x.code, x.title, x.parentCode, x.dictType from table_of_dicts x where x.code = #{dictId}")
  AdminDictDetail loadDict(@Param("dictId") String dictId);
}
