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

import static siosio.doma.DomaBundle.message;

/**
 * {@link siosio.doma.inspection.SelectMethodInspector}のテスト
 */
public class SelectMethodInspectorTest extends UsefulTestCase {

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
     * SelectOptions型の引数が1だけの場合のケース
     * <p/>
     * SelectOptions1つはvalidなのでエラーとならないこと
     */
    public void test_SelectOptions引数が1つの場合_検査エラーとならないこと() throws Exception {
        List<HighlightInfo> infos = doInspection("SingleSelectOptions");

        HighlightInfo option = findHighlightInfo(infos, "option");
        assertNull(option);
    }

    /**
     * SelectOptios型の引数が2つある場合のケース
     * <p/>
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

    /**
     * selectメソッドの戻り値がvoidのケース
     * <p/>
     * selectでvoidは許容されないのでエラーとなること
     *
     * @throws Exception
     */
    public void test_戻り値がvoidの場合_検査エラーとなること() throws Exception {
        List<HighlightInfo> infos = doInspection("SelectReturnType");
        HighlightInfo actual = findHighlightInfo(infos, "void");

        assertEquals(HighlightSeverity.ERROR, actual.getSeverity());
        assertEquals(DomaBundle.message("inspection.dao.invalid-selectReturnType"), actual.getDescription());
    }
}
