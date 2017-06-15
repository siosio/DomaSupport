package siosio.doma.inspection

import com.intellij.codeInspection.*

interface Rule<in PsiElement> {
    fun inspect(problemsHolder: ProblemsHolder, element: PsiElement): Unit
}