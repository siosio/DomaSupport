package siosio.doma

import com.intellij.openapi.module.*
import com.intellij.openapi.vfs.*
import com.intellij.pom.java.*
import com.intellij.psi.search.*
import com.intellij.testFramework.builders.*
import com.intellij.testFramework.fixtures.*
import com.intellij.util.*
import org.seasar.doma.*
import java.io.*

abstract class DaoTestCase : JavaCodeInsightFixtureTestCase() {

    override fun setUp() {
        super.setUp()
        addClassFromJavaFile("testData/siosio/doma/entity/ImmutableEntity.java")
        addClassFromJavaFile("testData/siosio/doma/entity/MutableEntity.java")
    }
    
    override fun tuneFixture(moduleBuilder: JavaModuleFixtureBuilder<*>) {
        super.tuneFixture(moduleBuilder)
        moduleBuilder.setLanguageLevel(LanguageLevel.JDK_1_8);
        moduleBuilder.addJdk("./java/mockJDK-1.8");
        moduleBuilder.addLibrary("doma", PathUtil.getJarPathForClass(Dao::class.java))
        moduleBuilder.addLibrary("test-classes-kt", "./build/classes/kotlin/test")
        moduleBuilder.addLibrary("test-classes-java", "./build/classes/java/test")
    }

    fun createSqlFile(vararg sqlFileNames: String) {
        for (name in sqlFileNames) {
            myFixture.addFileToProject("META-INF/dao/$name", "");
        }
    }

    fun findSqlFile(sqlFileName: String): VirtualFile? {
        val scope = GlobalSearchScope.moduleRuntimeScope(module, false)
        return ResourceFileUtil.findResourceFileInScope("META-INF/dao/$sqlFileName", myFixture.project, scope)
    }

    private infix fun addClassFromJavaFile(path: String) {
        val file = File(path)
        myFixture.addClass(file.readText())
    }
}
