package siosio.doma.inspection.dao

import com.intellij.codeInsight.intention.*
import com.intellij.psi.util.*
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
                        if (findAttributeValue("strategy")?.text?.contains("STREAM") != true) {
                            true
                        } else {
                            filter { PsiTypesUtil.getPsiClass(it.type)?.qualifiedName == "java.util.function.Function" }.size == 1
                        }
                    }
                }
            }
        }
