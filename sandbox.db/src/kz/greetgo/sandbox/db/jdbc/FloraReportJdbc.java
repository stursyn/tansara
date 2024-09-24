package kz.greetgo.sandbox.db.jdbc;

import kz.greetgo.sandbox.controller.model.FloraToFilter;
import kz.greetgo.sandbox.db.report.ReportView;
import kz.greetgo.sandbox.db.report.main.MainRow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class FloraReportJdbc extends FloraLogicJdbc<Void, MainRow> {
  private ReportView view;

  public FloraReportJdbc(FloraToFilter toFilter, ReportView view) {
    super(toFilter);
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
    sql.select("f.num");
    sql.select("string_agg(distinct cd.title||' '||md.title,', ') as collection");
    sql.select("f.catalog");
    sql.select("fd.title as familyName");
    sql.select("gd.title as generateName");
    sql.select("td.title as typeName");
    sql.select("rd.title as floraRegionNumber");
    sql.select("f.collectPlace");
    sql.select("f.collectCoordinate as collectCoordinate");
    sql.select("string_agg(distinct ud.title,', ') as importantStaff");
    sql.select("f.collectAltitude as heightFromWater");
    sql.select("f.collectDate");
    sql.select("f.behaviorPercent as seedWeight");
    sql.select("f.floraWeight as accuracyRate");
    sql.select("string_agg(distinct cb.title,', ') as whoIsCollect");
    sql.select("lf.title as lifeForm");
    sql.select("td.description as familyDescription");
    sql.select("td.image as familyImage");
  }

  @Override
  protected MainRow fromRs(ResultSet rs) throws SQLException {
    MainRow r = new MainRow();

    r.num = rs.getLong("num");
    r.catalog = rs.getString("catalog");
    r.collection = rs.getString("collection");
    r.familyName = rs.getString("familyName");
    r.generateName = rs.getString("generateName");
    r.typeName = rs.getString("typeName");
    r.floraRegionNumber = rs.getString("floraRegionNumber");
    r.collectPlace = rs.getString("collectPlace");
    r.collectCoordinate = rs.getString("collectCoordinate");
    r.heightFromWater = rs.getString("heightFromWater");
    r.importantStaff = rs.getString("importantStaff");
    if(rs.getDate("collectDate")!=null)
      r.collectYear = new SimpleDateFormat("dd.MM.yyyy").format(rs.getDate("collectDate"));
    r.seedWeight = rs.getString("seedWeight");
    r.accuracyRate = rs.getString("accuracyRate");
    r.whoIsCollect = rs.getString("whoIsCollect");
    r.description = rs.getString("familyDescription");
    r.lifeForm = rs.getString("lifeForm");
    if(rs.getBytes("familyImage")==null){
      r.hasImage = false;
    } else {
      r.hasImage = true;
    }
    r.image = rs.getBytes("familyImage");

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
    sql.group_by("gd.title");
    sql.group_by("td.title");
    sql.group_by("rd.title");
    sql.group_by("f.collectPlace");
    sql.group_by("f.collectCoordinate");
//    sql.group_by("ud.title");
    sql.group_by("f.collectAltitude");
    sql.group_by("f.collectDate");
    sql.group_by("f.behaviorPercent");
    sql.group_by("f.floraWeight");
//    sql.group_by("f.collectedBy");
    sql.group_by("lf.title");
    sql.group_by("td.description");
    sql.group_by("td.image");
  }

  @Override
  protected void innerJoin() {
  }

  @Override
  protected void leftJoin(){
    sql.leftjoin("table_of_dicts fd on fd.code = f.familyCode and fd.dictType = 'FAMILY'");
    sql.leftjoin("table_of_dicts gd on gd.code = f.genusCode and gd.dictType = 'GENUS'");
    sql.leftjoin("table_of_dicts td on td.code = f.typeCode and td.dictType = 'TYPE'");
    sql.leftjoin("table_of_dicts rd on rd.code = f.regionCode and rd.dictType = 'REGION'");
    sql.leftjoin("flora_collection_relation fct on fct.flora = f.num");
    sql.leftjoin("table_of_dicts cd on cd.code = fct.collectionDict and cd.dictType = 'COLLECTION'");
    sql.leftjoin("table_of_dicts md on md.code = fct.measureDict and md.dictType = 'MEASURE'");
    sql.leftjoin("table_of_dicts lf on lf.code = f.lifeFormCode and lf.dictType = 'LIFE_FORM'");
    sql.leftjoin("flora_usage_relation fur on fur.flora = f.num");
    sql.leftjoin("table_of_dicts ud on ud.code = fur.usageDict and ud.dictType = 'USAGE'");
    sql.leftjoin("flora_collected_by_relation fcbr on fcbr.flora = f.num");
    sql.leftjoin("table_of_dicts cb on cb.code = fcbr.collectedByDict and cb.dictType = 'COLLECTED_BY'");
  }

  @Override
  protected void limit() {
  }

  @Override
  protected void offset() {
  }
}
