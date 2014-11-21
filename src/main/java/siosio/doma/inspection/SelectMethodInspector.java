package siosio.doma.inspection;

import java.util.ArrayList;
import java.util.List;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.compiler.RemoveElementQuickFix;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import siosio.doma.DomaBundle;

/**
 * SQLメソッドの検索を行う。
 *
 * @author siosio
 * @since 1
 */
public final class SelectMethodInspector extends DaoMethodInspector {

    /** SelectOptionsの完全修飾名 */
    private static final String SELECT_OPTIONS_CLASS_NAME = "org.seasar.doma.jdbc.SelectOptions";

    /** シングルトンオブジェクト */
    private static final SelectMethodInspector INSPECTION = new SelectMethodInspector();

    /**
     * シングルトンインスタンスをかえす。
     * @return インスタンス
     */
    public static SelectMethodInspector getInspection() {
        return INSPECTION;
    }

    /** 隠蔽コンストラクタ */
    private SelectMethodInspector() {
    }

    /**
     * 以下の検査を行う。
     *
     * <ul>
     *     <li>SQLの存在チェック</li>
     *     <li>パラメータチェック</li>
     * </ul>
     * @param problemsHolder 検査結果の詳細（検査エラーが格納される）
     * @param psiClass 検査対象クラス
     * @param method 検査対象メソッド
     */
    @Override
    public void inspect(ProblemsHolder problemsHolder, PsiClass psiClass, PsiMethod method) {
        validateRequiredSqlFile(problemsHolder, psiClass, method);

        validateSelectOptionsParameter(problemsHolder, method.getParameterList());
    }

    /**
     * {@link #SELECT_OPTIONS_CLASS_NAME}型の引数が最大で1つであることをチェックする。
     *
     * @param problemsHolder 検査結果の詳細（検査エラーが格納される）
     * @param psiParameterList 引数リスト
     */
    private static void validateSelectOptionsParameter(
            ProblemsHolder problemsHolder, PsiParameterList psiParameterList) {

        List<PsiParameter> selectOptionsParameters = new ArrayList<PsiParameter>();
        for (PsiParameter parameter : psiParameterList.getParameters()) {
            if (SELECT_OPTIONS_CLASS_NAME.equals(parameter.getType().getCanonicalText())) {
                selectOptionsParameters.add(parameter);
            }
        }

        if (selectOptionsParameters.size() <= 1) {
            return;
        }
        for (PsiParameter errorParameter : selectOptionsParameters) {
            problemsHolder.registerProblem(
                    errorParameter,
                    DomaBundle.message("inspection.dao.multi-SelectOptions"),
                    ProblemHighlightType.ERROR,
                    new RemoveElementQuickFix(DomaBundle.message("quick-fix.remove", errorParameter.getName()))
            );
        }
    }
}

