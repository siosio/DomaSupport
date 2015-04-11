package siosio.doma.inspection.dao

import com.intellij.codeInsight.AnnotationUtil
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.compiler.RemoveElementQuickFix
import siosio.doma.DomaBundle
import siosio.doma.inspection.dao.quickfix.CreateSqlFileQuickFix
import java.util.ArrayList
import kotlin.properties.Delegates

abstract class Rule {
  protected val rules: MutableList<Rule> = ArrayList()
  open fun inspect(context: DaoMethodInspectionContext) {
    rules.forEach { it.inspect(context) }
  }
}

class Dao : Rule() {
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

  fun parameter(init: Parameter.() -> Unit): Parameter {
    val parameter = Parameter()
    rules.add(parameter)
    parameter.init()
    return parameter
  }
}

/**
 * SQLファイルの検査を行うクラス。
 */
class Sql(val required: Boolean) : Rule() {

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

class Parameter : Rule() {

  class Type(val type: String, val min: Int = 0, val max: Int = 0) : Rule() {
    override fun inspect(context: DaoMethodInspectionContext) {
      val selectOptions = context.method.getParameterList().getParameters().filter {
        type.equals(it.getType().getCanonicalText())
      }

      if (selectOptions !in min..max) {
        selectOptions.forEach {
          context.problemsHolder.registerProblem(
              it,
              DomaBundle.message("inspection.dao.multi-SelectOptions"),
              ProblemHighlightType.ERROR,
              RemoveElementQuickFix(DomaBundle.message("quick-fix.remove", it.getName())))
        }
      }
    }
  }

  fun type(clazz: String, min: Int = 0, max: Int = 0): Type {
    val type = Type(clazz, min, max)
    rules.add(type)
    return type
  }
}

