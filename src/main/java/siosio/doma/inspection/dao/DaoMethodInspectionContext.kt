package siosio.doma.inspection.dao

import com.intellij.psi.PsiMethod
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiClass
import com.intellij.psi.util.PsiUtil
import siosio.doma.DaoType
import siosio.doma.inspection.dao.DaoInspectorFactory
import siosio.doma.psi.PsiDaoMethod

/**
 * DAOメソッドのインスペクション情報
 */
public class DaoMethodInspectionContext(
    val problemsHolder: ProblemsHolder,
    val method: PsiDaoMethod
) {

  fun doInspection() {
    val inspectors = DaoInspectorFactory.createDaoMethodInspector(method.daoType)
    inspectors.inspect(this)
  }
}