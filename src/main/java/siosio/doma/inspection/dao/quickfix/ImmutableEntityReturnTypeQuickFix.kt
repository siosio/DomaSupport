package siosio.doma.inspection.dao.quickfix

import com.intellij.codeInspection.*
import com.intellij.openapi.application.*
import com.intellij.openapi.command.*
import com.intellij.openapi.project.*
import com.intellij.psi.*
import com.intellij.psi.codeStyle.*
import com.intellij.psi.impl.*
import com.intellij.psi.util.*
import org.jetbrains.uast.*
import siosio.doma.*
import siosio.doma.extension.*

class ImmutableEntityReturnTypeQuickFix : LocalQuickFix {

    override fun getFamilyName(): String {
        return DomaBundle.message("quick-fix.immutable-return-type")
    }

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val returnElement = descriptor.psiElement
        val method = PsiTreeUtil.getParentOfType(returnElement, PsiMethod::class.java) ?: return
        val firstParam = method.parameterList.parameters.firstOrNull { it.isEntity() }?.takeIf { it.isImmutableEntity() } ?: return
        ApplicationManager.getApplication().runWriteAction {
            val instance = PsiElementFactory.SERVICE.getInstance(project)
            returnElement.replace(instance.createTypeElement(
                    instance.createTypeByFQClassName("org.seasar.doma.jdbc.Result<${firstParam.type.canonicalText}>")
            ))
            JavaCodeStyleManager.getInstance(project)
                    .shortenClassReferences(method)
        }

    }

}