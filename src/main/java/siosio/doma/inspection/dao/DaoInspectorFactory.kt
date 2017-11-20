package siosio.doma.inspection.dao

import com.intellij.codeInsight.*
import com.intellij.psi.util.*

val insertMethodRule =
        rule {
            sql(false)
            
            // 引数チェック
            parameterRule {
                message = "inspection.dao.entity-param-not-found"
                rule = {
                    when (size) {
                        1 -> {
                            val type = first().type
                            PsiTypesUtil.getPsiClass(type)?.let {
                                AnnotationUtil.isAnnotated(it, "org.seasar.doma.Entity", false)
                            } ?: false
                        }
                        else -> false
                    }
                }
            }
        }

val updateMethodRule =
        rule {
            sql(false)
        }

val deleteMethodRule =
        rule {
            sql(false)
        }

val batchInsertMethodRule =
        rule {
            sql(false)
        }

val batchUpdateMethodRule =
        rule {
            sql(false)
        }

val batchDeleteMethodRule =
        rule {
            sql(false)
        }

val scriptMethodRule =
        rule {
            sql(true)
        }
