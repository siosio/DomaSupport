package siosio.doma.inspection.dao

import com.intellij.codeInspection.*
import com.intellij.psi.*
import siosio.doma.*
import siosio.doma.psi.*

/**
 * DomaのDAOのチェックを行うクラス。
 */
class DaoInspectionTool : BaseJavaLocalInspectionTool() {

    override fun getDisplayName(): String = DomaBundle.message("inspection.dao-inspection")

    override fun isEnabledByDefault(): Boolean = true

    override fun getGroupDisplayName(): String = "Doma"

    override fun getShortName(): String = "DaoInspection"

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

