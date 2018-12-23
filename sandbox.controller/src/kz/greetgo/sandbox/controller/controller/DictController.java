package kz.greetgo.sandbox.controller.controller;

import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.mvc.annotations.Json;
import kz.greetgo.mvc.annotations.Mapping;
import kz.greetgo.mvc.annotations.Par;
import kz.greetgo.mvc.annotations.ToJson;
import kz.greetgo.sandbox.controller.model.*;
import kz.greetgo.sandbox.controller.register.DictRegister;
import kz.greetgo.sandbox.controller.register.FloraRegister;
import kz.greetgo.sandbox.controller.security.NoSecurity;
import kz.greetgo.sandbox.controller.util.Controller;

import java.util.List;

/**
 * как составлять контроллеры написано
 * <a href="https://github.com/greetgo/greetgo.mvc/blob/master/greetgo.mvc.parent/doc/controller_spec.md">здесь</a>
 */
@Bean
@Mapping("/dict")
public class DictController implements Controller {

  public BeanGetter<DictRegister> dictRegister;

  @NoSecurity
  @ToJson
  @Mapping("/list")
  public List<AdminDictRecord> list(@Json @Par("toFilter") AdminDictToFilter toFilter) {
    return dictRegister.get().getList(toFilter);
  }

  @NoSecurity
  @ToJson
  @Mapping("/count")
  public int count(@Json @Par("toFilter") AdminDictToFilter toFilter) {
    return dictRegister.get().getCount(toFilter);
  }

  @NoSecurity
  @ToJson
  @Mapping("/detail")
  public AdminDictDetail detail( @Par("dictId") String dictId) {
    return dictRegister.get().detail(dictId);
  }

  @NoSecurity
  @ToJson
  @Mapping("/save")
  public void save(@Json @Par("toSave") AdminDictDetail toSave) {
    dictRegister.get().save(toSave);
  }

  @NoSecurity
  @ToJson
  @Mapping("/dictTypeDict")
  public List<DictRecord> dictTypeDict() {
    return dictRegister.get().dictTypeDict();
  }

  @NoSecurity
  @ToJson
  @Mapping("/parentDict")
  public List<DictRecord> parentDict(@Json @Par("toFilter") DictSimpleToFilter toFilter) {
    return dictRegister.get().parentDict(toFilter);
  }

  @NoSecurity
  @ToJson
  @Mapping("/remove")
  public void remove(@Par("code") String code) {
    dictRegister.get().remove(code);
  }

}
