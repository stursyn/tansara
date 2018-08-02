package kz.greetgo.sandbox.db.jdbc;


import com.google.common.base.Strings;
import kz.greetgo.sandbox.controller.model.FloraToFilter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FloraCountJdbc extends FloraLogicJdbc<Integer, Integer> {

  public FloraCountJdbc(FloraToFilter filter) {
    super(filter);
  }

  @Override
  public Integer doInConnection(Connection con) throws Exception {
    prepareSql();
    try (PreparedStatement ps = con.prepareStatement(sql.compile())) {
      try (ResultSet rs = sql.applyParameter(ps).executeQuery()) {
        rs.next();
        return fromRs(rs);
      }
    }
  }

  @Override
  protected void select() {
    sql.select("count(1)");
  }

  protected Integer fromRs(ResultSet rs) throws SQLException {
    return rs.getInt(1);
  }

  @Override
  protected void innerJoin() {

  }

  @Override
  protected void leftJoin() {
    if (!Strings.isNullOrEmpty(filter.collection) || !Strings.isNullOrEmpty(filter.measure)) {
      sql.leftjoin("flora_collection_relation fct on fct.flora = f.num");
    }
  }

  @Override
  protected void orderBy() {}

  @Override
  protected void limit() {}

  @Override
  protected void offset() {}
}
