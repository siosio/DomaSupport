package siosio.doma.inspection.dao

import com.intellij.codeInsight.AnnotationUtil
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.compiler.RemoveElementQuickFix
import com.intellij.psi.PsiParameter
import siosio.doma.DomaBundle
import siosio.doma.inspection.dao.quickfix.CreateSqlFileQuickFix
import siosio.doma.psi.PsiDaoMethod
import java.util.ArrayList
import kotlin.properties.Delegates

/**
 * DAOクラスのインスペクションルール
 */
abstract class DaoInspectionRule {

  val rules: MutableList<DaoInspectionRule> = ArrayList()

  /**
   * 検査を実行する
   */
  open fun inspect(context: DaoMethodInspectionContext) {
    rules.forEach { it.inspect(context) }
  }
}

/**
 * DAOクラスの定義
 */
class Dao : DaoInspectionRule() {

  companion object {
    fun dao(init: Dao.() -> Unit): Dao {
      val dao = Dao()
      dao.init()
      return dao
    }
  }

  fun sql(required: Boolean = false): Sql {
    val sql = Sql(required)
    rules.add(sql)
    return sql
  }

  fun parameter(init: ParameterInspectionRule.() -> Unit): ParameterInspectionRule {
    val parameter = ParameterInspectionRule()
    rules.add(parameter)
    parameter.init()
    return parameter
  }
}

/**
 * SQLファイルの検査を行うクラス。
 */
class Sql(val required: Boolean) : DaoInspectionRule() {

  val sqlFileInspector = fun (context: DaoMethodInspectionContext) {

    if (context.method.containsSqlFile()) {
      context.problemsHolder.registerProblem(
          context.method.getNameIdentifier()!!,
          DomaBundle.message("inspection.dao.sql-not-found"),
          ProblemHighlightType.ERROR,
          CreateSqlFileQuickFix(context.method.getModule(), context.method.getSqlFilePath()))
    }
  }

  /**
   * 必須の場合は、必ず存在していること。
   * 任意の場合には、sqlFile属性がtrueの場合のみ存在していること
   */
  override fun inspect(context: DaoMethodInspectionContext) {
    if (!required && !context.method.daoAnnotation.useSqlFile()) {
      return
    }
    sqlFileInspector(context)
  }
}

/**
 * パラメータの検査を行うクラス
 */
class ParameterInspectionRule : DaoInspectionRule() {

  fun typeInspection(inspector: (List<PsiParameter>, DaoMethodInspectionContext) -> Unit) {
    class TypeInspectionRule : DaoInspectionRule() {
      override fun inspect(context: DaoMethodInspectionContext) {
        val params = context.method.getParameterList().getParameters().toList()
        inspector(params, context)
      }
    }
    rules.add(TypeInspectionRule())
  }
}

