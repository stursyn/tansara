package kz.greetgo.sandbox.db.register_impl;

import com.google.common.collect.Sets;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.sandbox.controller.errors.IllegalLoginOrPassword;
import kz.greetgo.sandbox.controller.errors.NoAccountName;
import kz.greetgo.sandbox.controller.errors.NoPassword;
import kz.greetgo.sandbox.controller.errors.NotFound;
import kz.greetgo.sandbox.controller.model.*;
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

import java.util.*;

import static org.fest.assertions.api.Assertions.assertThat;

public class FloraRegisterImplTest extends ParentTestNg {

  public BeanGetter<FloraRegister> floraRegister;
  public BeanGetter<FloraDao> floraDao;

  @Test
  public void detail() throws Exception {
    FloraDetail floraRecord = initFlora();
    floraDao.get().insertFlora(floraRecord);

    Set<String> collectionSet = Sets.newHashSet();
    Set<String> measureSet = Sets.newHashSet();
    floraRecord.collectionList.forEach(collectionRecord -> {
      floraDao.get().insertFloraCollectionRelation(floraRecord.num, collectionRecord);
      collectionSet.add(collectionRecord.collection);
      measureSet.add(collectionRecord.measure);
    });

    FloraDetail detail = floraRegister.get().detail(floraRecord.num);

    assertThat(detail.num).isEqualTo(floraRecord.num);
    assertThat(detail.catalog).isEqualTo(floraRecord.catalog);
    assertThat(detail.collectedBy).isEqualTo(floraRecord.collectedBy);
    assertThat(detail.family).isEqualTo(floraRecord.family);
    assertThat(detail.genus).isEqualTo(floraRecord.genus);
    assertThat(detail.type).isEqualTo(floraRecord.type);
    assertThat(detail.usage).isEqualTo(floraRecord.usage);
    assertThat(detail.region).isEqualTo(floraRecord.region);
    assertThat(detail.collectPlace).isEqualTo(floraRecord.collectPlace);
    assertThat(detail.collectCoordinate).isEqualTo(floraRecord.collectCoordinate);
    assertThat(detail.collectAltitude).isEqualTo(floraRecord.collectAltitude);
    assertThat(detail.collectDate).isEqualTo(floraRecord.collectDate);
    assertThat(detail.floraWeight).isEqualTo(floraRecord.floraWeight);
    assertThat(detail.behaviorPercent).isEqualTo(floraRecord.behaviorPercent);

    assertThat(detail.collectionList).hasSize(3);

    detail.collectionList.forEach(collectionRecord -> {
      assertThat(collectionRecord.collection).isIn(collectionSet);
      collectionSet.remove(collectionRecord.collection);
      assertThat(collectionRecord.measure).isIn(measureSet);
      measureSet.remove(collectionRecord.measure);
    });
  }

  private FloraDetail initFlora() {
    FloraDetail ret = new FloraDetail();
    ret.num = RND.plusLong(1500000000);
    ret.catalog = RND.str(15);
    ret.collectedBy = RND.str(15);
    ret.family = RND.str(15);
    ret.genus = RND.str(15);
    ret.type = RND.str(15);
    ret.usage = RND.str(15);
    ret.region = RND.str(15);
    ret.collectPlace = RND.str(15);
    ret.collectCoordinate = RND.str(15);
    ret.collectAltitude = RND.str(15);
    ret.collectDate = new Date();
    ret.floraWeight = RND.str(15);
    ret.behaviorPercent = RND.str(15);

    ret.collectionList.add(initCollectionRecord());
    ret.collectionList.add(initCollectionRecord());
    ret.collectionList.add(initCollectionRecord());
    return ret;
  }

  private CollectionRecord initCollectionRecord() {
    CollectionRecord ret = new CollectionRecord();
    ret.collection = RND.str(12);
    ret.measure = RND.str(25);
    return ret;
  }

}
