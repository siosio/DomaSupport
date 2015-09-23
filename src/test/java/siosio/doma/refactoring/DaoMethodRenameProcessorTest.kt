package siosio.doma.refactoring

import com.intellij.openapi.module.*
import com.intellij.openapi.vfs.*
import com.intellij.openapi.vfs.newvfs.impl.*
import com.intellij.psi.search.*
import com.intellij.testFramework.*
import com.intellij.testFramework.fixtures.*
import siosio.doma.*

public class DaoMethodRenameProcessorTest : DaoTestCase() {

  override fun getTestDataPath(): String? {
    val path = super.getTestDataPath();
    return "${path}/testData/siosio/doma/refactoring/";
  }

  fun test_daoメソッド名変更() {
    createSqlFile("User/insert.sql")

    myFixture.configureByFiles("DaoMethodRename.java")
    myFixture.renameElementAtCaret("insertUser")
    myFixture.checkResultByFile("DaoMethodRenameAfter.java", false)

    assert(findSqlFile("User/insertUser.sql") != null, "リネーム後のSQLファイルが存在すること")
    assert(findSqlFile("User/insert.sql") == null, "リネーム前のSQLファイルは存在しないこと")
  }
}