package kz.greetgo.sandbox.db.dao;

import kz.greetgo.sandbox.controller.model.AdminDictDetail;
import kz.greetgo.sandbox.controller.model.CollectionRecord;
import kz.greetgo.sandbox.controller.model.DictRecord;
import kz.greetgo.sandbox.controller.model.FloraDetail;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DictDao {
  void insertDict(AdminDictDetail dictDetail);

  AdminDictDetail loadDict(String dictId);

  @Delete("delete from table_of_dicts where code = #{code}")
  void deleteDict(@Param("code") String code);
}
