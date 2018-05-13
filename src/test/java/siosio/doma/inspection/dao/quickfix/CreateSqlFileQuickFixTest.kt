package siosio.doma.inspection.dao.quickfix

import com.intellij.openapi.module.*
import com.intellij.openapi.project.*
import com.intellij.openapi.vfs.*
import com.intellij.psi.*
import com.intellij.psi.impl.file.*
import siosio.doma.*
import siosio.doma.inspection.dao.*

class CreateSqlFileQuickFixTest : DaoTestCase() {

    override fun getTestDataPath(): String = "testData/siosio/doma/inspection/dao/quickfix"

    override fun setUp() {
        super.setUp()
        myFixture.enableInspections(DaoInspectionTool())
        System.setProperty("DirectoryChooser.className", "siosio.doma.inspection.dao.quickfix.CreateSqlFileQuickFixTest\$DirectoryChooserMock")
    }

    override fun tearDown() {
        super.tearDown()
        System.clearProperty("DirectoryChooser.className")
    }

    fun test_SQLファイルが作られることのテスト() {
        DirectoryChooserMock.sqlDir = PsiDirectoryFactory.getInstance(project)
                .createDirectory(myFixture.tempDirFixture.findOrCreateDir("META-INF").parent)

        myFixture.configureByFile("QuickFixDao.java")
        val intention = myFixture.findSingleIntention(DomaBundle.message("quick-fix.create-sql-file"))
        myFixture.launchAction(intention)
        myFixture.testHighlighting("QuickFixDao.java")
    }

    class DirectoryChooserMock : DirectoryChooser() {
        companion object {
            var sqlDir: PsiDirectory? = null
        }
        override fun chooseDirectory(project: Project, module: Module, isInTest: Boolean): PsiDirectory? {
            return sqlDir
        }
    }
}
