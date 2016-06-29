package dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import siosio.entity.UserEntity;

@Dao
public interface User {

    @Insert(sqlFile = true)
    int insert<caret>(UserEntity entity);

    @Select
    String find(Long id);
}