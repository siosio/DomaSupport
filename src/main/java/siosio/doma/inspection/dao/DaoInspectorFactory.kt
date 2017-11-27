package siosio.doma.inspection.dao

import com.intellij.codeInsight.*
import com.intellij.psi.util.*

val parameterTypeCheck: ParameterRule.() -> Unit = {
    message = "inspection.dao.entity-param-not-found"
    rule = { dao ->
        if (dao.useSqlFile()) {
            true
        } else {
            when (size) {
                1 -> {
                    PsiTypesUtil.getPsiClass(first().type)?.let {
                        AnnotationUtil.isAnnotated(it, "org.seasar.doma.Entity", false)
                    } ?: false
                }
                else -> false
            }
        }
    }
}

val insertMethodRule =
        rule {
            sql(false)
            
            // 引数チェック
            parameterRule(parameterTypeCheck)
        }

val updateMethodRule =
        rule {
            sql(false)
            
            // 引数チェック
            parameterRule(parameterTypeCheck)
        }

val deleteMethodRule =
        rule {
            sql(false)
            
            // 引数チェック
            parameterRule(parameterTypeCheck)
        }

val batchInsertMethodRule =
        rule {
            sql(false)
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
