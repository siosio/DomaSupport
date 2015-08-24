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
  fun inspect(context: InspectionContext, daoMethod: PsiDaoMethod): Unit
}

/**
 * DAOクラスのインスペクションルール
 */
class DaoInspectionRule : Rule<PsiDaoMethod> {

  val rules: MutableList<DaoRule> = ArrayList()

  override fun inspect(context: InspectionContext, element: PsiDaoMethod) {
    rules.forEach {
      it.inspect(context, element)
    }
  }

  fun sql(required: Boolean) {
    val sql = Sql(required)
    rules.add(sql)
  }

  fun parameterRule(rule: List<PsiParameter>.(context: InspectionContext) -> Unit): ParameterRule {
    val parameterRule = ParameterRule(rule)
    rules.add(parameterRule)
    return parameterRule
  }
}

///**
// * DAOクラスの定義
// */
//class Dao : DaoInspectionRule() {
//
//  companion object {
//    fun dao(init: Dao.() -> Unit): Dao {
//      val dao = Dao()
//      dao.init()
//      return dao
//    }
//  }
//
//  fun sql(required: Boolean = false): Sql {
//    val sql = Sql(required)
//    rules.add(sql)
//    return sql
//  }
//
//  fun parameter(rule: ParameterInspectionRule.() -> Unit): ParameterInspectionRule {
//    val parameter = ParameterInspectionRule()
//    parameter.rule()
//    rules.add(parameter)
//    return parameter
//  }
//}
//
/**
 * SQLファイルの検査を行うクラス。
 */
class Sql(val required: Boolean) : DaoRule {
  override fun inspect(context: InspectionContext, daoMethod: PsiDaoMethod) {
    if (!required && !daoMethod.daoAnnotation.useSqlFile()) {
      return
    }
    if (!daoMethod.containsSqlFile()) {
      context.problemsHolder.registerProblem(
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
class ParameterRule(val rule: List<PsiParameter>.(context: InspectionContext) -> Unit) : DaoRule {
  override fun inspect(context: InspectionContext, daoMethod: PsiDaoMethod) {
    val params = daoMethod.getParameterList().getParameters().toList()
    params.rule(context)
  }
}

