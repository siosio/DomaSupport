package siosio.doma.refactoring

import com.intellij.codeInsight.*
import com.intellij.psi.*
import com.intellij.refactoring.listeners.*
import com.intellij.refactoring.rename.*
import com.intellij.usageView.*
import siosio.doma.*
import siosio.doma.psi.*

public class DaoMethodRenameProcessor : RenameJavaMethodProcessor() {

  override fun canProcessElement(element: PsiElement): Boolean {
    val canProcessElement = super.canProcessElement(element)
    return if (canProcessElement) {
      createPsiDaoMethod(element as PsiMethod)?.let {
        it.findSqlFile()
      }?.let {
        true
      } ?: false
    } else {
      false
    }
  }

  override fun renameElement(element: PsiElement, name: String, p2: Array<out UsageInfo>?, p3: RefactoringElementListener?) {
    createPsiDaoMethod(element as PsiMethod)?.let {
      it.findSqlFile()
    }?.let {
      val extension = it.getExtension()
      it.rename(it, "${name}.${extension}")
    }
    super.renameElement(element, name, p2, p3)
  }

  private fun createPsiDaoMethod(method: PsiMethod): PsiDaoMethod? {
    return DaoType.values().firstOrNull {
      AnnotationUtil.isAnnotated(method, it.annotationName, false)
    }?.let {
      PsiDaoMethod(method, it)
    } ?: null
  }
}