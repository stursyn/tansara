package kz.greetgo.sandbox.db.jdbc.dict;


import com.google.common.base.Strings;
import kz.greetgo.sandbox.controller.model.AdminDictToFilter;
import kz.greetgo.sandbox.controller.model.FloraToFilter;
import kz.greetgo.sandbox.db.jdbc.SqlLogicJdbc;
import kz.greetgo.sandbox.db.util.sql.SQL;

abstract public class DictLogicJdbc<ReturnType, ModelClassType> extends SqlLogicJdbc<ReturnType, ModelClassType> {
  protected AdminDictToFilter filter;
  protected SQL sql = new SQL();

  public DictLogicJdbc(AdminDictToFilter filter) {
    this.filter = filter;
  }

  protected void where() {
    if (!Strings.isNullOrEmpty(filter.name)) {
      sql.where("lower(tod.title) like lower('%' || :name || '%')");
      sql.setValue("name", filter.name);
    }
    if (!Strings.isNullOrEmpty(filter.dictType)) {
      sql.where("tod.dictType = :dictType");
      sql.setValue("dictType", filter.dictType);
    }
  }

  protected void from() {
    sql.from("table_of_dicts tod");
  }

  protected void groupBy() {
  }
}
