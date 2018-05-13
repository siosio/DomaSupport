package siosio.doma.refactoring

import siosio.doma.*

class DaoMethodRenameProcessorTest : DaoTestCase() {

    override fun getTestDataPath(): String? = "testData/siosio/doma/refactoring/rename/"

    fun test_daoメソッド名変更() {
        createSqlFile("User/insert.sql")

        myFixture.configureByFiles("DaoMethodRename.java")
        myFixture.renameElementAtCaret("insertUser")
        myFixture.checkResultByFile("DaoMethodRenameAfter.java", false)

        assert(findSqlFile("User/insertUser.sql") != null) { "リネーム後のSQLファイルが存在すること" }
        assert(findSqlFile("User/insert.sql") == null) { "リネーム前のSQLファイルは存在しないこと" }
    }

    fun test_SQLファイルなし_メソッド名のリネームは成功すること() {
        myFixture.configureByFiles("RenameWithoutSqlFile.java")
        myFixture.renameElementAtCaret("insert2")
        myFixture.checkResultByFile("RenameWithoutSqlFileAfter.java", false)
    }
}
