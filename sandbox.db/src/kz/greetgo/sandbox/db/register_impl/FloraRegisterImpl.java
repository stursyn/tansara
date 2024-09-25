package kz.greetgo.sandbox.db.register_impl;

import com.google.common.collect.Lists;
import kz.greetgo.db.ConnectionCallback;
import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.mvc.interfaces.BinResponse;
import kz.greetgo.sandbox.controller.errors.RestError;
import kz.greetgo.sandbox.controller.model.*;
import kz.greetgo.sandbox.controller.register.FloraRegister;
import kz.greetgo.sandbox.controller.util.FileUtil;
import kz.greetgo.sandbox.db.dao.FloraDao;
import kz.greetgo.sandbox.db.jdbc.FloraCountJdbc;
import kz.greetgo.sandbox.db.jdbc.FloraListJdbc;
import kz.greetgo.sandbox.db.jdbc.FloraReportJdbc;
import kz.greetgo.sandbox.db.report.ReportView;
import kz.greetgo.sandbox.db.report.main.MainFooterData;
import kz.greetgo.sandbox.db.report.main.MainHeaderData;
import kz.greetgo.sandbox.db.report.main.MainViewXlsx;
import kz.greetgo.sandbox.db.util.JdbcSandbox;
import org.apache.poi.xwpf.usermodel.*;
import org.fest.util.Strings;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Bean
public class FloraRegisterImpl implements FloraRegister {
  public BeanGetter<JdbcSandbox> jdbcSandbox;
  public BeanGetter<FloraDao> floraDao;

  @Override
  public List<FloraRecord> getList(FloraToFilter toFilter) {
    return jdbcSandbox.get().execute(new FloraListJdbc(toFilter));
  }

  @Override
  public int getCount(FloraToFilter toFilter) {
    return jdbcSandbox.get().execute(new FloraCountJdbc(toFilter));
  }

  @Override
  public FloraDetail detail(Long floraId) {
    FloraDetail floraDetail = floraDao.get().loadFlora(floraId);
    floraDetail.collectionList = floraDao.get().loadCollectionList(floraId);
    floraDetail.usageList = floraDao.get().loadUsageList(floraId);
    floraDetail.collectedByList = floraDao.get().loadCollectedByList(floraId);
    return floraDetail;
  }

  @Override
  public void save(FloraDetail toSave) {
    if(toSave.num == null) toSave.num = floraDao.get().loadFloraId();

    if(!toSave.edit) {
      if(floraDao.get().checkFloraNum(toSave.num)!=null) new RestError("Нумерация уже существует");
    }

    floraDao.get().insertFlora(toSave);
    floraDao.get().deleteFloraCollectionList(toSave.num);
    toSave.collectionList.forEach( collection->{
      floraDao.get().insertFloraCollectionRelation(toSave.num, collection);
    });

    floraDao.get().deleteFloraUsageList(toSave.num);
    toSave.usageList.forEach( usage->{
      if (Strings.isNullOrEmpty(usage)) return;
      floraDao.get().insertFloraUsageRelation(toSave.num, usage);
    });

    floraDao.get().deleteFloraCollectedByList(toSave.num);
    toSave.collectedByList.forEach(collectBy->{
      if (Strings.isNullOrEmpty(collectBy)) return;
      floraDao.get().insertFloraCollectedByRelation(toSave.num, collectBy);
    });
  }

  @Override
  public List<DictRecord> dictSimple(DictSimpleToFilter toFilter) {
    if(Strings.isNullOrEmpty(toFilter.parentCode)) return floraDao.get().loadDictSimpleList(toFilter.dictType);
    return floraDao.get().loadDictSimpleListByParentCode(toFilter.dictType, toFilter.parentCode);
  }

  @Override
  public List<EmptyNumsRecord> emptyNums() {
    List<EmptyNumsRecord> ret = Lists.newArrayList();
    List<String> longs = floraDao.get().loadFloraNumByOrder();
    if(!longs.isEmpty()) {
      for (long i = 1; i < Long.parseLong(longs.get(longs.size() - 1)); i++) {
        if (longs.contains(i + "")) continue;
        ret.add(new EmptyNumsRecord(i));
      }
      ret.add(new EmptyNumsRecord(Long.parseLong(longs.get(longs.size() - 1)) + 1));
    }
    return ret;
  }

  @Override
  public void remove(String floraId) {
    floraDao.get().deleteFlora(Long.parseLong(floraId));
  }

  @Override
  public void downloadReport(FloraToFilter toFilter, BinResponse binResponse) {
    try(ReportView view = new MainViewXlsx(binResponse.out())){
      MainHeaderData headerData = new MainHeaderData();
      view.start(headerData);

      jdbcSandbox.get().execute(new FloraReportJdbc(toFilter, view));

      view.finish(new MainFooterData());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String importFloraData(FileModel fileModel) {
    StringBuilder ret = new StringBuilder();

    try {
      XWPFDocument document =
          new XWPFDocument(new ByteArrayInputStream(FileUtil.bytesToBase64(fileModel.src)));
      AtomicBoolean findTable = new AtomicBoolean(false);
      document.getBodyElements().forEach(iBodyElement -> {
        if(iBodyElement instanceof XWPFTable) {
          workWithTable(ret, (XWPFTable) iBodyElement);
          findTable.set(true);
        }
      });

      if(!findTable.get()) ret.append("Не может найти таблицу в файле");
      if(!Strings.isNullOrEmpty(ret.toString())) throw new RestError(ret.toString());
      return Strings.isNullOrEmpty(ret.toString())?null:ret.toString();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void checkInnerElements(IBodyElement iBodyElement, StringBuilder ret) {
    if(iBodyElement instanceof XWPFParagraph) {
      iBodyElement.getBody().getBodyElements().forEach(innerIBodyElement -> {
        checkInnerElements(innerIBodyElement, ret);
      });
    } else if(iBodyElement instanceof XWPFTable) {
      workWithTable(ret, (XWPFTable) iBodyElement);
    }
  }

  private void workWithTable(StringBuilder ret, XWPFTable iBodyElement) {
    XWPFTable table = iBodyElement;
    jdbcSandbox.get().execute(new ConnectionCallback<Void>() {

      @Override
      public Void doInConnection(Connection connection) throws Exception {
        try(PreparedStatement ps = connection.prepareStatement("update flora set collectPlace=?," +
            "collectCoordinate=? where num=?")) {
          AtomicBoolean header = new AtomicBoolean(true);
          table.getRows().forEach(xwpfTableRow -> {
            if(header.getAndSet(false)) return;

            List<XWPFTableCell> tableCells = xwpfTableRow.getTableCells();
            if (tableCells.size() != 4) ret.append("строка:" + tableCells.get(0).getText());
            String idStr = tableCells.get(0).getText().trim();
            Integer id = null;
            if(idStr.indexOf(".")!=-1) {
              try {
                id = Integer.parseInt(tableCells.get(0).getText().trim().split("\\.")[0]);
              } catch (Exception e){
                ret.append("строка:" + tableCells.get(0).getText());
              }
            } else {
              try {
                id = Integer.parseInt(tableCells.get(0).getText().trim().split("\\.")[0]);
              } catch (Exception e){
                ret.append("строка:" + tableCells.get(0).getText());
              }
            }
            if(id == null) return;
            try {
              ps.setString(1, tableCells.get(2).getText().trim());
              ps.setString(2, tableCells.get(3).getText().trim());
              ps.setInt(3, id);
              ps.addBatch();
            } catch (SQLException e) {
              throw new RuntimeException(e);
            }

          });
          ps.executeBatch();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
        return null;
      }
    });
  }
}
