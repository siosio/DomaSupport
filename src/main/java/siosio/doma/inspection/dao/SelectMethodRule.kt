package siosio.doma.inspection.dao

import com.intellij.codeInsight.intention.*
import com.intellij.psi.util.*
import org.jetbrains.kotlin.name.*
import org.jetbrains.kotlin.nj2k.types.*
import org.jetbrains.kotlin.psi.*
import siosio.doma.*

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

private val SELECT_OPTIONS_FQNAME = FqName("org.seasar.doma.jdbc.SelectOptions")
private fun List<KtParameter>.filterSelectOptions(): List<KtParameter> {
    return filter {
        it.typeFqName() == SELECT_OPTIONS_FQNAME
    }
}

