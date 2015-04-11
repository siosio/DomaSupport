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
import siosio.doma.psi.PsiDaoMethod

/**
 * DomaのDAOのチェックを行うクラス。
 */
class DaoInspectionTool : BaseJavaLocalInspectionTool() {

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

        val daoType = DaoType.values().firstOrNull {
          AnnotationUtil.isAnnotated(method, it.annotationName, false)
        }
        if (daoType == null) {
          return;
        }

        val psiDaoMethod = PsiDaoMethod(method, daoType)

        val context = DaoMethodInspectionContext(problemsHolder, psiDaoMethod)
        context.doInspection()
      }
    }
  }
}

