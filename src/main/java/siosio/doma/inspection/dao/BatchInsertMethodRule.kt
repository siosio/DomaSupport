package siosio.doma.inspection.dao

import com.intellij.codeInsight.daemon.impl.quickfix.*
import com.intellij.openapi.project.*
import com.intellij.psi.*
import com.intellij.psi.impl.source.*
import com.intellij.psi.search.*
import siosio.doma.extension.*
import siosio.doma.psi.*

val batchInsertMethodRule =
        rule {
            sql(false)

            // check parameter count
            parameterRule {
                message = "inspection.dao.batch-insert.param-size-error"
                rule = {
                    when (size) {
                        1 -> true
                        else -> false
                    }
                }
            }

            // check parameter type(sqlなし)
            parameterRule {
                message = "inspection.dao.batch-insert.param-error"
                rule = { daoMethod ->
                    if (size == 1 && daoMethod.useSqlFile().not()) {
                        firstOrNull()?.let {
                            val type = it.type as PsiClassReferenceType
                            isIterableType(daoMethod.project, type) && type.reference.typeParameters.first().isEntity()
                        } ?: true
                    } else {
                        true
                    }
                }
            }

            // check parameter type(sqlあり)
            parameterRule {
                message = "inspection.dao.batch-insert.use-sql.param-error"
                rule = { daoMethod ->
                    if (size == 1 && daoMethod.useSqlFile()) {
                        firstOrNull()?.type?.let {
                            isIterableType(daoMethod.project, it)
                        } ?: true
                    } else {
                        true
                    }
                }
            }

            // return type(not immutable entity)
            returnRule {
                message = "inspection.dao.batch-insert.mutable-insert-return-type"
                rule = block@{ daoMethod ->
                    quickFix = { MethodReturnTypeFix(daoMethod.psiMethod, PsiTypes.intType().createArrayType(), false) }
                    if (daoMethod.parameterCount() == 1) {
                        // 最初のパラメータの型パラメータを取得してチェックする
                        val typeParameter = getFirstParametersTypeParameter(daoMethod) ?: return@block true

                        if (typeParameter.isImmutableEntity().not()) {
                            type.isAssignableFrom(PsiTypes.intType().createArrayType())
                        } else {
                            true
                        }
                    } else {
                        true
                    }
                }
            }

            // return type( immutable entity)
            returnRule {
                message = "inspection.dao.batch-insert.immutable-insert-return-type"
                rule = block@{ daoMethod ->
                    if (daoMethod.parameterCount() == 1) {
                        // 最初のパラメータの型パラメータを取得してチェックする
                        val typeParameter = getFirstParametersTypeParameter(daoMethod) ?: return@block true

                        quickFix = {
                            MethodReturnTypeFix(
                                    daoMethod.psiMethod,
                                    PsiType.getTypeByName(
                                            "org.seasar.doma.jdbc.BatchResult<${typeParameter.canonicalText}>", project, resolveScope), false)
                        }

                        if (typeParameter.isImmutableEntity()) {
                            messageArgs = arrayOf(typeParameter.canonicalText)
                            type.isAssignableFrom(PsiType.getTypeByName("org.seasar.doma.jdbc.BatchResult", daoMethod.project, resolveScope))
                        } else {
                            true
                        }
                    } else {
                        true
                    }
                }
            }
        }

private fun getFirstParametersTypeParameter(daoMethod: PsiDaoMethod): PsiType? {
    val parameterType = daoMethod.parameterList.parameters.first().type as PsiClassReferenceType
    return parameterType.reference.typeParameters.firstOrNull()
}

private fun isIterableType(project: Project, psiType: PsiType): Boolean {
    val iterableType = PsiType.getTypeByName("java.lang.Iterable", project, GlobalSearchScope.allScope(project))
    return psiType.superTypes.any { iterableType.isAssignableFrom(it) }
}
