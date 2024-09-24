package kz.greetgo.sandbox.db.jdbc;

import com.google.common.base.Strings;
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
    sql.select("string_agg(distinct cd.title||' '||md.title,', ') as collectionTitle");
    sql.select("f.catalog");
    sql.select("fd.title as familyTitle");
    sql.select("td.title as typeTitle");
    sql.select("f.collectDate");
  }

  @Override
  protected FloraRecord fromRs(ResultSet rs) throws SQLException {
    FloraRecord r = new FloraRecord();
    r.num = rs.getLong("num");
    r.catalog = rs.getString("catalog");
    r.collectionTitle = rs.getString("collectionTitle");
    r.familyTitle = rs.getString("familyTitle");
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
  protected void groupBy() {
    sql.group_by("f.num");
    sql.group_by("f.catalog");
    sql.group_by("fd.title");
    sql.group_by("td.title");
    sql.group_by("f.collectDate");
  }


  @Override
  protected void innerJoin() {
  }

  @Override
  protected void leftJoin(){
    sql.leftjoin("table_of_dicts fd on fd.code = f.familyCode and fd.dictType = 'FAMILY'");
    sql.leftjoin("table_of_dicts gd on gd.code = f.genusCode and gd.dictType = 'GENUS'");
    sql.leftjoin("table_of_dicts td on td.code = f.typeCode and td.dictType = 'TYPE'");
    sql.leftjoin("flora_collection_relation fct on fct.flora = f.num");
    sql.leftjoin("table_of_dicts cd on cd.code = fct.collectionDict and cd.dictType = 'COLLECTION'");
    sql.leftjoin("table_of_dicts md on md.code = fct.measureDict and md.dictType = 'MEASURE'");
    if (!Strings.isNullOrEmpty(filter.usage)) {
      sql.leftjoin("flora_usage_relation fur on fur.flora = f.num");
    }
    if (!Strings.isNullOrEmpty(filter.collectedBy)) {
      sql.leftjoin("flora_collected_by_relation fcbr on fcbr.flora = f.num");
    }
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
