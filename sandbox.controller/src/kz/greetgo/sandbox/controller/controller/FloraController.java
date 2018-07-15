package kz.greetgo.sandbox.controller.controller;

import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.mvc.annotations.*;
import kz.greetgo.sandbox.controller.model.*;
import kz.greetgo.sandbox.controller.register.AuthRegister;
import kz.greetgo.sandbox.controller.register.FloraRegister;
import kz.greetgo.sandbox.controller.security.NoSecurity;
import kz.greetgo.sandbox.controller.util.Controller;

import java.util.List;

/**
 * как составлять контроллеры написано
 * <a href="https://github.com/greetgo/greetgo.mvc/blob/master/greetgo.mvc.parent/doc/controller_spec.md">здесь</a>
 */
@Bean
@Mapping("/flora")
public class FloraController implements Controller {

  public BeanGetter<FloraRegister> floraRegister;

  @NoSecurity
  @ToJson
  @Mapping("/list")
  public List<FloraRecord> list(@Json @Par("toFilter") FloraToFilter toFilter) {
    return floraRegister.get().getList(toFilter);
  }

  @NoSecurity
  @ToJson
  @Mapping("/count")
  public int count(@Json @Par("toFilter") FloraToFilter toFilter) {
    return floraRegister.get().getCount(toFilter);
  }

  @NoSecurity
  @ToJson
  @Mapping("/detail")
  public FloraDetail detail( @Par("floraId") String floraId) {
    return floraRegister.get().detail(Long.parseLong(floraId));
  }

  @NoSecurity
  @ToJson
  @Mapping("/save")
  public void save(@Json @Par("toSave") FloraDetail toSave) {
    floraRegister.get().save(toSave);
  }

  @NoSecurity
  @ToJson
  @Mapping("/dict_simple")
  public List<DictRecord> dictSimple(@Json @Par("toFilter") DictSimpleToFilter toFilter) {
    return floraRegister.get().dictSimple(toFilter);
  }

  @NoSecurity
  @ToJson
  @Mapping("/empty_nums")
  public List<EmptyNumsRecord> emptyNums() {
    return floraRegister.get().emptyNums();
  }
}
