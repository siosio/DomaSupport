package siosio.doma.inspection;

import com.intellij.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierList;
import org.jetbrains.annotations.NotNull;
import siosio.doma.DomaBundle;

/**
 * DomaのDAOのチェックを行うクラス。
 */
public class DaoInspectionTool extends BaseJavaLocalInspectionTool {

    /** DAOクラスを表すアノテーションクラス名 */
    private static final String DAO_ANNOTATION_NAME = "org.seasar.doma.Dao";

    /** SELECTメソッドを表すアノテーション */
    private static final String DAO_SELECT_ANNOTATION = "org.seasar.doma.Select";

    @NotNull
    public String getDisplayName() {
        return DomaBundle.message("inspection.dao-inspection");
    }

    public boolean isEnabledByDefault() {
        return true;
    }

    @NotNull
    public String getGroupDisplayName() {
        return "Doma";
    }

    @NotNull
    public String getShortName() {
        return "DaoInspection";
    }

    /**
     * チェック対象のクラスか判定する。
     * <p/>
     * クラスに対して{@link #DAO_ANNOTATION_NAME}アノテーションが設定されている場合はチェック対象とする。
     *
     * @param psiClass 判定対象のクラス
     * @return チェック対象の場合はtrue
     */
    private static boolean isCheckedClass(PsiClass psiClass) {
        PsiModifierList modifierList = psiClass.getModifierList();
        if (modifierList == null) {
            return false;
        }
        PsiAnnotation[] annotations = modifierList.getAnnotations();
        for (PsiAnnotation annotation : annotations) {
            String name = annotation.getQualifiedName();
            if (DAO_ANNOTATION_NAME.equals(name)) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {

            @Override
            public void visitMethod(PsiMethod method) {
                super.visitMethod(method);

                PsiClass psiClass = method.getContainingClass();
                if (!isCheckedClass(psiClass)) {
                    return;
                }

                DaoMethodInspection inspection = createDaoMethodInspection(method);
                if (inspection == null) {
                    return;
                }

                inspection.inspect(holder, psiClass, method);
            }

            /**
             * Inspectionを実行するクラスを生成する。
             *
             * @param method チェック対象メソッド
             * @return Inspectionを実行するクラスのインスタンス
             */
            private DaoMethodInspection createDaoMethodInspection(PsiMethod method) {
                PsiAnnotation[] annotations = method.getModifierList().getAnnotations();
                for (PsiAnnotation annotation : annotations) {
                    if (DAO_SELECT_ANNOTATION.equals(annotation.getQualifiedName())) {
                        return new SelectMethodInspection();
                    }
                }
                return null;
            }
        };
    }
}

