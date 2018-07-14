package kz.greetgo.sandbox.db.jdbc;

import kz.greetgo.sandbox.controller.model.FloraRecord;
import kz.greetgo.sandbox.controller.model.FloraToFilter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
    sql.select("f.num");
    sql.select("f.catalog");
    sql.select("fd.title as familyTitle");
    sql.select("gd.title as genusTitle");
    sql.select("td.title as typeTitle");
    sql.select("f.collectDate");
  }

  @Override
  protected FloraRecord fromRs(ResultSet rs) throws SQLException {
    FloraRecord r = new FloraRecord();
    r.num = rs.getLong("num");
    r.catalog = rs.getString("catalog");
    r.familyTitle = rs.getString("familyTitle");
    r.genusTitle = rs.getString("genusTitle");
    r.typeTitle = rs.getString("typeTitle");
    if(rs.getDate("collectDate")!=null)
      r.collectDate = new SimpleDateFormat("dd.MM.yyyy").format(rs.getDate("collectDate"));
    return r;
  }

  @Override
  protected void orderBy() {
    sql.order_by("f.num");
  }


  @Override
  protected void innerJoin() {
  }

  @Override
  protected void leftJoin(){
    sql.leftjoin("table_of_dicts fd on fd.code = f.familyCode");
    sql.leftjoin("table_of_dicts gd on gd.code = f.genusCode");
    sql.leftjoin("table_of_dicts td on td.code = f.typeCode");
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
