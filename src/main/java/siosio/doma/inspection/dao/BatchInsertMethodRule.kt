package siosio.doma.inspection.dao

import com.intellij.openapi.project.*
import com.intellij.psi.*
import com.intellij.psi.impl.source.*
import com.intellij.psi.search.*
import siosio.doma.extension.*


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
                            isIterableType(daoMethod.project, type)
                                    && type.reference.typeParameters.first().isEntity()
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
        }

fun isIterableType(project: Project, psiType: PsiType): Boolean {
    val iterableType = PsiType.getTypeByName("java.lang.Iterable", project, GlobalSearchScope.allScope(project))
    return psiType.superTypes.any { iterableType.isAssignableFrom(it) }
}
