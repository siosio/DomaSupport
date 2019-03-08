package siosio.doma.inspection.dao

import siosio.doma.*

class KotlinDaoInspectionToolTest : DaoTestCase() {

    override fun getTestDataPath(): String = "testData/siosio/doma/inspection/dao/"

    override fun setUp() {
        super.setUp()
        myFixture.enableInspections(KotlinDaoInspectionTool())
        createSqlFile(
                "SelectDaoKotlin/SQLファイルあり.sql",
                "SelectDaoKotlin/selectOptionsなし.sql",
                "SelectDaoKotlin/selectOptions1つ.sql",
                "SelectDaoKotlin/selectOptions2つ.sql",
                
                "InsertDaoKotlin/SQLファイルあり.sql",
        
                "UpdateDaoKotlin/SQLファイルあり.sql",

                "DeleteDaoKotlin/SQLファイルあり.sql"
        )
    }

    fun test_selectメソッド() {
        myFixture.testHighlighting("SelectDaoKotlin.kt")
    }
    
    fun test_insertメソッド() {
        myFixture.testHighlighting("InsertDaoKotlin.kt")
    }
    
    fun test_updateメソッド() {
        myFixture.testHighlighting("UpdateDaoKotlin.kt")
    }
    
    fun test_deleteメソッド() {
        myFixture.testHighlighting("DeleteDaoKotlin.kt")
    }
}
