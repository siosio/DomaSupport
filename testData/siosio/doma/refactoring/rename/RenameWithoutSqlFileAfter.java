package dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import siosio.entity.UserEntity;

@Dao
public interface User {

    @Insert
    int insert2(UserEntity entity);

    @Select
    String find(Long id);
}