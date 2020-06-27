package siosio.doma.inspection.dao

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.idea.util.findAnnotation
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtParameter
import siosio.doma.DomaBundle
import siosio.doma.inspection.Rule
import siosio.doma.inspection.dao.quickfix.CreateSqlFileQuickFixFactory
import siosio.doma.psi.PsiDaoFunction
import java.util.ArrayList

fun kotlinRule(rule: KotlinDaoInspectionRule.() -> Unit): KotlinDaoInspectionRule {
    val daoInspectionRule = KotlinDaoInspectionRule()
    daoInspectionRule.rule()
    return daoInspectionRule
}

interface KotlinDaoRule : Rule<PsiDaoFunction> {
}

/**
 * DAOクラスのインスペクションルール
 */
class KotlinDaoInspectionRule : Rule<PsiDaoFunction> {

    val rules: MutableList<KotlinDaoRule> = ArrayList()

    override fun inspect(problemsHolder: ProblemsHolder, psiFunction: PsiDaoFunction) {
        rules.forEach {
            it.inspect(problemsHolder, psiFunction)
        }
    }

    fun sql(required: Boolean) {
        val sql = KotlinSql(required)
        rules.add(sql)
    }
    
    fun parameterRule(init: KotlinParameterRule.() -> Unit) {
        val parameterRule = KotlinParameterRule().apply(init)
        if (parameterRule.message == null) {
            throw IllegalArgumentException("message must be set")
        }
        rules.add(parameterRule)
    }
}

/**
 * SQLファイルの検査を行うクラス。
 */
class KotlinSql(private val required: Boolean) : KotlinDaoRule {
    override fun inspect(problemsHolder: ProblemsHolder, daoFunction: PsiDaoFunction) {
        if ((!required && !daoFunction.useSqlFile())
            || daoFunction.getModule() == null 
            || daoFunction.psiFunction.findAnnotation(FqName("org.seasar.doma.Sql")) != null
            || daoFunction.psiFunction.findAnnotation(FqName("org.seasar.doma.experimental.Sql")) != null) {
            return
        }
        if (!daoFunction.containsSqlFile()) {
            problemsHolder.registerProblem(
                    daoFunction.nameIdentifier!!,
                    DomaBundle.message("inspection.dao.sql-not-found"),
                    ProblemHighlightType.ERROR,
                    CreateSqlFileQuickFixFactory.create(daoFunction)
                    )
        }
    }
}

/**
 * パラメータの検査を行うクラス
 */
class KotlinParameterRule : KotlinDaoRule {
    var message: String? = null
    var errorElement: (PsiDaoFunction) -> List<PsiElement> = { psiDaoFunction -> psiDaoFunction.valueParameters }
    var errorElements: (PsiDaoFunction) -> List<PsiElement> = { psiDaoFunction -> errorElement.invoke(psiDaoFunction) }
    var rule: List<KtParameter>.(PsiDaoFunction) -> Boolean = { _ -> true }
    var quickFix: ((PsiElement) -> LocalQuickFix)? = null
    override fun inspect(problemsHolder: ProblemsHolder, psiDaoFunction: PsiDaoFunction) {
        val params = psiDaoFunction.getValueParameters()
        if (!params.rule(psiDaoFunction)) {
            val register: (PsiElement) -> Unit = when (quickFix) {
                null -> { el -> problemsHolder.registerProblem(el, DomaBundle.message(message!!)) }
                else -> { el -> problemsHolder.registerProblem(el, DomaBundle.message(message!!), quickFix!!.invoke(el)) }
            }
            errorElements.invoke(psiDaoFunction)
                    .forEach(register)
        }
    }
}
