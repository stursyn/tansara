package kz.greetgo.sandbox.db.jdbc.dict;

import kz.greetgo.sandbox.controller.model.AdminDictToFilter;
import kz.greetgo.sandbox.controller.model.DictSimpleToFilter;
import kz.greetgo.sandbox.controller.model.DictType;
import kz.greetgo.sandbox.db.report.ReportView;
import kz.greetgo.sandbox.db.report.dict.DictRow;
import org.fest.util.Strings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DictReportJdbc extends DictLogicJdbc<Void, DictRow> {
  private ReportView view;

  public DictReportJdbc(AdminDictToFilter filter, ReportView view) {
    super(filter);
    this.view = view;
  }

  @Override
  public Void doInConnection(Connection con) throws Exception {
    prepareSql();
    String sqlStr = sql.compile();
    try (PreparedStatement ps = con.prepareStatement(sqlStr)) {
      try (ResultSet rs = sql.applyParameter(ps).executeQuery()) {
        while (rs.next())
          view.addRow(fromRs(rs));
      }
    }
    return null;
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
  protected DictRow fromRs(ResultSet rs) throws SQLException {
    DictRow r = new DictRow();
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
  }

  @Override
  protected void offset() {
  }
}
