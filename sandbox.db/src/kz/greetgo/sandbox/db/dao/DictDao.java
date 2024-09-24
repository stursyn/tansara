package kz.greetgo.sandbox.db.dao;

import kz.greetgo.sandbox.controller.model.AdminDictDetail;
import kz.greetgo.sandbox.controller.model.CollectionRecord;
import kz.greetgo.sandbox.controller.model.DictRecord;
import kz.greetgo.sandbox.controller.model.FloraDetail;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface DictDao {
  void insertDict(AdminDictDetail dictDetail);

  AdminDictDetail loadDict(String dictId);

  @Delete("delete from table_of_dicts where code = #{code}")
  void deleteDict(@Param("code") String code);

  @Update("update table_of_dicts set image_name = #{image_name}, image=#{image}, " +
      " description = #{description} where code = #{code} and dictType = ${dictType}")
  void updateImage(@Param("code") String code,
                   @Param("dictType") String dictType,
                   @Param("image_name") String imageName,
                   @Param("image") byte[] image,
                   @Param("description") String description);
}
