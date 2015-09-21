package siosio.doma.refactoring

import com.intellij.codeInsight.*
import com.intellij.psi.*
import com.intellij.refactoring.listeners.*
import com.intellij.refactoring.rename.*
import com.intellij.usageView.*
import siosio.doma.*
import siosio.doma.psi.*

public class DaoMethodRenameProcessor : RenameJavaMethodProcessor() {

  override fun canProcessElement(method: PsiElement): Boolean {
    val canProcessElement = super.canProcessElement(method)
    return if (canProcessElement) {
      DaoType.values().firstOrNull {
        AnnotationUtil.isAnnotated(method as PsiMethod, it.annotationName, false)
      }?.let {
        PsiDaoMethod(method as PsiMethod, it).findSqlFile()
      }?.let {
        true
      } ?: false
    } else {
      false
    }
  }

  override fun renameElement(element: PsiElement, name: String, p2: Array<out UsageInfo>?, p3: RefactoringElementListener) {
    val daoType = DaoType.values().firstOrNull {
      AnnotationUtil.isAnnotated(element as PsiMethod, it.annotationName, false)
    }
    daoType?.let {
      val psiDaoMethod = PsiDaoMethod(element as PsiMethod, it)
      psiDaoMethod.findSqlFile()
    }?.let {
      val extension = it.getExtension()
      it.rename(it, "${name}.${extension}")
    }
    super.renameElement(element, name, p2, p3)
  }
}