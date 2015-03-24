package siosio.doma.inspection.dao.quickfix

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.ide.util.DirectoryChooserUtil
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiManager
import com.intellij.util.ArrayUtil
import com.intellij.util.IncorrectOperationException
import siosio.doma.DomaBundle
import java.io.IOException
import java.util.HashMap

/**
 * SQLファイルを作成するクィックフィックス実装。
 */
class CreateSqlFileQuickFix(
    val module: Module,
    val sqlFilePath: String) : LocalQuickFix {

  override fun getName(): String {
    return DomaBundle.message("quick-fix.create-sql-file")
  }

  override fun getFamilyName(): String {
    return DomaBundle.message("quick-fix.create-sql-file")
  }

  override fun applyFix(project: Project, problemDescriptor: ProblemDescriptor) {
    val roots = ModuleRootManager.getInstance(module).getSourceRoots()
    val psiDirectories = arrayOfNulls<PsiDirectory>(roots.size)
    val psiManager = PsiManager.getInstance(project)
    for (i in roots.indices) {
      val root = roots[i]
      psiDirectories[i] = psiManager.findDirectory(root)
    }
    val rootDir = DirectoryChooserUtil.chooseDirectory(psiDirectories, null, project, HashMap<PsiDirectory, String>())

    if (rootDir == null) {
      return
    }

    val lastIndexOf = sqlFilePath.lastIndexOf("/")
    if (lastIndexOf == -1) {
      return
    }
    val fileName = sqlFilePath.substring(lastIndexOf + 1)
    val path = sqlFilePath.substring(0, lastIndexOf)
    val rootVirtualFileDir = rootDir.getVirtualFile()
    try {
      VfsUtil.createDirectoryIfMissing(rootVirtualFileDir, path)
    } catch (e: IOException) {
      throw IncorrectOperationException(e.getMessage())
    }
    val sqlOutputDir = PsiManager.getInstance(project).findDirectory(VfsUtil.findRelativeFile(rootVirtualFileDir, *ArrayUtil.toStringArray(StringUtil.split(path, "/"))))
    val file = sqlOutputDir.createFile(fileName)
    val editorManager = FileEditorManager.getInstance(project)
    editorManager.openFile(file.getVirtualFile(), true)
  }
}