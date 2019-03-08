package siosio.doma.inspection.dao

import b.a.it
import com.intellij.codeInsight.intention.QuickFixFactory
import com.intellij.psi.util.PsiTypesUtil
import com.intellij.sql.type
import org.jetbrains.kotlin.idea.intentions.SpecifyTypeExplicitlyIntention
import org.jetbrains.kotlin.js.descriptorUtils.getJetTypeFqName
import org.jetbrains.kotlin.psi.KtParameter
import org.jetbrains.kotlin.psi.KtUserType
import siosio.doma.DomaBundle

val selectMethodRule =
        rule {
            sql(true)

            // SelectOptionsの検査
            parameterRule {
                message = "inspection.dao.multi-SelectOptions"
                quickFix = { QuickFixFactory.getInstance().createDeleteFix(it, DomaBundle.message("quick-fix.remove")) }
                rule = { filter { it.type.canonicalText == "org.seasar.doma.jdbc.SelectOptions" }.size in (0..1) }
                errorElements = {
                    it.parameterList.parameters.filter { it.type.canonicalText == "org.seasar.doma.jdbc.SelectOptions" }
                }
            }

            // strategyにSTREAMを指定した場合の検査
            parameterRule {
                message = "inspection.dao.function-strategy"
                errorElement = { psiDaoMethod -> psiDaoMethod.daoAnnotation.originalElement }
                rule = { psiDaoMethod ->
                    psiDaoMethod.daoAnnotation.run {
                        if (findAttributeValue("strategy")?.text?.contains("STREAM") == true) {
                            filter { PsiTypesUtil.getPsiClass(it.type)?.qualifiedName == "java.util.function.Function" }.size == 1
                        } else {
                            true
                        }
                    }
                }
            }
        }

private fun List<KtParameter>.filterSelectOptions(): List<KtParameter> {
    return filter {
        SpecifyTypeExplicitlyIntention.getTypeForDeclaration(it).getJetTypeFqName(false) == "org.seasar.doma.jdbc.SelectOptions"
    }
}

val kotlinSelectMethodRule = 
        kotlinRule { 
            sql(true)
            
            // SelectOptionsの検査
            parameterRule {
                message = "inspection.dao.multi-SelectOptions"
                quickFix = { QuickFixFactory.getInstance().createDeleteFix(it, DomaBundle.message("quick-fix.remove")) }
                rule = { filterSelectOptions().size in (0..1) }
                errorElements = {
                    it.valueParameters.filterSelectOptions()
                }
            }
        }
