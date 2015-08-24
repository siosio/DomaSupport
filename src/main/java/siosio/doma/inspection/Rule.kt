package siosio.doma.inspection

import siosio.doma.inspection.dao.*

interface Rule<in PsiElement> {
  fun inspect(context: InspectionContext, element: PsiElement): Unit
}