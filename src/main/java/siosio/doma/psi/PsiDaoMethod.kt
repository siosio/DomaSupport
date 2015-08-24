package siosio.doma.psi

import com.intellij.codeInsight.AnnotationUtil
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ResourceFileUtil
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiMethod
import com.intellij.psi.search.GlobalSearchScope
import siosio.doma.DaoType

public class PsiDaoMethod(val psiMethod: PsiMethod, val daoType: DaoType) : PsiMethod by psiMethod {

  val daoAnnotation: PsiDaoAnnotation

  init {
    daoAnnotation = PsiDaoAnnotation(AnnotationUtil.findAnnotation(this, daoType.annotationName)!!)
  }

  fun getModule(): Module {
    val module = ProjectRootManager.getInstance(this.getProject())
        .getFileIndex()
        .getModuleForFile(this.getContainingFile().getVirtualFile())!!
    return module
  }

  fun getSqlFilePath(): String {
    return ("META-INF/"
        + this.getContainingClass()!!.getQualifiedName()!!.replace('.', '/')
        + '/' + this.getName()
        + '.' + "sql"
        )
  }

  fun containsSqlFile(): Boolean {
    return findSqlFile() != null
  }

  /**
   * SQLファイルを検索する。
   */
  fun findSqlFile(): VirtualFile? {
    val scope = GlobalSearchScope.moduleRuntimeScope(getModule(), false)
    return ResourceFileUtil.findResourceFileInScope(getSqlFilePath(), this.getProject(), scope)
  }
}
