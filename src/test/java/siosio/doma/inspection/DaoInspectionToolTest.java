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

    protected void doNormalEndTest(String testName, String... methodNames) throws Exception {
        myFixture.configureByFile(testName + ".java");
        myFixture.enableInspections(DaoInspectionTool.class);
        List<HighlightInfo> highlightInfos = myFixture.doHighlighting();

        for (String name : methodNames) {
            HighlightInfo info = findHighlightInfo(highlightInfos, name);
            assertFalse(name + "メソッドはInspection対象外なのでエラー報告されないこと",
                    info.getSeverity() == HighlightSeverity.ERROR);
        }
    }

    private HighlightInfo findHighlightInfo(List<HighlightInfo> highlightInfos, String elementName) {
        for (HighlightInfo highlightInfo : highlightInfos) {
            if (elementName.equals(highlightInfo.getText())) {
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
        doNormalEndTest("DAOではないインタフェース", "findById");
    }

    /**
     * DAOインタフェースでアノテーションの設定されていないメソッドのケース
     * <p/>
     * アノテーションが設定されていないメソッドなので、SQLファイルがなくてもエラーとならないこと
     */
    public void test_DAOメソッドが存在しない場合_検査対象外() throws Exception {
        doNormalEndTest("DAOメソッドではない", "findById", "findByName");
    }

    /**
     * DAOメソッドでSQLファイルが
     * <p/>
     * アノテーションが設定されていないメソッドなので、SQLファイルがなくてもエラーとならないこと
     */
    public void test_SelectメソッドでSQLファイルがある場合_検査エラーとはならない() throws Exception {
        doNormalEndTest("SQLファイルが存在している", "findById", "findByName");
    }
}


