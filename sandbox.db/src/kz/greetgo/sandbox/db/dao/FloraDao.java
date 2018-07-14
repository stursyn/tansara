package kz.greetgo.sandbox.db.dao;

import kz.greetgo.sandbox.controller.model.CollectionRecord;
import kz.greetgo.sandbox.controller.model.DictRecord;
import kz.greetgo.sandbox.controller.model.FloraDetail;
import kz.greetgo.sandbox.controller.model.FloraRecord;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FloraDao {
  @Delete("delete from flora_collection_relation where flora=#{floraNum}")
  void deleteFloraCollectionList(@Param("floraNum") Long floraNum);

  void insertFlora(FloraDetail floraRecord);

  void insertFloraCollectionRelation(Long floraNum, CollectionRecord collection);

  FloraDetail loadFlora(Long floraId);

  Long loadFloraId();

  @Select("select code, title from table_of_dicts where dictType=#{dictType} ")
  List<DictRecord> loadDictSimpleList(@Param("dictType") String dictType);

  @Select("select code, title from table_of_dicts where dictType=#{dictType} and parentCode = #{parentCode}")
  List<DictRecord> loadDictSimpleListByParentCode(@Param("dictType") String dictType, @Param("parentCode") String parentCode);

  @Select("select collectionDict as collection, measureDict as measure from flora_collection_relation where flora=#{floraId}")
  List<CollectionRecord> loadCollectionList(@Param("floraId") long floraId);
}
