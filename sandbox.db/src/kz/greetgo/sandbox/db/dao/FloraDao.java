package kz.greetgo.sandbox.db.dao;

import kz.greetgo.sandbox.controller.model.*;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FloraDao {
  @Delete("delete from flora_collection_relation where flora=#{floraNum}")
  void deleteFloraCollectionList(@Param("floraNum") Long floraNum);

  @Delete("delete from flora where num = #{flora}")
  void deleteFlora(@Param("flora") Long flora);

  @Delete("delete from flora_usage_relation where flora=#{floraNum}")
  void deleteFloraUsageList(@Param("floraNum") Long floraNum);

  void insertFlora(FloraDetail floraRecord);

  void insertFloraCollectionRelation(Long floraNum, CollectionRecord collection);

  void insertFloraUsageRelation(Long floraNum, String usage);

  FloraDetail loadFlora(Long floraId);

  Long loadFloraId();

  @Select("select code, title from table_of_dicts where dictType=#{dictType} order by title")
  List<DictRecord> loadDictSimpleList(@Param("dictType") String dictType);

  @Select("select code, title from table_of_dicts where dictType=#{dictType} and parentCode = #{parentCode}  order by title")
  List<DictRecord> loadDictSimpleListByParentCode(@Param("dictType") String dictType, @Param("parentCode") String parentCode);

  @Select("select collectionDict as collection, measureDict as measure from flora_collection_relation where flora=#{floraId}")
  List<CollectionRecord> loadCollectionList(@Param("floraId") long floraId);

  @Select("select usageDict from flora_usage_relation where flora=#{floraId}")
  List<String> loadUsageList(@Param("floraId") long floraId);

  @Select("select num from flora order by num")
  List<String> loadFloraNumByOrder();

  @Select("select num from flora where num = #{num}")
  Long checkFloraNum(@Param("num")Long num);
}
