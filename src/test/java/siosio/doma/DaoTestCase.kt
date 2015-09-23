package siosio.doma

import com.intellij.openapi.module.*
import com.intellij.openapi.vfs.*
import com.intellij.openapi.vfs.newvfs.impl.*
import com.intellij.psi.search.*
import com.intellij.testFramework.*
import com.intellij.testFramework.fixtures.*

public open class DaoTestCase : LightCodeInsightFixtureTestCase() {
  override fun setUp() {
    VfsRootAccess.SHOULD_PERFORM_ACCESS_CHECK = false
    super.setUp();
    createDomaClass()
  }

  override fun getProjectDescriptor(): LightProjectDescriptor {
    return DomaProjectDescriptor()
  }

  internal fun createSqlFile(className:String, vararg sqlFileNames: String) {
    for (name in sqlFileNames) {
      myFixture.addFileToProject("META-INF/dao/${className}/${name}", "");
    }
  }

  internal fun findSqlFile(classname:String, sqlFileName: String): VirtualFile? {
    val scope = GlobalSearchScope.moduleRuntimeScope(myModule, false)
    return ResourceFileUtil.findResourceFileInScope("META-INF/dao/${classname}/${sqlFileName}", myFixture.getProject(), scope)
  }

  /**
   * テストで必要となるDoma関連クラスを作成する。
   */
  private fun createDomaClass() {
    myFixture.addClass("package org.seasar.doma;"
        + "@Target(ElementType.TYPE)"
        + "@Retention(RetentionPolicy.RUNTIME)"
        + "public @interface Dao {}");
    myFixture.addClass("package org.seasar.doma;"
        + "@Target(ElementType.METHOD)"
        + "@Retention(RetentionPolicy.RUNTIME)"
        + "public @interface Select {}");
    myFixture.addClass("package org.seasar.doma.jdbc;"
        + "public class SelectOptions {}");

    createUpdateAnnotation("Insert");
    createUpdateAnnotation("Update");
    createUpdateAnnotation("Delete");
    createUpdateAnnotation("BatchInsert");
  }

  private fun createUpdateAnnotation(className: String) {
    myFixture.addClass("""package org.seasar.doma;
        @Target(ElementType.METHOD)
        @Retention(RetentionPolicy.RUNTIME)
        public @interface ${className} {
          boolean sqlFile() default false;
        }""");
  }
}