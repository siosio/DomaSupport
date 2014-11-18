package siosio.doma.inspection;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;

/**
 * SQLメソッドの検索を行う。
 *
 * @author siosio
 * @since 1
 */
public class SelectMethodInspection extends DaoMethodInspection {

    /**
     * 以下の検査を行う。
     *
     * <ul>
     *     <li>SQLの存在チェック</li>
     * </ul>
     * @param problemsHolder 検査結果の詳細（検査エラーが格納される）
     * @param psiClass 検査対象クラス
     * @param method 検査対象メソッド
     */
    @Override
    public void inspect(ProblemsHolder problemsHolder, PsiClass psiClass, PsiMethod method) {
        validateRequiredSqlFile(problemsHolder, psiClass, method);
    }
}
