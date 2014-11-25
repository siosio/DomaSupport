package siosio.doma.inspection;

import com.intellij.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;
import siosio.doma.DaoType;
import siosio.doma.DomaBundle;
import siosio.doma.DomaUtils;

/**
 * DomaのDAOのチェックを行うクラス。
 */
public class DaoInspectionTool extends BaseJavaLocalInspectionTool {

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

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {

            @Override
            public void visitMethod(PsiMethod method) {
                super.visitMethod(method);

                PsiClass psiClass = method.getContainingClass();
                if (!DomaUtils.isDaoMethod(method)) {
                    return;
                }

                DaoMethodInspector inspection = createDaoMethodInspection(method);
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
            private DaoMethodInspector createDaoMethodInspection(PsiMethod method) {
                DaoType type = DomaUtils.toDaoType(method);
                switch (type) {
                    case SELECT:
                        return SelectMethodInspector.getInspection();
                    case UPDATE:
                        return UpdateMethodInspector.getInstance();
                    case INSERT:
                        return InsertMethodInspector.getInstance();
                    case DELETE:
                        return DeleteMethodInspector.getInstance();
                    default:
                        return null;
                }
            }
        };
    }
}

