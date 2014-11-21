package siosio.doma.inspection;

import com.intellij.codeInsight.AnnotationUtil;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiAnnotation;
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

    void inspect(ProblemsHolder problemsHolder, PsiClass psiClass, PsiMethod method) {
        PsiAnnotation annotation = AnnotationUtil.findAnnotation(method, DaoType.UPDATE.getAnnotation());
        assert annotation != null;
        Boolean useSql = AnnotationUtil.getBooleanAttributeValue(annotation, "sqlFile");
        if ((useSql != null) && useSql) {
            validateRequiredSqlFile(problemsHolder, psiClass, method);
        }
    }
}
