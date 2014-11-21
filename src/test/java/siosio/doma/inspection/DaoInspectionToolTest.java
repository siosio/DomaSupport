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

        assertTrue(findHighlightInfo(infos, "findById").getSeverity() != HighlightSeverity.ERROR);
        assertTrue(findHighlightInfo(infos, "findByName").getSeverity() != HighlightSeverity.ERROR);
    }

    /**
     * DAOメソッドでSQLファイルが存在している場合のケース
     * <p/>
     * アノテーションが設定されていないメソッドなので、SQLファイルがなくてもエラーとならないこと
     */
    public void test_SelectメソッドでSQLファイルがある場合_検査エラーとはならない() throws Exception {
        List<HighlightInfo> infos = doInspection("ExistsSqlFile");

        assertTrue(findHighlightInfo(infos, "findById").getSeverity() != HighlightSeverity.ERROR);
        assertTrue(findHighlightInfo(infos, "findByName").getSeverity() != HighlightSeverity.ERROR);
    }

    /**
     * DAOメソッドでSQLファイルが存在していない場合のケース
     * <p/>
     * SQLファイルが存在していないメソッドだけ、エラーとなること。
     */
    public void test_SelectメソッドでSQLファイルがない場合_検査エラーとなる() throws Exception {
        List<HighlightInfo> infos = doInspection("NotExistsSqlFile");

        assertTrue("SQLが存在しているメソッドはエラーとならない",
                findHighlightInfo(infos, "sqlFound").getSeverity() != HighlightSeverity.ERROR);
        HighlightInfo errorMethod = findHighlightInfo(infos, "sqlNotFound");
        assertEquals("SQLファイルが存在していないメソッドはエラー", HighlightSeverity.ERROR, errorMethod.getSeverity());
        assertEquals(message("inspection.dao.sql-not-found"), errorMethod.getDescription());
    }

    /**
     * SelectOptions型の引数が1だけの場合のケース
     *
     * SelectOptions1つはvalidなのでエラーとならないこと
     */
    public void test_SelectOptions引数が1つの場合_検査エラーとならないこと() throws Exception {
        List<HighlightInfo> infos = doInspection("SingleSelectOptions");

        HighlightInfo option = findHighlightInfo(infos, "option");
        assertNull(option);
    }

    /**
     * SelectOptios型の引数が2つある場合のケース
     *
     * SelectOptions型の引数は両方ともエラーとなること
     */
    public void test_SelectOptions引数が2つの場合_検査エラーとなること() throws Exception {
        List<HighlightInfo> infos = doInspection("MultiSelectOptions");

        HighlightInfo option1 = findHighlightInfo(infos, "option1");
        assertEquals(HighlightSeverity.ERROR, option1.getSeverity());
        assertEquals(message("inspection.dao.multi-SelectOptions"), option1.getDescription());

        HighlightInfo option2 = findHighlightInfo(infos, "option2");
        assertEquals(HighlightSeverity.ERROR, option2.getSeverity());
        assertEquals(message("inspection.dao.multi-SelectOptions"), option2.getDescription());
    }
}

