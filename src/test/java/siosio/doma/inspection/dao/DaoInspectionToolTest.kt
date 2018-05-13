package siosio.doma.inspection.dao

import siosio.doma.*

class DaoInspectionToolTest : DaoTestCase() {

    override fun getTestDataPath(): String = "testData/siosio/doma/inspection/dao/"

    override fun setUp() {
        super.setUp()
        myFixture.enableInspections(DaoInspectionTool())
        createSqlFile(
                "SelectDao/SQLファイルあり.sql",
                "SelectDao/selectOptionsなし.sql",
                "SelectDao/selectOptions1つ.sql",
                "SelectDao/selectOptions2つ.sql",
                "SelectDao/引数にFunctionなし.sql",
                "SelectDao/引数にFunction複数.sql",
                "SelectDao/引数にFunctionあり.sql",
                "InsertDao/SQLファイルあり.sql",
                "InsertDao/SQLファイルありのMutableEntity.sql",
                "InsertDao/SQLファイルありのImmutableEntity.sql",
                "UpdateDao/SQLファイルあり.sql",
                "DeleteDao/SQLファイルあり.sql",
                "BatchInsertDao/SQLファイルあり.sql",
                "BatchInsertDao/SQLファイルありで引数がIterableではない.sql",
                "BatchInsertDao/SQLファイルありで引数がIterableではないその２.sql",
                "BatchUpdateDao/SQLファイルあり.sql",
                "BatchDeleteDao/SQLファイルあり.sql",
                "ScriptDao/SQLファイルあり.script"
        )
    }

    /**
     * selectメソッドのテストを行う。
     */
    fun test_selectメソッド() {
        myFixture.testHighlighting("SelectDao.java")
    }

    /**
     * insertメソッドのテストを行う。
     */
    fun test_insertメソッド() {
        myFixture.testHighlighting("InsertDao.java")
    }

    /**
     * updateメソッドのテストを行う。
     */
    fun test_updateメソッド() {
        myFixture.testHighlighting("UpdateDao.java")
    }

    /**
     * deleteメソッドのテストを行う。
     */
    fun test_deleteメソッド() {
        myFixture.testHighlighting("DeleteDao.java")
    }

    /**
     * batchInsertメソッドのテストを行う。
     */
    fun test_batchInsertメソッド() {
        myFixture.testHighlighting("BatchInsertDao.java")
    }

    /**
     * batchUpdateメソッドのテストを行う。
     */
    fun test_batchUpdateメソッド() {
        myFixture.testHighlighting("BatchUpdateDao.java")
    }

    /**
     * batchDeleteメソッドのテストを行う。
     */
    fun test_batchDeleteメソッド() {
        myFixture.testHighlighting("BatchDeleteDao.java")
    }

    /**
     * scriptメソッドのテストを行う。
     */
    fun test_scriptメソッド() {
        myFixture.testHighlighting("ScriptDao.java")
    }
}
