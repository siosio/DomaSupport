package siosio.doma.inspection.dao.quickfix

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.ide.util.DirectoryChooserUtil
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiManager
import com.intellij.util.ArrayUtil
import com.intellij.util.IncorrectOperationException
import siosio.doma.DomaBundle
import java.io.File
import java.io.IOException
import java.util.Collections
import java.util.HashMap

/**
 * SQLファイルを作成するクィックフィックス実装。
 */
class CreateSqlFileQuickFix(
    val module: Module,
    val sqlFilePath: String) : LocalQuickFix {

  override fun getName() = getFamilyName()

  override fun getFamilyName() = DomaBundle.message("quick-fix.create-sql-file")

  override fun applyFix(project: Project, problemDescriptor: ProblemDescriptor) {
    val roots = ModuleRootManager.getInstance(module).getSourceRoots()

    val psiDirectories = roots.map { PsiManager.getInstance(project).findDirectory(it) }.copyToArray()

    val rootDir = DirectoryChooserUtil.chooseDirectory(
        psiDirectories, null, project, HashMap<PsiDirectory, String>())

    rootDir?.let {
      it.getVirtualFile()
    }?.let {
      val sqlFile = SqlFile(sqlFilePath)
      try {
        VfsUtil.createDirectoryIfMissing(it, sqlFile.parentDirPath)
      } catch (e: IOException) {
        throw IncorrectOperationException(e.getMessage())
      }
      val sqlOutputDir = PsiManager.getInstance(project).findDirectory(
          VfsUtil.findRelativeFile(it, *sqlFile.parentDirSplitPaths)!!)
      FileEditorManager.getInstance(project)
          .openFile(sqlOutputDir!!.createFile(sqlFile.fileName).getVirtualFile(), true)
    }
  }

  class SqlFile(val sqlFilePath: String) {
    val fileName: String
    val parentDirPath: String
    val parentDirSplitPaths:Array<String>

    init {
      val f = File(sqlFilePath)
      fileName = f.getName()
      parentDirPath = f.getParentFile().getPath()
      parentDirSplitPaths = StringUtil.split(parentDirPath, "/").copyToArray()
    }
  }
}

