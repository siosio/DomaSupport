package siosio.doma.inspection.dao

import com.intellij.psi.*
import siosio.doma.extension.*
import siosio.doma.inspection.dao.quickfix.*

val insertMethodRule =
        rule {
            sql(false)

            // 引数チェック
            parameterRule(parameterTypeCheck)

            returnRule {
                rule = block@{ daoMethod ->
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
                        message = "inspection.dao.mutable-insert-return-type"
                        type.isAssignableFrom(PsiType.INT)
                    } else {
                        // 引数がまともじゃない場合はとりあえずOKにする
                        true
                    }
                }
            }
        }

