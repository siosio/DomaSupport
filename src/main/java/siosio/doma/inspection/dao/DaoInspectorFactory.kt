package siosio.doma.inspection.dao

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.compiler.RemoveElementQuickFix
import siosio.doma.DaoType
import siosio.doma.DomaBundle

class DaoInspectorFactory {
  companion object {

    fun createDaoMethodInspector(daoType: DaoType): Dao {
      when (daoType) {
        DaoType.SELECT ->
          return createSelectMethod()
        DaoType.INSERT ->
          return createInsertMethod()
        DaoType.UPDATE ->
          return createUpdateMethod()
        DaoType.DELETE ->
          return createDeleteMethod()
        DaoType.BATCH_INSERT ->
          return createBatchInsertMethod()
        else -> throw IllegalArgumentException("invalid dao type.")
      }
    }

    private fun createSelectMethod() =
        Dao.dao {
          sql(required = true)

          parameter {
            typeInspection { params, context ->
              val selectOptions = params.filter {
                "org.seasar.doma.jdbc.SelectOptions".equals(it.getType().getCanonicalText())
              }

              if (selectOptions.size() !in 0..1) {
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
        }

    private fun createInsertMethod() =
        Dao.dao {
          sql(required = false)
          parameter {
          }
        }

    private fun createUpdateMethod() =
        Dao.dao {
          sql(required = false)
        }

    private fun createDeleteMethod() =
        Dao.dao {
          sql(required = false)
        }

    private fun createBatchInsertMethod() =
        Dao.dao {
          sql(required = false)
        }
  }
}
