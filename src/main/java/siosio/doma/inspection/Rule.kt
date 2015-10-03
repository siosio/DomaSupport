package siosio.doma.inspection

import com.intellij.codeInspection.*
import siosio.doma.inspection.dao.*

interface Rule<in PsiElement> {
  fun inspect(problemsHolder: ProblemsHolder, element: PsiElement): Unit
}