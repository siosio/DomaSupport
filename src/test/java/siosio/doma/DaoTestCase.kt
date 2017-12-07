package siosio.doma

import com.intellij.openapi.module.*
import com.intellij.openapi.roots.*
import com.intellij.openapi.util.io.*
import com.intellij.openapi.vfs.*
import com.intellij.pom.java.*
import com.intellij.psi.search.*
import com.intellij.psi.util.*
import com.intellij.testFramework.*
import com.intellij.testFramework.fixtures.*
import com.intellij.util.*
import org.seasar.doma.*
import org.seasar.doma.jdbc.*
import siosio.doma.inspection.dao.entity.*
import java.io.*
import java.util.function.Function
import java.util.stream.*

abstract class DaoTestCase : LightCodeInsightFixtureTestCase() {

    override fun setUp() {
        super.setUp();
        LanguageLevelProjectExtension.getInstance(project).languageLevel = LanguageLevel.JDK_1_8
        createDomaClass()
    }

    fun createSqlFile(vararg sqlFileNames: String) {
        for (name in sqlFileNames) {
            myFixture.addFileToProject("META-INF/dao/$name", "");
        }
    }

    fun findSqlFile(sqlFileName: String): VirtualFile? {
        val scope = GlobalSearchScope.moduleRuntimeScope(myModule, false)
        return ResourceFileUtil.findResourceFileInScope("META-INF/dao/$sqlFileName", myFixture.project, scope)
    }

    /**
     * テストで必要となるDoma関連クラスを作成する。
     */
    private fun createDomaClass() {
        // doma
        addLibrary<Dao>()
        // jre
        addLibrary<java.lang.Object>()
        
        addClassFromJavaFile("testData/siosio/doma/entity/ImmutableEntity.java")
        addClassFromJavaFile("testData/siosio/doma/entity/MutableEntity.java")

    }
    
    private infix fun addClassFromJavaFile(path: String) {
        val file = File(path)
        myFixture.addClass(file.readText())
    }

    private inline fun <reified T : Any> addLibrary() {
        PsiTestUtil.addLibrary(myModule, PathUtil.getJarPathForClass(T::class.java))
    }
}
