package siosio.doma.inspection;

import java.io.IOException;
import java.util.HashMap;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.ide.util.DirectoryChooserUtil;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ResourceFileUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.impl.PsiManagerImpl;
import com.intellij.psi.impl.file.PsiDirectoryImpl;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.ArrayUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import siosio.doma.DomaBundle;

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

        GlobalSearchScope scope = GlobalSearchScope.moduleRuntimeScope(module, false);
        VirtualFile virtualFile = ResourceFileUtil.findResourceFileInScope(sqlFileName, method.getProject(), scope);
        if (virtualFile == null) {
            holder.registerProblem(
                    method.getNameIdentifier(),
                    DomaBundle.message("inspection.dao.sql-not-found"),
                    ProblemHighlightType.ERROR,
                    new CreateSqlFileFix(module, sqlFileName));
        }
    }

    /**
     * SQLファイルを作成するクィックフィックス実装。
     */
    private static final class CreateSqlFileFix implements LocalQuickFix {

        /** モジュール */
        private final Module module;

        /** SQLファイルパス(クラスパスからの相対パス) */
        private final String sqlFilePath;

        public CreateSqlFileFix(Module module, String sqlFilePath) {
            this.module = module;
            this.sqlFilePath = sqlFilePath;
        }

        @NotNull
        @Override
        public String getName() {
            return DomaBundle.message("quick-fix.create-sql-file");
        }

        @NotNull
        @Override
        public String getFamilyName() {
            return DomaBundle.message("quick-fix.create-sql-file");
        }

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor) {

            VirtualFile[] roots = ModuleRootManager.getInstance(module).getSourceRoots();
            PsiDirectory[] psiDirectories = new PsiDirectory[roots.length];
            PsiManager psiManager = PsiManager.getInstance(project);
            for (int i = 0; i < roots.length; i++) {
                VirtualFile root = roots[i];
                psiDirectories[i] = psiManager.findDirectory(root);
            }
            PsiDirectory rootDir = DirectoryChooserUtil.chooseDirectory(
                    psiDirectories, null, project, new HashMap<PsiDirectory, String>());

            if (rootDir == null) {
                return;
            }

            int lastIndexOf = sqlFilePath.lastIndexOf("/");
            if (lastIndexOf == -1) {
                return;
            }
            String fileName = sqlFilePath.substring(lastIndexOf + 1);
            String path = sqlFilePath.substring(0, lastIndexOf);
            VirtualFile rootVirtualFileDir = rootDir.getVirtualFile();
            try {
                VfsUtil.createDirectoryIfMissing(rootVirtualFileDir, path);
            } catch (IOException e) {
                throw new IncorrectOperationException(e.getMessage());
            }
            VirtualFile sqlDir = VfsUtil.findRelativeFile(rootVirtualFileDir,
                    ArrayUtil.toStringArray(StringUtil.split( path, "/")));
            new PsiDirectoryImpl((PsiManagerImpl) psiManager, sqlDir).createFile(fileName);
        }
    }
}

