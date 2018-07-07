package kz.greetgo.sandbox.db.register_impl;

import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.sandbox.controller.errors.IllegalLoginOrPassword;
import kz.greetgo.sandbox.controller.errors.NoAccountName;
import kz.greetgo.sandbox.controller.errors.NoPassword;
import kz.greetgo.sandbox.controller.errors.NotFound;
import kz.greetgo.sandbox.controller.model.AuthInfo;
import kz.greetgo.sandbox.controller.model.FloraRecord;
import kz.greetgo.sandbox.controller.model.UserInfo;
import kz.greetgo.sandbox.controller.register.AuthRegister;
import kz.greetgo.sandbox.controller.register.FloraRegister;
import kz.greetgo.sandbox.controller.register.model.SessionInfo;
import kz.greetgo.sandbox.controller.register.model.UserParamName;
import kz.greetgo.sandbox.controller.security.SecurityError;
import kz.greetgo.sandbox.db.dao.FloraDao;
import kz.greetgo.sandbox.db.test.dao.AuthTestDao;
import kz.greetgo.sandbox.db.test.util.ParentTestNg;
import kz.greetgo.util.RND;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class FloraRegisterImplTest extends ParentTestNg {

  public BeanGetter<FloraRegister> floraRegister;
  public BeanGetter<FloraDao> floraDao;

  @Test
  public void detail() throws Exception {
    FloraRecord floraRecord = initFlora();
    floraDao.get().insertFlora(floraRecord);
    FloraRecord detail = floraRegister.get().detail(floraRecord.id);

    assertThat(detail.id).isEqualTo(floraRecord.id);
    assertThat(detail.num).isEqualTo(floraRecord.num);
    assertThat(detail.catalog).isEqualTo(floraRecord.catalog);
    assertThat(detail.collectedBy).isEqualTo(floraRecord.collectedBy);
    assertThat(detail.typeTitle).isEqualTo(floraRecord.typeTitle);
    assertThat(detail.familyTitle).isEqualTo(floraRecord.familyTitle);
    assertThat(detail.useReason).isEqualTo(floraRecord.useReason);
    assertThat(detail.floraNum).isEqualTo(floraRecord.floraNum);
    assertThat(detail.collectPlace).isEqualTo(floraRecord.collectPlace);
    assertThat(detail.collectCoordinate).isEqualTo(floraRecord.collectCoordinate);
    assertThat(detail.collectAltitude).isEqualTo(floraRecord.collectAltitude);
    assertThat(detail.collectDate).isEqualTo(floraRecord.collectDate);
    assertThat(detail.floraWeight).isEqualTo(floraRecord.floraWeight);
    assertThat(detail.behaviorPercent).isEqualTo(floraRecord.behaviorPercent);
  }

  private FloraRecord initFlora() {
    FloraRecord ret = new FloraRecord();
    ret.id = floraDao.get().loadFloraId();
    ret.num = RND.str(15);
    ret.catalog = RND.str(15);
    ret.collectedBy = RND.str(15);
    ret.typeTitle = RND.str(15);
    ret.familyTitle = RND.str(15);
    ret.useReason = RND.str(15);
    ret.floraNum = RND.str(15);
    ret.collectPlace = RND.str(15);
    ret.collectCoordinate = RND.str(15);
    ret.collectAltitude = RND.str(15);
    ret.collectDate = RND.str(15);
    ret.floraWeight = RND.str(15);
    ret.behaviorPercent = RND.str(15);
    return ret;
  }

}
