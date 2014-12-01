package siosio.doma.inspection;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import siosio.doma.DaoType;

/**
 * UPDATEメソッドの検査を行うクラス。
 */
public class UpdateMethodInspector extends DaoMethodInspector {

    /** しんぐるとんインスタンス */
    private static final DaoMethodInspector INSTANCE = new UpdateMethodInspector();

    /** シングルトン的なインスタンスを取得する。 */
    public static DaoMethodInspector getInstance() {
        return INSTANCE;
    }

    /** コンストラクタ */
    private UpdateMethodInspector() {
    }

    /**
     * 以下の検査を行う。
     *
     * <ul>
     *     <li>sqlFile属性がtrueの場合のSQLの存在チェック</li>
     * </ul>
     * @param problemsHolder 検査結果の詳細（検査エラーが格納される）
     * @param psiClass 検査対象クラス
     * @param method 検査対象メソッド
     */
    void inspect(ProblemsHolder problemsHolder, PsiClass psiClass, PsiMethod method) {
        if (useSqlFile(method, DaoType.UPDATE.getAnnotation())) {
            validateRequiredSqlFile(problemsHolder, psiClass, method);
        }
    }
}
