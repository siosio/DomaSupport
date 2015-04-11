package siosio.doma.psi

import com.intellij.codeInsight.AnnotationUtil
import com.intellij.psi.PsiAnnotation

public class PsiDaoAnnotation(val annotation: PsiAnnotation) : PsiAnnotation by annotation {

  /**
   * SQLファイルを必要とするか
   */
  fun useSqlFile(): Boolean {
    return AnnotationUtil.getBooleanAttributeValue(this, "sqlFile") ?: false
  }
}