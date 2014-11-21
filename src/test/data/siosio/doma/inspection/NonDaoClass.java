import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import test.AppConfig

public interface EmployeeDao {

    @Select
    Map<String, Object> findById(int id);
}
