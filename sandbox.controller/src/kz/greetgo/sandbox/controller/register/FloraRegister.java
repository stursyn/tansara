package kz.greetgo.sandbox.controller.register;

import kz.greetgo.sandbox.controller.model.FloraRecord;
import kz.greetgo.sandbox.controller.model.FloraToFilter;

import java.util.List;

public interface FloraRegister {
  List<FloraRecord> getList(FloraToFilter toFilter);

  int getCount(FloraToFilter toFilter);

  FloraRecord detail(Integer floraId);

  void save(FloraRecord toSave);
}
