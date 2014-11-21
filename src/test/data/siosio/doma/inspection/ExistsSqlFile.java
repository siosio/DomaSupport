package dao

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import test.AppConfig

@Dao(config = AppConfig.class)
public interface EmployeeDao {

    @Select
    Map<String, Object> findById(int id);

    @Select
    Map<String, Object> findByName(String name);

    @Update
    int update1(Emp emp);

    @Update(sqlFile = false)
    int update2(Emp emp);

    @Update(sqlFile = true)
    int update3(Emp emp);
}
