import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import test.AppConfig;

public interface EmployeeDao {

    @Select
    Map<String, Object> findById(int id);

    @Update
    int update(Emp emp);

}
