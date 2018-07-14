package kz.greetgo.sandbox.db.dao;

import kz.greetgo.sandbox.controller.model.DictRecord;
import kz.greetgo.sandbox.controller.model.FloraRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FloraDao {
  void insertFlora(FloraRecord floraRecord);

  FloraRecord loadFlora(Integer floraId);

  Integer loadFloraId();

  @Select("select code, title from table_of_dict where dictType=#{dictType} ")
  List<DictRecord> loadDictSimpleList(@Param("dictType") String dictType);

  @Select("select code, title from table_of_dict where dictType=#{dictType} and parentCode = #{parentCode}")
  List<DictRecord> loadDictSimpleListByParentCode(@Param("dictType") String dictType, @Param("parentCode") String parentCode);
}
