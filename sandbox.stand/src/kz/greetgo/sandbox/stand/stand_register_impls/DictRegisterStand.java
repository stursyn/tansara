package kz.greetgo.sandbox.stand.stand_register_impls;

import com.google.common.collect.Lists;
import kz.greetgo.depinject.core.Bean;
import kz.greetgo.mvc.interfaces.BinResponse;
import kz.greetgo.sandbox.controller.errors.RestError;
import kz.greetgo.sandbox.controller.model.*;
import kz.greetgo.sandbox.controller.register.DictRegister;
import kz.greetgo.sandbox.controller.register.FloraRegister;
import kz.greetgo.sandbox.controller.util.FileReadUtil;
import kz.greetgo.sandbox.controller.util.resources.UtilResources;
import kz.greetgo.util.RND;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@Bean
public class DictRegisterStand implements DictRegister {

  @Override
  public List<AdminDictRecord> getList(AdminDictToFilter toFilter) {
    List<AdminDictRecord> ret = Lists.newArrayList();
    ret.add(createAdminDictRecord());
    ret.add(createAdminDictRecord());
    ret.add(createAdminDictRecord());
    ret.add(createAdminDictRecord());
    ret.add(createAdminDictRecord());
    return ret;
  }

  private AdminDictRecord createAdminDictRecord() {
    AdminDictRecord ret = new AdminDictRecord();
    ret.code = RND.str(12);
    ret.parentCode = RND.str(12);
    ret.parentTitle = RND.str(12);
    ret.title = RND.str(12);
    ret.dictType = RND.str(12);
    return ret;
  }

  @Override
  public int getCount(AdminDictToFilter toFilter) {
    return 4;
  }

  @Override
  public AdminDictDetail detail(String dictId, String dictType) {
    return createAdminDictDetial();
  }

  private AdminDictDetail createAdminDictDetial() {
    AdminDictDetail ret = new AdminDictDetail();
    ret.code = RND.str(12);
    ret.title = RND.str(23);
    ret.dictType = "FAMILY";
    return ret;
  }

  @Override
  public void save(AdminDictDetail toSave) {

  }

  @Override
  public List<DictRecord> dictTypeDict() {
    List<DictRecord> ret = Lists.newArrayList();
    ret.add(new DictRecord("FAMILY", "Семейства"));
    ret.add(new DictRecord("TYPE", "Вид"));
    return ret;
  }

  @Override
  public List<DictRecord> parentDict(DictSimpleToFilter toFilter) {
    List<DictRecord> ret = Lists.newArrayList();
    ret.add(new DictRecord(RND.str(14), RND.str(14)));
    ret.add(new DictRecord(RND.str(14), RND.str(14)));
    return ret;
  }

  @Override
  public void remove(String code, String dictType) {

  }

  @Override
  public void downloadReport(AdminDictToFilter toFilter, BinResponse binResponse) {

  }

  @Override
  public String floraImage(String code, String dictType) {
    return null;
  }

}
