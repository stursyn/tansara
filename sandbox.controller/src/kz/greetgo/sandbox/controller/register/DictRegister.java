package kz.greetgo.sandbox.controller.register;

import kz.greetgo.sandbox.controller.model.*;

import java.util.List;

public interface DictRegister {
  List<AdminDictRecord> getList(AdminDictToFilter toFilter);

  int getCount(AdminDictToFilter toFilter);

  AdminDictDetail detail(String dictId);

  void save(AdminDictDetail toSave);

  List<DictRecord> dictTypeDict();

  List<DictRecord> parentDict(DictSimpleToFilter toFilter);
}
