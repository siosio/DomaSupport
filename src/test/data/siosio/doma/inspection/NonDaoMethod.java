import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import test.AppConfig;

@Dao(config = AppConfig.class)
public interface EmployeeDao {

    Map<String, Object> findById(int id);

    Map<String, Object> findByName(String name);

    int update(Emp emp);
}
