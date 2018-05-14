package siosio.doma.inspection.dao

import com.intellij.codeInspection.*
import com.intellij.database.scripting.*
import com.intellij.psi.*
import groovy.xml.Entity.*
import siosio.doma.*
import siosio.doma.inspection.*
import siosio.doma.inspection.dao.quickfix.*
import siosio.doma.psi.*
import java.util.*

fun rule(rule: DaoInspectionRule.() -> Unit): DaoInspectionRule {
    val daoInspectionRule = DaoInspectionRule()
    daoInspectionRule.rule()
    return daoInspectionRule
}

interface DaoRule {
    fun inspect(problemsHolder: ProblemsHolder, daoMethod: PsiDaoMethod): Unit
}

/**
 * DAOクラスのインスペクションルール
 */
class DaoInspectionRule : Rule<PsiDaoMethod> {

    val rules: MutableList<DaoRule> = ArrayList()

    override fun inspect(problemsHolder: ProblemsHolder, element: PsiDaoMethod) {
        rules.forEach {
            it.inspect(problemsHolder, element)
        }
    }

    fun sql(required: Boolean) {
        val sql = Sql(required)
        rules.add(sql)
    }

    fun parameterRule(init: ParameterRule.() -> Unit) {
        val parameterRule = ParameterRule().apply(init)
        if (parameterRule.message == null) {
            throw IllegalArgumentException("message must be set")
        }
        rules.add(parameterRule)
    }

    fun returnRule(init: ReturnRule.() -> Unit) {
        val returnTypeRule = ReturnRule()
                .apply(init)
        rules.add(returnTypeRule)
    }
}

/**
 * SQLファイルの検査を行うクラス。
 */
class Sql(private val required: Boolean) : DaoRule {
    override fun inspect(problemsHolder: ProblemsHolder, daoMethod: PsiDaoMethod) {
        if (!required && !daoMethod.useSqlFile()) {
            return
        }
        if (!daoMethod.containsSqlFile()) {
            problemsHolder.registerProblem(
                    daoMethod.nameIdentifier!!,
                    DomaBundle.message("inspection.dao.sql-not-found"),
                    ProblemHighlightType.ERROR,
                    CreateSqlFileQuickFixFactory.create(daoMethod))
        }
    }
}

/**
 * パラメータの検査を行うクラス
 */
class ParameterRule : DaoRule {
    var message: String? = null
    var errorElement: (PsiDaoMethod) -> PsiElement = { psiDaoMethod -> psiDaoMethod.parameterList }
    var errorElements: (PsiDaoMethod) -> List<PsiElement> = { psiDaoMethod -> listOf(errorElement.invoke(psiDaoMethod)) }
    var rule: List<PsiParameter>.(PsiDaoMethod) -> Boolean = { _ -> true }
    var quickFix: ((PsiElement) -> LocalQuickFix)? = null
    override fun inspect(problemsHolder: ProblemsHolder, daoMethod: PsiDaoMethod) {
        val params = daoMethod.parameterList.parameters.toList()
        if (!params.rule(daoMethod)) {
            val register: (PsiElement) -> Unit = when (quickFix) {
                null -> { el -> problemsHolder.registerProblem(el, DomaBundle.message(message!!)) }
                else -> { el -> problemsHolder.registerProblem(el, DomaBundle.message(message!!), quickFix!!.invoke(el)) }
            }
            errorElements.invoke(daoMethod)
                    .forEach(register)
        }
    }
}

/**
 * 戻り値の検査を行うクラス
 */
class ReturnRule() : DaoRule {
    var message: String? = null
    var errorElement: (PsiDaoMethod) -> PsiElement = { psiDaoMethod -> psiDaoMethod.returnTypeElement!! }
    var rule: PsiTypeElement.(PsiDaoMethod) -> Boolean = { _ -> true }
    var quickFix: (() -> LocalQuickFix)? = null
    var messageArgs: Array<String> = emptyArray()

    override fun inspect(problemsHolder: ProblemsHolder, daoMethod: PsiDaoMethod) {
        val returnTypeElement = daoMethod.returnTypeElement ?: return
        if (returnTypeElement.rule(daoMethod).not()) {
            when(quickFix) {
                null -> problemsHolder.registerProblem(errorElement.invoke(daoMethod), DomaBundle.message(message!!, *messageArgs))
                else -> problemsHolder.registerProblem(errorElement.invoke(daoMethod), DomaBundle.message(message!!, *messageArgs), quickFix!!.invoke())
            }
            
        }
    }
}

