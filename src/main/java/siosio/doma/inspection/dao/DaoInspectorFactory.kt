package siosio.doma.inspection.dao

import com.intellij.codeInsight.AnnotationUtil
import siosio.doma.extension.isEntity
import siosio.doma.sqlAnnotationName
import siosio.doma.sqlExperimentalAnnotationName

val parameterTypeCheck: ParameterRule.() -> Unit = {
    message = "inspection.dao.entity-param-not-found"
    rule = { dao ->
        when {
            dao.useSqlFile() -> true
            AnnotationUtil.isAnnotated(
                dao,
                listOf(sqlAnnotationName, sqlExperimentalAnnotationName),
                AnnotationUtil.CHECK_HIERARCHY
            ) -> true
            else ->
                when (size) {
                    1 -> first().isEntity()
                    else -> false
                }
        }
    }
}


val batchUpdateMethodRule =
    rule {
        sql(false)
    }

val batchDeleteMethodRule =
    rule {
        sql(false)
    }

val scriptMethodRule =
    rule {
        sql(true)
    }
