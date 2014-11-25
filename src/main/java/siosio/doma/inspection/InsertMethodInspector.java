package siosio.doma.inspection;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import siosio.doma.DaoType;

/**
 * INSERTメソッドの検査を行うクラス。
 */
public class InsertMethodInspector extends DaoMethodInspector {

    /** しんぐるとんインスタンス */
    private static final DaoMethodInspector INSTANCE = new InsertMethodInspector();

    /** シングルトン的なインスタンスを取得する。 */
    public static DaoMethodInspector getInstance() {
        return INSTANCE;
    }

    /** コンストラクタ */
    private InsertMethodInspector() {
    }

    @Override
    void inspect(ProblemsHolder problemsHolder, PsiClass psiClass, PsiMethod method) {
        if (useSqlFile(method, DaoType.INSERT.getAnnotation())) {
            validateRequiredSqlFile(problemsHolder, psiClass, method);
        }
    }
}

