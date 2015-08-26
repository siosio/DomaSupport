package siosio.doma.inspection.dao

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.compiler.RemoveElementQuickFix
import siosio.doma.DaoType
import siosio.doma.DomaBundle
import siosio.doma.inspection.*

val selectMethodRule =
    rule {
      sql(true)

      parameterRule { context, method ->
        val selectOptions = filter {
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

val insertMethodRule =
    rule {
      sql(false)
    }

val updateMethodRule =
    rule {
      sql(false)
    }

val deleteMethodRule =
    rule {
      sql(false)
    }

val batchInsertMethodRule =
    rule {
      sql(false)
    }

