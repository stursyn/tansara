package kz.greetgo.sandbox.db.jdbc;

import kz.greetgo.sandbox.controller.model.FloraRecord;
import kz.greetgo.sandbox.controller.model.FloraToFilter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FloraListJdbc extends FloraLogicJdbc<List<FloraRecord>, FloraRecord> {

  public FloraListJdbc(FloraToFilter filter) {
    super(filter);
  }

  @Override
  public List<FloraRecord> doInConnection(Connection con) throws Exception {
    prepareSql();
    List<FloraRecord> ret = new ArrayList<>();
    String sqlStr = sql.compile();
    try (PreparedStatement ps = con.prepareStatement(sqlStr)) {
      try (ResultSet rs = sql.applyParameter(ps).executeQuery()) {
        while (rs.next())
          ret.add(fromRs(rs));
      }
    }
    return ret;
  }

  @Override
  protected void select() {
    sql.select("f.id, f.num, f.catalog, f.collectedBy, f.typeTitle, f.familyTitle, " +
        " f.floraNum, f.collectPlace, f.collectCoordinate, f.collectAltitude, f.collectDate, " +
        " f.floraWeight, f.behaviorPercent, f.useReason");
  }

  @Override
  protected FloraRecord fromRs(ResultSet rs) throws SQLException {
    FloraRecord r = new FloraRecord();
    r.id = rs.getBigDecimal("id").intValue();
    r.num = rs.getString("num");
    r.catalog = rs.getString("catalog");
    r.collectedBy = rs.getString("collectedBy");
    r.typeTitle = rs.getString("typeTitle");
    r.familyTitle = rs.getString("familyTitle");
    r.floraNum = rs.getString("floraNum");
    r.collectPlace = rs.getString("collectPlace");
    r.collectCoordinate = rs.getString("collectCoordinate");
    r.collectAltitude = rs.getString("collectAltitude");
    r.collectDate = rs.getString("collectDate");
    r.floraWeight = rs.getString("floraWeight");
    r.behaviorPercent = rs.getString("behaviorPercent");
    r.useReason = rs.getString("useReason");
    return r;
  }

  @Override
  protected void orderBy() {
    sql.order_by("f.id");
  }


  @Override
  protected void innerJoin() {
  }

  @Override
  protected void leftJoin(){
  }

  @Override
  protected void limit() {
    sql.limit(":limit");
    sql.setValue("limit", filter.pageSize);
  }

  @Override
  protected void offset() {
    sql.offset(":offset");
    sql.setValue("offset", filter.page * filter.pageSize);
  }
}
