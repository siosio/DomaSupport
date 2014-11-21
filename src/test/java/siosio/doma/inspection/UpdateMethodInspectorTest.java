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
import siosio.doma.DomaBundle;

/**
 * {@linke UpdateMethodInspectorTest}のテストクラス
 */
public class UpdateMethodInspectorTest extends UsefulTestCase {


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
        builder.addLibrary("select", PathUtil.getJarPathForClass(Update.class));
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

        HighlightInfo info = findHighlightInfo(infos, "findById");
        assertTrue("メソッドはInspection対象外なのでエラー報告されないこと",
                info.getSeverity() != HighlightSeverity.ERROR);
    }

    /**
     * DAOインタフェースでアノテーションの設定されていないメソッドのケース
     * <p/>
     * アノテーションが設定されていないメソッドなので、SQLファイルがなくてもエラーとならないこと
     */
    public void test_DAOメソッドが存在しない場合_検査対象外() throws Exception {
        List<HighlightInfo> infos = doInspection("NonDaoMethod");

        assertTrue(findHighlightInfo(infos, "update").getSeverity() != HighlightSeverity.ERROR);
    }

    /**
     * UpdateメソッドでsqlFileがtrueでSQLファイルが存在しない場合
     *
     * 検査エラーとなること
     */
    public void test_useSqlがtrueでSQLファイルが存在しない場合_検査エラーとなること() {
        List<HighlightInfo> infos = doInspection("NotExistsSqlFile");

        HighlightInfo info = findHighlightInfo(infos, "update");
        assertTrue("SQLファイルが存在しないのでエラーとなる", info.getSeverity() == HighlightSeverity.ERROR);
        assertEquals(DomaBundle.message("inspection.dao.sql-not-found"), info.getDescription());
    }

    /**
     * UpdateメソッドでsqlFileがfalseyaでSQLファイルが存在しない場合
     *
     * 検査エラーとなること
     */
    public void test_useSqlがfalseでSQLファイルが存在しない場合_検査エラーとならないこと() {
        List<HighlightInfo> infos = doInspection("ExistsSqlFile");

        assertTrue("SQLファイルが存在しないのでエラーとなる", findHighlightInfo(infos, "update1").getSeverity() != HighlightSeverity.ERROR);
        assertTrue("SQLファイルが存在しないのでエラーとなる", findHighlightInfo(infos, "update2").getSeverity() != HighlightSeverity.ERROR);
        assertTrue("SQLファイルが存在しないのでエラーとなる", findHighlightInfo(infos, "update3").getSeverity() != HighlightSeverity.ERROR);
    }
}

