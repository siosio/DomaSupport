package siosio.doma.inspection.dao;

import com.intellij.openapi.application.PluginPathManager;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

public class DaoInspectionToolTest extends LightCodeInsightFixtureTestCase {

    @Override
    public String getBasePath() {
        return PluginPathManager.getPluginHomePathRelative("DomaSupport")
                + "/testData/siosio/doma/inspection/dao/select";
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        myFixture.enableInspections(DaoInspectionTool.class);

        createDomaClass();
        createSqlFile("SQLファイルあり", "selectOptions1つ", "selectOptions2つ");
    }

    private void createSqlFile(String... sqlFileNames) {
        for (String name : sqlFileNames) {
            myFixture.addFileToProject("META-INF/select/SelectDao/" + name + ".sql", "");
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
        myFixture.addClass("package org.seasar.doma.jdbc;"
                + "public class SelectOptions {}");
    }

    /**
     * selectメソッドのテストを行う。
     */
    public void test_selectメソッド() {
        myFixture.testHighlighting("SelectDao.java");
    }
}
