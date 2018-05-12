package siosio.doma.inspection.dao

import com.intellij.openapi.project.*
import com.intellij.psi.*
import com.intellij.psi.search.*
import com.intellij.psi.util.*


val batchInsertMethodRule =
        rule {
            sql(false)
            parameterRule {
                message = "inspection.dao.batch-insert.param-error"
                rule = { dao ->
                    when (size) {
                        1 -> {
                            first().type.let {
                                isIterableType(dao.project, it)
                            }
                        }
                        else -> false
                    }
                }
            }
        }

fun isIterableType(project: Project, psiType: PsiType): Boolean {
    val iterableType = PsiType.getTypeByName("java.lang.Iterable", project, GlobalSearchScope.allScope(project))
    return psiType.superTypes.any { iterableType.isAssignableFrom(it) }
}
