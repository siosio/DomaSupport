package siosio.doma.inspection.dao.quickfix

import com.intellij.codeInspection.*
import com.intellij.ide.util.*
import com.intellij.openapi.fileEditor.*
import com.intellij.openapi.module.*
import com.intellij.openapi.project.*
import com.intellij.openapi.roots.*
import com.intellij.openapi.util.text.*
import com.intellij.openapi.vfs.*
import com.intellij.psi.*
import com.intellij.util.*
import siosio.doma.*
import java.io.*
import java.util.*

/**
 * SQLファイルを作成するクィックフィックス実装。
 */
class CreateSqlFileQuickFix(
    val module: Module,
    val sqlFilePath: String) : LocalQuickFix {

  override fun getName() = getFamilyName()

  override fun getFamilyName() = DomaBundle.message("quick-fix.create-sql-file")

  override fun applyFix(project: Project, problemDescriptor: ProblemDescriptor) {
    val roots = ModuleRootManager.getInstance(module).getSourceRoots(false)

    val psiDirectories = roots.map { PsiManager.getInstance(project).findDirectory(it) }.toTypedArray()

    val rootDir = DirectoryChooserUtil.chooseDirectory(
        psiDirectories, null, project, HashMap<PsiDirectory, String>())

    rootDir?.let {
      it.virtualFile
    }?.let {
      val sqlFile = SqlFile(sqlFilePath)
      try {
        VfsUtil.createDirectoryIfMissing(it, sqlFile.parentDirPath)
      } catch (e: IOException) {
        throw IncorrectOperationException(e)
      }
      val sqlOutputDir = PsiManager.getInstance(project).findDirectory(
          VfsUtil.findRelativeFile(it, *sqlFile.parentDirSplitPaths)!!)
      FileEditorManager.getInstance(project)
          .openFile(sqlOutputDir!!.createFile(sqlFile.fileName).virtualFile, true)
    }
  }

  class SqlFile(val sqlFilePath: String) {
    val fileName: String
    val parentDirPath: String
    val parentDirSplitPaths:Array<String>

    init {
      val f = File(sqlFilePath)
      fileName = f.name
      parentDirPath = f.parentFile.path.replace('\\', '/')
      parentDirSplitPaths = StringUtil.split(parentDirPath, "/").toTypedArray()
    }
  }
}

