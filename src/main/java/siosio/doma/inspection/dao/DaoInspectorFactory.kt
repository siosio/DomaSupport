package siosio.doma.inspection.dao

import com.intellij.psi.*
import com.intellij.psi.util.*
import siosio.doma.extension.*

val parameterTypeCheck: ParameterRule.() -> Unit = {
    message = "inspection.dao.entity-param-not-found"
    rule = { dao ->
        when {
            dao.useSqlFile() -> true
            else ->
                when (size) {
                    1 -> first().isEntity()
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

        returnRule {
            rule = { daoMethod ->
                when {
                    daoMethod.useSqlFile() -> true
                    else -> {
                        val firstParam = daoMethod.parameterList.parameters.firstOrNull()
                        when {
                            firstParam?.isEntity() == true && firstParam.isImmutableEntity() -> {
                                message = "inspection.dao.immutable-insert-return-type"
                                // 引数がvalidじゃない場合はOKとする
                                type.isAssignableFrom(PsiType.getTypeByName("org.seasar.doma.jdbc.Result", daoMethod.project, resolveScope))
                            }
                            firstParam?.isEntity() == true && !firstParam.isImmutableEntity() -> {
                                message = "inspection.dao.mutable-insert-return-type"
                                type.isAssignableFrom(PsiType.INT)
                            }
                            // 引数がまともじゃない場合はOK
                            else -> true
                        }
                    }
                }
            }
        }
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
