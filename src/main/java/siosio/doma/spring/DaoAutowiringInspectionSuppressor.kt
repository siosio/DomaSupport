package siosio.doma.spring

import com.intellij.codeInsight.*
import com.intellij.codeInspection.*
import com.intellij.psi.*
import com.intellij.psi.impl.source.*

class DaoAutowiringInspectionSuppressor : InspectionSuppressor {

    override fun isSuppressedFor(element: PsiElement, toolId: String): Boolean {
        if (toolId !== "SpringJavaAutowiringInspection") {
            return false
        }
        val context = element.context
        return when (context) {
            is PsiParameter -> isDaoRepositoryType(context.type)
            is PsiField -> isDaoRepositoryType(context.type)
            else -> false
        }
    }

    override fun getSuppressActions(element: PsiElement?, toolId: String): Array<SuppressQuickFix> {
        return emptyArray()
    }

    private fun isDaoRepositoryType(type: PsiType): Boolean {
        return when (type) {
            is PsiClassReferenceType ->
                type.rawType().resolve()?.let {
                    AnnotationUtil.isAnnotated(it, "org.seasar.doma.Dao", AnnotationUtil.CHECK_TYPE)
                            && AnnotationUtil.isAnnotated(it, "org.seasar.doma.boot.ConfigAutowireable", AnnotationUtil.CHECK_TYPE)
                } ?: false
            else -> false
        }
    }
}
