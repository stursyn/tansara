package kz.greetgo.sandbox.db.dao.postgres;

import kz.greetgo.depinject.core.Bean;
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
  @Insert("insert into flora(id, num, catalog, collectedBy, typeTitle, familyTitle," +
      " floraNum, collectPlace, collectCoordinate, collectAltitude, collectDate, floraWeight," +
      " useReason, behaviorPercent) values (#{flora.id}, #{flora.num}, #{flora.catalog}, #{flora.collectedBy}, #{flora.typeTitle}," +
      " #{flora.familyTitle}, #{flora.floraNum}, #{flora.collectPlace}, #{flora.collectCoordinate}, #{flora.collectAltitude}, #{flora.collectDate}," +
      " #{flora.floraWeight}, #{flora.useReason}, #{flora.behaviorPercent})" +
      " on conflict (id) do update set num = #{flora.num}, catalog = #{flora.catalog}, collectedBy = #{flora.collectedBy}, typeTitle = #{flora.typeTitle}, familyTitle = #{flora.familyTitle}, " +
      " floraNum = #{flora.floraNum}, collectPlace = #{flora.collectPlace}, " +
      " collectCoordinate = #{flora.collectCoordinate}, collectAltitude = #{flora.collectAltitude}, collectDate = #{flora.collectDate}, floraWeight = #{flora.floraWeight}," +
      " useReason = #{flora.useReason}, behaviorPercent = #{flora.behaviorPercent}")
  void insertFlora(@Param("flora") FloraRecord floraRecord);

  @Override
  @Select("select * from flora where id = #{floraId}")
  FloraRecord loadFlora(@Param("floraId") Integer floraId);

  @Override
  @Select("select nextval('num')")
  Integer loadFloraId();
}
