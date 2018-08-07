package kz.greetgo.sandbox.db.jdbc.dict;

import kz.greetgo.sandbox.controller.model.*;
import org.fest.util.Strings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DictListJdbc extends DictLogicJdbc<List<AdminDictRecord>, AdminDictRecord> {

  public DictListJdbc(AdminDictToFilter filter) {
    super(filter);
  }

  @Override
  public List<AdminDictRecord> doInConnection(Connection con) throws Exception {
    prepareSql();
    List<AdminDictRecord> ret = new ArrayList<>();
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
    sql.select("tod.code");
    sql.select("tod.title");
    sql.select("ptod.code as parentCode");
    sql.select("ptod.title as parentTitle");
    sql.select("tod.dictType");
  }

  @Override
  protected AdminDictRecord fromRs(ResultSet rs) throws SQLException {
    AdminDictRecord r = new AdminDictRecord();
    r.code = rs.getString("code");
    r.title = rs.getString("title");
    r.parentCode = rs.getString("parentCode");
    r.parentTitle = rs.getString("parentTitle");
    String dictType = rs.getString("dictType");
    if(!Strings.isNullOrEmpty(dictType)) {
      r.dictType = DictType.valueOf(dictType).title;
    }
    return r;
  }

  @Override
  protected void orderBy() {
    sql.order_by("tod.dictType");
  }

  @Override
  protected void groupBy() {
  }


  @Override
  protected void innerJoin() {
  }

  @Override
  protected void leftJoin(){
    sql.leftjoin("table_of_dicts ptod on tod.parentCode = ptod.code");
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
