package dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import test.AppConfig;
import org.seasar.doma.jdbc.SelectOptions;

@Dao(config = AppConfig.class)
public interface EmployeeDao {

    @Select
    Map<String, Object> oneSelectOptions(SelectOptions option);

}
