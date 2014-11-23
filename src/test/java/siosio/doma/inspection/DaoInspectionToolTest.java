package siosio.doma.inspection;

import java.util.List;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.testFramework.UsefulTestCase;
import com.intellij.testFramework.builders.JavaModuleFixtureBuilder;
import com.intellij.testFramework.fixtures.CodeInsightTestFixture;
import com.intellij.testFramework.fixtures.IdeaProjectTestFixture;
import com.intellij.testFramework.fixtures.IdeaTestFixtureFactory;
import com.intellij.testFramework.fixtures.JavaTestFixtureFactory;
import com.intellij.testFramework.fixtures.TestFixtureBuilder;
import com.intellij.util.PathUtil;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

import static siosio.doma.DomaBundle.message;

/**
 * {@link DaoInspectionTool}のテストクラス。
 */
public class DaoInspectionToolTest extends UsefulTestCase {

    protected CodeInsightTestFixture myFixture;

    private static final String DATA_PATH = "./src/test/data/siosio/doma/inspection/";


    public void setUp() throws Exception {

        final IdeaTestFixtureFactory fixtureFactory = IdeaTestFixtureFactory.getFixtureFactory();
        final TestFixtureBuilder<IdeaProjectTestFixture> testFixtureBuilder =
                fixtureFactory.createFixtureBuilder(getName());

        myFixture = JavaTestFixtureFactory.getFixtureFactory()
                .createCodeInsightFixture(testFixtureBuilder.getFixture());
        myFixture.setTestDataPath(DATA_PATH);

        final JavaModuleFixtureBuilder builder = testFixtureBuilder.addModule(JavaModuleFixtureBuilder.class);
        builder.addContentRoot(myFixture.getTempDirPath()).addSourceRoot("");
        builder.setMockJdkLevel(JavaModuleFixtureBuilder.MockJdkLevel.jdk15);
        builder.addLibrary("dao", PathUtil.getJarPathForClass(Dao.class));
        builder.addLibrary("select", PathUtil.getJarPathForClass(Select.class));
        builder.addLibrary("update", PathUtil.getJarPathForClass(Update.class));
        builder.addSourceContentRoot("./src/test/data");

        myFixture.setUp();
    }

    public void tearDown() throws Exception {
        myFixture.tearDown();
        myFixture = null;
    }

    /**
     * 指定されたJavaコードに対するインスペクションを実行する。
     *
     * @param testName Javaコード
     */
    private List<HighlightInfo> doInspection(String testName) {
        myFixture.configureByFile(testName + ".java");
        myFixture.enableInspections(DaoInspectionTool.class);
        return myFixture.doHighlighting();
    }

    private HighlightInfo findHighlightInfo(List<HighlightInfo> highlightInfos, String elementName) {
        for (HighlightInfo highlightInfo : highlightInfos) {
            if (highlightInfo.getText().contains(elementName)) {
                return highlightInfo;
            }
        }
        return null;
    }

    /**
     * DAOアノテーションが設定されていないインタフェースのケース
     * <p/>
     * DAOアノテーションが付加されていないので、@Selectアノテーションが設定されていて
     * SQLファイルがない場合でもエラーとはならないこと
     */
    public void test_DAOアノテーションがついていない場合_検査対象外() throws Exception {
        List<HighlightInfo> infos = doInspection("NonDaoClass");
        assertHasNotError(infos, "findById");
        assertHasNotError(infos, "update");
    }

    /**
     * DAOインタフェースでアノテーションの設定されていないメソッドのケース
     * <p/>
     * アノテーションが設定されていないメソッドなので、SQLファイルがなくてもエラーとならないこと
     */
    public void test_DAOメソッドが存在しない場合_検査対象外() throws Exception {
        List<HighlightInfo> infos = doInspection("NonDaoMethod");

        assertHasNotError(infos, "findById");
        assertHasNotError(infos, "findByName");
        assertHasNotError(infos, "update");
    }

    /**
     * DAOメソッドでSQLファイルが存在している場合のケース
     * <p/>
     * アノテーションが設定されていないメソッドなので、SQLファイルがなくてもエラーとならないこと
     */
    public void test_DAOメソッドでSQLファイルがある場合_検査エラーとはならない() throws Exception {
        List<HighlightInfo> infos = doInspection("ExistsSqlFile");

        assertHasNotError(infos, "findById");
        assertHasNotError(infos, "findByName");
        assertHasNotError(infos, "update1");
        assertHasNotError(infos, "update2");
        assertHasNotError(infos, "update3");
    }

    /**
     * DAOメソッドでSQLファイルが存在していない場合のケース
     * <p/>
     * SQLファイルが存在していないメソッドだけ、エラーとなること。
     */
    public void test_DAOメソッドでSQLファイルがない場合_検査エラーとなる() throws Exception {
        List<HighlightInfo> infos = doInspection("NotExistsSqlFile");

        assertHasError(infos, "sqlNotFound");
        assertHasError(infos, "update");
    }


    private void assertHasNotError(List<HighlightInfo> infos, String elementName) {
        HighlightInfo info = findHighlightInfo(infos, elementName);
        assertTrue(elementName + "はInspectionでエラーが発生していないこと",
                info.getSeverity() != HighlightSeverity.ERROR);
    }

    private void assertHasError(List<HighlightInfo> infos, String elementName) {
        HighlightInfo errorMethod = findHighlightInfo(infos, elementName);
        assertEquals("SQLファイルが存在していないメソッドはエラー", HighlightSeverity.ERROR, errorMethod.getSeverity());
        assertEquals(message("inspection.dao.sql-not-found"), errorMethod.getDescription());
    }
}

