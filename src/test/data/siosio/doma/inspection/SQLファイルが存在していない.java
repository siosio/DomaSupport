package dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import test.AppConfig;

@Dao(config = AppConfig.class)
public interface EmployeeDao {
    @Select
    Map<String, Object> sqlFound(String name);

    @Select
    Map<String, Object> sqlNotFound(int id);

}
