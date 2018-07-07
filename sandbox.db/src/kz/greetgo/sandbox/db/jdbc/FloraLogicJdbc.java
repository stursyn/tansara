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
    if (!Strings.isNullOrEmpty(filter.num)) {
      sql.where("lower(f.num) like lower('%' || :num || '%')");
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
    if (!Strings.isNullOrEmpty(filter.collectDate)) {
      sql.where("lower(f.collectDate) like lower('%' || :collectDate || '%')");
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
    if (!Strings.isNullOrEmpty(filter.familyTitle)) {
      sql.where("lower(f.familyTitle) like lower('%' || :familyTitle || '%')");
      sql.setValue("familyTitle", filter.familyTitle);
    }
    if (!Strings.isNullOrEmpty(filter.floraNum)) {
      sql.where("lower(f.floraNum) like lower('%' || :floraNum || '%')");
      sql.setValue("floraNum", filter.floraNum);
    }
    if (!Strings.isNullOrEmpty(filter.floraWeight)) {
      sql.where("lower(f.floraWeight) like lower('%' || :floraWeight || '%')");
      sql.setValue("floraWeight", filter.floraWeight);
    }
    if (!Strings.isNullOrEmpty(filter.typeTitle)) {
      sql.where("lower(f.typeTitle) like lower('%' || :typeTitle || '%')");
      sql.setValue("typeTitle", filter.typeTitle);
    }
    if (!Strings.isNullOrEmpty(filter.useReason)) {
      sql.where("lower(f.useReason) like lower('%' || :useReason || '%')");
      sql.setValue("useReason", filter.useReason);
    }
  }

  protected void from() {
    sql.from("flora f");
  }

  protected void groupBy() {
  }
}
