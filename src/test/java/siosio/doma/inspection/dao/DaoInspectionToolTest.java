package siosio.doma.inspection.dao;

import com.intellij.testFramework.LightProjectDescriptor;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.jetbrains.annotations.NotNull;
import siosio.doma.DaoTestCase;
import siosio.doma.DomaProjectDescriptor;

public class DaoInspectionToolTest extends DaoTestCase {

    @Override
    protected String getTestDataPath() {
        final String path = super.getTestDataPath();
        return path + "/testData/siosio/doma/inspection/dao/";
    }


    @Override
    protected void setUp() {
        super.setUp();
        myFixture.enableInspections(DaoInspectionTool.class);
        createSqlFile(
                "SelectDao/SQLファイルあり.sql",
                "SelectDao/selectOptionsなし.sql",
                "SelectDao/selectOptions1つ.sql",
                "SelectDao/selectOptions2つ.sql",
                "InsertDao/SQLファイルあり.sql",
                "UpdateDao/SQLファイルあり.sql",
                "DeleteDao/SQLファイルあり.sql",
                "BatchInsertDao/SQLファイルあり.sql"
        );
    }

    /**
     * selectメソッドのテストを行う。
     */
    public void test_selectメソッド() {
        myFixture.testHighlighting("SelectDao.java");
    }

    /**
     * insertメソッドのテストを行う。
     */
    public void test_insertメソッド() {
        myFixture.testHighlighting("InsertDao.java");
    }

    /**
     * updateメソッドのテストを行う。
     */
    public void test_updateメソッド() {
        myFixture.testHighlighting("UpdateDao.java");
    }

    /**
     * deleteメソッドのテストを行う。
     */
    public void test_deleteメソッド() {
        myFixture.testHighlighting("DeleteDao.java");
    }

    /**
     * batchInsertメソッドのテストを行う。
     */
    public void test_batchInsertメソッド() {
        myFixture.testHighlighting("BatchInsertDao.java");
    }

}
