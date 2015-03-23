package siosio.doma.inspection.dao;

import com.intellij.openapi.application.PluginPathManager;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.testFramework.IdeaTestUtil;
import com.intellij.testFramework.LightProjectDescriptor;
import com.intellij.testFramework.fixtures.DefaultLightProjectDescriptor;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.jetbrains.annotations.NotNull;

public class DaoInspectionToolTest extends LightCodeInsightFixtureTestCase {

    @Override
    public String getBasePath() {
        return PluginPathManager.getPluginHomePathRelative("DomaSupport")
                + "/testData/siosio/doma/inspection/dao/select";
    }

    @NotNull
    @Override
    protected LightProjectDescriptor getProjectDescriptor() {
        return new DomaProjectDescriptor();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        myFixture.enableInspections(DaoInspectionTool.class);

        createDomaClass();
        createSqlFile(
                // select
                "SelectDao/SQLファイルあり",
                "SelectDao/selectOptions1つ",
                "SelectDao/selectOptions2つ",
                "InsertDao/SQLファイルあり");
    }

    private void createSqlFile(String... sqlFileNames) {
        for (String name : sqlFileNames) {
            myFixture.addFileToProject("META-INF/dao/" + name + ".sql", "");
        }
    }

    private void createDomaClass() {
        myFixture.addClass("package org.seasar.doma;"
                + "@Target(ElementType.TYPE)"
                + "@Retention(RetentionPolicy.RUNTIME)"
                + "public @interface Dao {}");
        myFixture.addClass("package org.seasar.doma;"
                + "@Target(ElementType.METHOD)"
                + "@Retention(RetentionPolicy.RUNTIME)"
                + "public @interface Select {}");
        myFixture.addClass("package org.seasar.doma;"
                + "@Target(ElementType.METHOD)"
                + "@Retention(RetentionPolicy.RUNTIME)"
                + "public @interface Insert {"
                + " boolean sqlFile() default false;"
                + "}");
        myFixture.addClass("package org.seasar.doma.jdbc;"
                + "public class SelectOptions {}");
    }

    /**
     * selectメソッドのテストを行う。
     */
    public void test_selectメソッド() {
        myFixture.testHighlighting("SelectDao.java");
    }

    /**
     * insertメソッドのテストを行う。
     */
    public void test_insertメソッド() {
        myFixture.testHighlighting("InsertDao.java");
    }

    public static class DomaProjectDescriptor extends DefaultLightProjectDescriptor {

        @Override
        public Sdk getSdk() {
            return IdeaTestUtil.getMockJdk18();
        }
    }
}
