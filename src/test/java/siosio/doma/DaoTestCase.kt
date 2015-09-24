package siosio.doma

import com.intellij.openapi.module.*
import com.intellij.openapi.vfs.*
import com.intellij.openapi.vfs.newvfs.impl.*
import com.intellij.psi.search.*
import com.intellij.testFramework.*
import com.intellij.testFramework.fixtures.*

internal abstract class DaoTestCase : LightCodeInsightFixtureTestCase() {
  override fun setUp() {
    VfsRootAccess.SHOULD_PERFORM_ACCESS_CHECK = false
    super.setUp();
    createDomaClass()
  }

  override fun getProjectDescriptor(): LightProjectDescriptor {
    return DomaProjectDescriptor()
  }

  internal fun createSqlFile(vararg sqlFileNames: String) {
    for (name in sqlFileNames) {
      myFixture.addFileToProject("META-INF/dao/${name}", "");
    }
  }

  internal fun findSqlFile(sqlFileName: String): VirtualFile? {
    val scope = GlobalSearchScope.moduleRuntimeScope(myModule, false)
    return ResourceFileUtil.findResourceFileInScope("META-INF/dao/${sqlFileName}", myFixture.getProject(), scope)
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
    createUpdateAnnotation("Script");
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