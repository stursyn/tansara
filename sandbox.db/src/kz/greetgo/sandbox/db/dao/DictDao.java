package kz.greetgo.sandbox.db.dao;

import kz.greetgo.sandbox.controller.model.AdminDictDetail;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface DictDao {
  void insertDict(AdminDictDetail dictDetail);

  AdminDictDetail loadDict(String dictId, String dictType);

  @Delete("delete from table_of_dicts where code = #{code} and dictType = #{dictType}")
  void deleteDict(@Param("code") String code,
                  @Param("dictType") String dictType);

  @Update("update table_of_dicts set image_name = #{image_name}, image=#{image}, " +
      " description = #{description} where code = #{code} and dictType = #{dictType}")
  void updateImage(@Param("code") String code,
                   @Param("dictType") String dictType,
                   @Param("image_name") String imageName,
                   @Param("image") byte[] image,
                   @Param("description") String description);
}
