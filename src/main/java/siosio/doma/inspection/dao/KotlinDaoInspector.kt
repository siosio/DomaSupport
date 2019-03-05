package siosio.doma.inspection.dao

import com.intellij.codeInspection.*
import org.jetbrains.kotlin.idea.util.findAnnotation
import org.jetbrains.kotlin.name.FqName
import siosio.doma.*
import siosio.doma.inspection.*
import siosio.doma.inspection.dao.quickfix.*
import siosio.doma.psi.*
import java.util.*

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

    override fun inspect(problemsHolder: ProblemsHolder, element: PsiDaoFunction) {
        rules.forEach {
            it.inspect(problemsHolder, element)
        }
    }

    fun sql(required: Boolean) {
        val sql = KotlinSql(required)
        rules.add(sql)
    }
}

/**
 * SQLファイルの検査を行うクラス。
 */
class KotlinSql(private val required: Boolean) : KotlinDaoRule {
    override fun inspect(problemsHolder: ProblemsHolder, daoFunction: PsiDaoFunction) {
        if ((!required && !daoFunction.useSqlFile())
            || daoFunction.getModule() == null 
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

