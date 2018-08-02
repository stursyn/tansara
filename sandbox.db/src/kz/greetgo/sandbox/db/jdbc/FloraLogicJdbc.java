package kz.greetgo.sandbox.db.jdbc;


import com.google.common.base.Strings;
import kz.greetgo.sandbox.controller.model.FloraToFilter;
import kz.greetgo.sandbox.db.util.sql.SQL;

abstract public class FloraLogicJdbc<ReturnType, ModelClassType> extends SqlLogicJdbc<ReturnType, ModelClassType> {
  protected FloraToFilter filter;
  protected SQL sql = new SQL();

  public FloraLogicJdbc(FloraToFilter filter) {
    this.filter = filter;
  }

  protected void where() {
    if (filter.num!=null) {
      sql.where("f.num = :num");
      sql.setValue("num", filter.num);
    }
    if (!Strings.isNullOrEmpty(filter.catalog)) {
      sql.where("lower(f.catalog) like lower('%' || :catalog || '%')");
      sql.setValue("catalog", filter.catalog);
    }
    if (!Strings.isNullOrEmpty(filter.behaviorPercent)) {
      sql.where("lower(f.behaviorPercent) like lower('%' || :behaviorPercent || '%')");
      sql.setValue("behaviorPercent", filter.behaviorPercent);
    }
    if (!Strings.isNullOrEmpty(filter.collectAltitude)) {
      sql.where("lower(f.collectAltitude) like lower('%' || :collectAltitude || '%')");
      sql.setValue("collectAltitude", filter.collectAltitude);
    }
    if (!Strings.isNullOrEmpty(filter.collectCoordinate)) {
      sql.where("lower(f.collectCoordinate) like lower('%' || :collectCoordinate || '%')");
      sql.setValue("collectCoordinate", filter.collectCoordinate);
    }
    if (filter.collectDate!=null) {
      sql.where("EXTRACT(YEAR FROM f.collectDate) = :collectDate");
      sql.setValue("collectDate", filter.collectDate);
    }
    if (!Strings.isNullOrEmpty(filter.collectedBy)) {
      sql.where("lower(f.collectedBy) like lower('%' || :collectedBy || '%')");
      sql.setValue("collectedBy", filter.collectedBy);
    }
    if (!Strings.isNullOrEmpty(filter.collectPlace)) {
      sql.where("lower(f.collectPlace) like lower('%' || :collectPlace || '%')");
      sql.setValue("collectPlace", filter.collectPlace);
    }
    if (!Strings.isNullOrEmpty(filter.family)) {
      sql.where("f.familyCode = :family");
      sql.setValue("family", filter.family);
    }
    if (!Strings.isNullOrEmpty(filter.genus)) {
      sql.where("f.genusCode = :genus");
      sql.setValue("genus", filter.genus);
    }
    if (!Strings.isNullOrEmpty(filter.region)) {
      sql.where("f.regionCode = :region");
      sql.setValue("region", filter.region);
    }
    if (!Strings.isNullOrEmpty(filter.floraWeight)) {
      sql.where("lower(f.floraWeight) like lower('%' || :floraWeight || '%')");
      sql.setValue("floraWeight", filter.floraWeight);
    }
    if (!Strings.isNullOrEmpty(filter.type)) {
      sql.where("f.typeCode = :typeCode");
      sql.setValue("typeCode", filter.type);
    }
    if (!Strings.isNullOrEmpty(filter.usage)) {
      sql.where("f.usageCode = :usage");
      sql.setValue("usage", filter.usage);
    }
    if (!Strings.isNullOrEmpty(filter.collection) && !Strings.isNullOrEmpty(filter.measure)) {
      sql.where("fct.collectionDict = :collection");
      sql.setValue("collection", filter.collection);
      sql.where("fct.measureDict = :measure");
      sql.setValue("measure", filter.measure);
    } else if (!Strings.isNullOrEmpty(filter.collection)) {
      sql.where("fct.collectionDict = :collection");
      sql.setValue("collection", filter.collection);
    } else if (!Strings.isNullOrEmpty(filter.measure)) {
      sql.where("fct.collectionDict = :measure");
      sql.setValue("measure", filter.measure);
    }
  }

  protected void from() {
    sql.from("flora f");
  }

  protected void groupBy() {
  }
}
