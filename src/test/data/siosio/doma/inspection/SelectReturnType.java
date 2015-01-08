package dao;

import org.seasar.doma.Dao;

import org.seasar.doma.Select;
import test.AppConfig;
import org.seasar.doma.jdbc.SelectOptions;

import siosio.doma.inspection.TestEnum;

import siosio.doma.inspection.SelectMethodInspectorTest;

@Dao(config = AppConfig.class)
public interface EmployeeDao {

    @Select
    void select(Entity entity);

    @Select
    TestEnum selectHoge(Long id);

}
