package siosio.doma.inspection;

import java.io.IOException;
import java.util.HashMap;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.ide.util.DirectoryChooserUtil;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.util.ArrayUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import siosio.doma.DomaBundle;

/**
 * SQLファイルを作成するクィックフィックス実装。
 */
final class CreateSqlFileQuickFix implements LocalQuickFix {

    /** モジュール */
    private final Module module;

    /** SQLファイルパス(クラスパスからの相対パス) */
    private final String sqlFilePath;

    public CreateSqlFileQuickFix(Module module, String sqlFilePath) {
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


        PsiDirectory sqlOutputDir = PsiManager.getInstance(project)
                .findDirectory(VfsUtil.findRelativeFile(rootVirtualFileDir,
                        ArrayUtil.toStringArray(StringUtil.split(path, "/"))));
        PsiFile file = sqlOutputDir.createFile(fileName);
        FileEditorManager editorManager = FileEditorManager.getInstance(project);
        editorManager.openFile(file.getVirtualFile(), true);
    }
}

