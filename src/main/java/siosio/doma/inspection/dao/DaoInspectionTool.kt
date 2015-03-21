package siosio.doma.inspection.dao

import com.intellij.codeInsight.AnnotationUtil
import com.intellij.codeInspection.BaseJavaLocalInspectionTool

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiMethod
import siosio.doma.DaoType
import siosio.doma.DomaBundle
import siosio.doma.DomaUtils

/**
 * DomaのDAOのチェックを行うクラス。
 */
class DaoInspectionTool : BaseJavaLocalInspectionTool() {

  fun PsiMethod.isDaoMethod(): Boolean {
    if (!this.isValid()) {
      return false
    }
    val psiClass = this.getContainingClass()
    if (psiClass == null) {
      return false
    }
    if (!AnnotationUtil.isAnnotated(psiClass, DomaUtils.DAO_ANNOTATION_NAME, false)) {
      return false
    }
    val daoType = DomaUtils.toDaoType(this)
    return daoType != null
  }

  override fun getDisplayName(): String {
    return DomaBundle.message("inspection.dao-inspection")
  }

  override fun isEnabledByDefault(): Boolean {
    return true
  }

  override fun getGroupDisplayName(): String {
    return "Doma"
  }

  override fun getShortName(): String {
    return "DaoInspection"
  }

  override fun buildVisitor(problemsHolder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {

    return object : JavaElementVisitor() {

      override fun visitMethod(method: PsiMethod) {
        super.visitMethod(method)
        if (!method.isDaoMethod()) {
          return
        }
        val context = DaoMethodInspectionContext(problemsHolder, method)
        context.doInspection()
      }
    }
  }
}

