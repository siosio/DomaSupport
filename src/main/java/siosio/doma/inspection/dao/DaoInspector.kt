package siosio.doma.inspection.dao

import com.intellij.codeInspection.*
import com.intellij.openapi.editor.*
import com.intellij.psi.*
import siosio.doma.*
import siosio.doma.inspection.*
import siosio.doma.inspection.dao.quickfix.*
import siosio.doma.psi.*
import java.util.*

fun rule(rule: DaoInspectionRule.() -> Unit): DaoInspectionRule {
  val daoInspectionRule = DaoInspectionRule()
  daoInspectionRule.rule()
  return daoInspectionRule
}

interface DaoRule {
  fun inspect(problemsHolder: ProblemsHolder, daoMethod: PsiDaoMethod): Unit
}

/**
 * DAOクラスのインスペクションルール
 */
class DaoInspectionRule : Rule<PsiDaoMethod> {

  val rules: MutableList<DaoRule> = ArrayList()

  override fun inspect(problemsHolder: ProblemsHolder, element: PsiDaoMethod) {
    rules.forEach {
      it.inspect(problemsHolder, element)
    }
  }

  fun sql(required: Boolean) {
    val sql = Sql(required)
    rules.add(sql)
  }

  fun parameterRule(rule: List<PsiParameter>.(problemsHolder: ProblemsHolder, daoMethod: PsiDaoMethod) -> Unit): ParameterRule {
    val parameterRule = ParameterRule(rule)
    rules.add(parameterRule)
    return parameterRule
  }
}

/**
 * SQLファイルの検査を行うクラス。
 */
class Sql(val required: Boolean) : DaoRule {
  override fun inspect(problemsHolder: ProblemsHolder, daoMethod: PsiDaoMethod) {
    if (!required && !daoMethod.daoAnnotation.useSqlFile()) {
      return
    }
    if (!daoMethod.containsSqlFile()) {
      problemsHolder.registerProblem(
          daoMethod.getNameIdentifier()!!,
          DomaBundle.message("inspection.dao.sql-not-found"),
          ProblemHighlightType.ERROR,
          CreateSqlFileQuickFix(daoMethod.getModule(), daoMethod.getSqlFilePath()))
    }
  }
}

/**
 * パラメータの検査を行うクラス
 */
class ParameterRule(val rule: List<PsiParameter>.(problemsHolder: ProblemsHolder, daoMethod:PsiDaoMethod) -> Unit) : DaoRule {
  override fun inspect(problemsHolder: ProblemsHolder, daoMethod: PsiDaoMethod) {
    val params = daoMethod.getParameterList().getParameters().toList()
    params.rule(problemsHolder, daoMethod)
  }
}

