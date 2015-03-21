package siosio.doma.inspection.dao

import com.intellij.psi.PsiMethod
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiClass
import com.intellij.psi.util.PsiUtil
import siosio.doma.DaoType
import siosio.doma.DomaUtils

/**
 * DAOメソッドのインスペクション情報
 */
public class DaoMethodInspectionContext(
    val problemsHolder: ProblemsHolder,
    val method: PsiMethod
) {
  val psiClass: PsiClass
  val daoType: DaoType

  init {
    psiClass = method.getContainingClass()!!
    daoType = DomaUtils.toDaoType(method)!!
  }

  fun doInspection() {
    val inspectors = daoInspectors.get(daoType) ?: listOf()
    inspectors.forEach { it.doInspection(this)}
  }

  companion object {
    val daoInspectors = mapOf(
        DaoType.SELECT to listOf(
            SqlFileInspector("sql")
        ),
        DaoType.INSERT to listOf(
            OptionalSqlFileInspector("sql")
        ),
        DaoType.UPDATE to listOf(
            OptionalSqlFileInspector("sql")
        ),
        DaoType.DELETE to listOf(
            OptionalSqlFileInspector("sql")
        ),
        DaoType.BATCH_INSERT to listOf(
            OptionalSqlFileInspector("sql")
        )
    )
  }
}