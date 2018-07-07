package kz.greetgo.sandbox.db.dao;

import kz.greetgo.sandbox.controller.model.FloraRecord;

public interface FloraDao {
  void insertFlora(FloraRecord floraRecord);

  FloraRecord loadFlora(Integer floraId);

  Integer loadFloraId();
}
