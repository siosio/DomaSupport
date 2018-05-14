package siosio.doma.inspection.dao

import com.intellij.codeInsight.daemon.impl.quickfix.*
import com.intellij.psi.*
import siosio.doma.extension.*
import siosio.doma.psi.*

val insertMethodRule =
        rule {
            sql(false)

            // 引数チェック
            parameterRule(parameterTypeCheck)

            // return type check(immutable entity)
            returnRule {
                message = "inspection.dao.immutable-insert-return-type"
                rule = block@{ daoMethod ->
                    val parameterType = getEntityParameterType(daoMethod) ?: return@block true

                    messageArgs = arrayOf(parameterType.canonicalText)
                    quickFix = {
                        MethodReturnTypeFix(daoMethod.psiMethod, PsiType.getTypeByName("org.seasar.doma.jdbc.Result<${parameterType.canonicalText}>", project, resolveScope), false)
                    }

                    // parameterがentity && immutableの場合だけチェックを行う
                    // @formatter:off
                    if (parameterType.isEntity() && parameterType.isImmutableEntity()) {
                        type.isAssignableFrom(PsiType.getTypeByName("org.seasar.doma.jdbc.Result", daoMethod.project, resolveScope))
                                && (daoMethod.returnTypeElement?.innermostComponentReferenceElement?.typeParameters?.let {
                                    it.firstOrNull()?.isAssignableFrom(parameterType)
                                } == true)
                    } else {
                        true
                    }
                    // @formatter:on
                }
            }

            // return type check(mutable entity)
            returnRule {
                rule = block@{ daoMethod ->
                    val parameteType = getEntityParameterType(daoMethod) ?: return@block true

                    if (parameteType.isEntity() && parameteType.isImmutableEntity().not()) {
                        quickFix = { MethodReturnTypeFix(daoMethod.psiMethod, PsiType.INT, false) }
                        message = "inspection.dao.mutable-insert-return-type"
                        type.isAssignableFrom(PsiType.INT)
                    } else {
                        // 引数がまともじゃない場合はとりあえずOKにする
                        true
                    }
                }
            }
        }


private fun getEntityParameterType(daoMethod: PsiDaoMethod): PsiType? {
    return when {
        daoMethod.useSqlFile() -> daoMethod.parameterList.parameters.firstOrNull { it.isEntity() }
        else -> daoMethod.parameterList.parameters.firstOrNull()
    }?.type
}
