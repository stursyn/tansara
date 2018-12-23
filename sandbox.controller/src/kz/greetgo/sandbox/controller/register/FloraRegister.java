package kz.greetgo.sandbox.controller.register;

import kz.greetgo.sandbox.controller.model.*;

import java.util.List;

public interface FloraRegister {
  List<FloraRecord> getList(FloraToFilter toFilter);

  int getCount(FloraToFilter toFilter);

  FloraDetail detail(Long floraId);

  void save(FloraDetail toSave);

  List<DictRecord> dictSimple(DictSimpleToFilter toFilter);

  List<EmptyNumsRecord> emptyNums();

  void remove(String floraId);
}
