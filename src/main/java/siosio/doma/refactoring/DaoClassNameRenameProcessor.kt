package siosio.doma.refactoring

import com.intellij.codeInsight.*
import com.intellij.psi.*
import com.intellij.refactoring.listeners.*
import com.intellij.refactoring.rename.*
import com.intellij.usageView.*
import siosio.doma.*
import siosio.doma.extension.*

class DaoClassNameRenameProcessor : RenameJavaClassProcessor() {

  override fun canProcessElement(element: PsiElement): Boolean {
    return if (super.canProcessElement(element)) {
      return AnnotationUtil.isAnnotated(element as PsiClass, "org.seasar.doma.Dao", false)
    } else {
      false
    }
  }

  override fun renameElement(element: PsiElement, newName: String, usages: Array<out UsageInfo>?, listener: RefactoringElementListener?) {
    val psiClass = element as PsiClass
    getModule(element.project, element).findSqlFileFromRuntimeScope(
        toSqlFilePath(element.qualifiedName!!.replace(".", "/")))?.let { sqlDir ->
      if (sqlDir.name == psiClass.name) {
        sqlDir.rename(sqlDir, newName)
      }
    }
    super.renameElement(element, newName, usages, listener)
  }
}
