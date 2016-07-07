package siosio.doma

import com.intellij.openapi.module.*
import com.intellij.openapi.vfs.*
import com.intellij.psi.search.*
import com.intellij.testFramework.*
import com.intellij.testFramework.fixtures.*

abstract class DaoTestCase : LightCodeInsightFixtureTestCase() {
  override fun setUp() {
    super.setUp();
    createDomaClass()
  }

  override fun getProjectDescriptor(): LightProjectDescriptor {
    return DomaProjectDescriptor()
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
    myFixture.addClass("package org.seasar.doma;"
        + "@Target(ElementType.TYPE)"
        + "@Retention(RetentionPolicy.RUNTIME)"
        + "public @interface Dao {}");
    myFixture.addClass("package org.seasar.doma;"
        + "@Target(ElementType.METHOD)"
        + "@Retention(RetentionPolicy.RUNTIME)"
        + "public @interface Select {" +
        "SelectType strategy() default org.seasar.doma.SelectType.RETURN" +
        "}");
    myFixture.addClass("package org.seasar.doma.jdbc;"
        + "public class SelectOptions {}");
    myFixture.addClass("package org.seasar.doma;"
        + "public enum SelectType {COLLECT, STREAM}");
    myFixture.addClass("package java.lang; public class String {}")
    myFixture.addClass("""package java.util.function; @FunctionalInterface public interface Function<T, R> {
    R apply(T t);
    }  """)
    myFixture.addClass("""package java.util.stream; @FunctionalInterface public interface Stream<T> {}""")

    createUpdateAnnotation("Insert");
    createUpdateAnnotation("Update");
    createUpdateAnnotation("Delete");
    createUpdateAnnotation("BatchInsert");
    createUpdateAnnotation("BatchUpdate");
    createUpdateAnnotation("BatchDelete");
    createUpdateAnnotation("Script");
  }

  private fun createUpdateAnnotation(className: String) {
    myFixture.addClass("""package org.seasar.doma;
        @Target(ElementType.METHOD)
        @Retention(RetentionPolicy.RUNTIME)
        public @interface $className {
          boolean sqlFile() default false;
        }""");
  }
}