package dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import test.AppConfig;

@Dao(config = AppConfig.class)
public interface EmployeeDao {

    @Select
    Map<String, Object> sqlNotFound(int id);

    @Update(sqlFile = true)
    int update(Emp emp);

}
