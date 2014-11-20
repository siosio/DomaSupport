package siosio.doma.inspection;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import siosio.doma.DomaBundle;
import siosio.doma.DomaUtils;

/**
 * Daoメソッドの検査を行うインタフェース。
 *
 * @author siosio
 * @since 1
 */
abstract class DaoMethodInspection {

    /**
     * 検査を行う。
     *
     * @param problemsHolder 検査結果の詳細（検査エラーが格納される）
     * @param psiClass 検査対象クラス
     * @param method 検査対象メソッド
     */
    abstract void inspect(ProblemsHolder problemsHolder, PsiClass psiClass, PsiMethod method);

    /**
     * SQLファイル名を生成する。
     *
     * @param psiClass 検査対象クラス
     * @param methodName 検査対象メソッド名
     * @return SQLファイル名（クラスパス配下からの相対パス）
     */
    static String makeSqlFileName(PsiClass psiClass, String methodName) {
        return "META-INF/" + psiClass.getQualifiedName().replace('.', '/') + '/' + methodName + ".sql";
    }

    /**
     * SQLの存在チェックを行う。
     *
     * @param holder 検査結果
     * @param psiClass 検査対象クラス
     * @param method 検査対象メソッド
     */
    static void validateRequiredSqlFile(ProblemsHolder holder, PsiClass psiClass, PsiMethod method) {

        String methodName = method.getName();
        String sqlFileName = makeSqlFileName(psiClass, methodName);

        Module module = ProjectRootManager.getInstance(method.getProject())
                .getFileIndex().getModuleForFile(method.getContainingFile().getVirtualFile());

        if (module == null) {
            return;
        }

        VirtualFile virtualFile = DomaUtils.findSqlFile(method, sqlFileName);
        if (virtualFile == null) {
            holder.registerProblem(
                    method.getNameIdentifier(),
                    DomaBundle.message("inspection.dao.sql-not-found"),
                    ProblemHighlightType.ERROR,
                    new CreateSqlFileQuickFix(module, sqlFileName));
        }
    }
}

