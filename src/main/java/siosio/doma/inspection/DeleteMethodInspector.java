package siosio.doma.inspection;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import siosio.doma.DaoType;

/**
 * DELETEメソッドの検査を行う。
 */
public class DeleteMethodInspector extends DaoMethodInspector {

    /** しんぐるとんインスタンス */
    private static final DaoMethodInspector INSTANCE = new DeleteMethodInspector();

    /** シングルトン的なインスタンスを取得する。 */
    public static DaoMethodInspector getInstance() {
        return INSTANCE;
    }

    /** コンストラクタ */
    private DeleteMethodInspector() {
    }

    @Override
    void inspect(ProblemsHolder problemsHolder, PsiClass psiClass, PsiMethod method) {
        if (useSqlFile(method, DaoType.DELETE.getAnnotation())) {
            validateRequiredSqlFile(problemsHolder, psiClass, method);
        }
    }
}
