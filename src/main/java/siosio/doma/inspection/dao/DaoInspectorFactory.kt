package siosio.doma.inspection.dao

import com.intellij.psi.*
import siosio.doma.extension.*
import siosio.doma.inspection.dao.quickfix.*

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
            rule = block@ { daoMethod ->
                val entityParameter =
                    when {
                        daoMethod.useSqlFile() -> daoMethod.parameterList.parameters.firstOrNull { it.isEntity() }
                        else -> daoMethod.parameterList.parameters.firstOrNull()
                    } ?: return@block true

                if (entityParameter.isEntity() && entityParameter.isImmutableEntity()) {
                    message = "inspection.dao.immutable-insert-return-type"
                    quickFix = ImmutableEntityReturnTypeQuickFix()
                    messageArgs = arrayOf(entityParameter.type.canonicalText)
                    // Resultの型パラメータまでチェックする
                    if (type.isAssignableFrom(PsiType.getTypeByName("org.seasar.doma.jdbc.Result", daoMethod.project, resolveScope))) {
                        daoMethod.returnTypeElement?.innermostComponentReferenceElement?.typeParameters?.let { 
                            it.firstOrNull()?.isAssignableFrom(entityParameter.type)
                        } ?: false
                    } else {
                        false
                    }
                } else if (entityParameter.isEntity() && entityParameter.isImmutableEntity().not()) {
                    quickFix = null
                    message = "inspection.dao.mutable-insert-return-type"
                    type.isAssignableFrom(PsiType.INT)
                } else {
                    // 引数がまともじゃない場合はとりあえずOKにする
                    true
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
