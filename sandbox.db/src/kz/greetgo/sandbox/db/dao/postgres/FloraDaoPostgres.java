package kz.greetgo.sandbox.db.dao.postgres;

import kz.greetgo.depinject.core.Bean;
import kz.greetgo.sandbox.controller.model.CollectionRecord;
import kz.greetgo.sandbox.controller.model.FloraDetail;
import kz.greetgo.sandbox.controller.model.FloraRecord;
import kz.greetgo.sandbox.controller.register.model.UserParamName;
import kz.greetgo.sandbox.db.dao.AuthDao;
import kz.greetgo.sandbox.db.dao.FloraDao;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Bean
public interface FloraDaoPostgres extends FloraDao {
  @Override
  @Insert("insert into flora(num, catalog, collectedBy, familyCode, genusCode, typeCode," +
      " usageCode, regionCode, collectPlace, collectCoordinate, collectAltitude, collectDate, floraWeight," +
      " behaviorPercent) values (#{flora.num}, #{flora.catalog}, #{flora.collectedBy}, #{flora.family}, #{flora.genus}," +
      " #{flora.type}, #{flora.usage}, #{flora.region}, #{flora.collectPlace}, #{flora.collectCoordinate}, #{flora.collectAltitude}, #{flora.collectDate}," +
      " #{flora.floraWeight}, #{flora.behaviorPercent})" +
      " on conflict (num) do update set catalog = #{flora.catalog}, collectedBy = #{flora.collectedBy}, familyCode = #{flora.family},  genusCode = #{flora.genus}, " +
      " typeCode = #{flora.type}, regionCode = #{flora.region}, collectPlace = #{flora.collectPlace}, " +
      " collectCoordinate = #{flora.collectCoordinate}, collectAltitude = #{flora.collectAltitude}, collectDate = #{flora.collectDate}, floraWeight = #{flora.floraWeight}," +
      " usageCode = #{flora.usage}, behaviorPercent = #{flora.behaviorPercent}")
  void insertFlora(@Param("flora") FloraDetail floraRecord);


  @Override
  @Insert("insert into flora_collection_relation(flora, collectionDict, measureDict, actual) " +
    " values (#{floraNum}, #{collection.collection}, #{collection.measure}, 1)" +
    " on conflict (flora, collectionDict, measureDict) do update set actual = 1")
  void insertFloraCollectionRelation(@Param("floraNum") Long floraNum, @Param("collection") CollectionRecord collection);

  @Override
  @Select("select x.familyCode as family, x.genusCode as genus, x.typeCode as type, x.usageCode as usage, x.regionCode as region, x.* from flora x where x.num = #{floraId}")
  FloraDetail loadFlora(@Param("floraId") Long floraId);

  @Override
  @Select("select nextval('num')")
  Long loadFloraId();
}
