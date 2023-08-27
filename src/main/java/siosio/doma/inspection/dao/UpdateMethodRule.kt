package siosio.doma.inspection.dao

import com.intellij.codeInsight.*
import com.intellij.codeInsight.daemon.impl.quickfix.*
import com.intellij.psi.*
import com.intellij.psi.util.*
import org.jetbrains.kotlin.idea.references.*
import org.jetbrains.kotlin.idea.util.*
import org.jetbrains.kotlin.name.*
import org.jetbrains.kotlin.psi.*
import siosio.doma.*
import siosio.doma.extension.*
import siosio.doma.psi.*

// return type check(immutable entity)
val updateMethodWithImmutableEntityReturnRule: ReturnRule.() -> Unit = {
    message = "inspection.dao.immutable-update-return-type"
    rule = block@{ daoMethod ->
        val parameterType = getEntityParameterType(daoMethod) ?: return@block true

        messageArgs = arrayOf(parameterType.canonicalText)
        quickFix = {
            MethodReturnTypeFix(
                daoMethod.psiMethod,
                PsiType.getTypeByName("org.seasar.doma.jdbc.Result<${parameterType.canonicalText}>", project, resolveScope),
                false
            )
        }

        // parameterがentity && immutableの場合だけチェックを行う
        if (parameterType.isEntity() && parameterType.isImmutableEntity()) {
            type.isAssignableFrom(PsiType.getTypeByName("org.seasar.doma.jdbc.Result", daoMethod.project, resolveScope))
                    && (daoMethod.returnTypeElement?.innermostComponentReferenceElement?.typeParameters?.let {
                it.firstOrNull<PsiType?>()?.isAssignableFrom(parameterType)
            } == true)
        } else {
            true
        }
    }
}

private val updateMethodWithMutableEntityReturnRule: ReturnRule.() -> Unit = {
    message = "inspection.dao.mutable-update-return-type"
    rule = block@{ daoMethod ->
        quickFix = { MethodReturnTypeFix(daoMethod.psiMethod, PsiTypes.intType(), false) }

        val parameterType = getEntityParameterType(daoMethod) ?: return@block true
        if (parameterType.isEntity() && parameterType.isImmutableEntity().not()) {
            type.isAssignableFrom(PsiTypes.intType())
        } else {
            // 引数がまともじゃない場合はとりあえずOKにする
            true
        }
    }
}


private fun getEntityParameterType(daoMethod: PsiDaoMethod): PsiType? {
    return when {
        daoMethod.useSqlFile() -> daoMethod.parameterList.parameters.firstOrNull { it.isEntity() }
        else -> daoMethod.parameterList.parameters.firstOrNull()
    }?.type
}

private val commonRule: DaoInspectionRule.() -> Unit = {
    sql(false)

    // 引数チェック
    parameterRule(parameterTypeCheck)

    // return type check(immutable entity)
    returnRule { apply(updateMethodWithImmutableEntityReturnRule) }

    // return type check(mutable entity)
    returnRule { apply(updateMethodWithMutableEntityReturnRule) }

    returnRule {
        message = "inspection.dao.mutable-update-return-type"
        rule = block@{ daoMethod ->
            quickFix = { MethodReturnTypeFix(daoMethod.psiMethod, PsiTypes.intType(), false) }
            if (daoMethod.useSqlFile() && getEntityParameterType(daoMethod) == null) {
                type.isAssignableFrom(PsiTypes.intType())
            } else {
                true
            }
        }
    }
}


val updateMethodRule =
    rule {
        apply(commonRule)
    }


val insertMethodRule =
    rule {
        apply(commonRule)
    }

//---------------------------------------------kotlin

val kotlinUpdateMethodRule =
    kotlinRule {
        sql(false)
    }

val kotlinInsertMethodRule =
    kotlinRule {
        sql(false)

        parameterRule {
            message = "inspection.dao.entity-param-not-found"
            rule = { dao ->
                when {
                    dao.useSqlFile() -> true
                    else ->
                        when (size) {
                            1 -> {
                                val param = PsiTreeUtil.findChildOfType(first(), KtNameReferenceExpression::class.java)?.mainReference?.resolve()
                                when (param) {
                                    is PsiClass -> AnnotationUtil.isAnnotated(
                                        param,
                                        listOf(entityAnnotationName),
                                        AnnotationUtil.CHECK_TYPE
                                    )
                                    is KtClass -> {
                                        param.findAnnotation(FqName(entityAnnotationName)) != null
                                    }
                                    else -> false
                                }
                            }
                            else -> false
                        }
                }
            }
        }
    }

val kotlinDeleteMethodRule =
    kotlinRule {
        sql(false)
    }
