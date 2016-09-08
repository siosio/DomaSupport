package siosio.doma.inspection.dao

import com.intellij.codeInsight.*
import com.intellij.codeInspection.*
import com.intellij.psi.*
import siosio.doma.*
import siosio.doma.psi.*

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

        val daoType = DaoType.valueOf(method)

        daoType?.let {
          val psiDaoMethod = PsiDaoMethod(method, it)
          val rule = psiDaoMethod.daoType.rule
          rule.inspect(problemsHolder, psiDaoMethod)
        }
      }
    }
  }
}

