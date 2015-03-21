package siosio.doma.inspection.dao

import com.intellij.codeInsight.AnnotationUtil

/**
 * SQLファイルが任意の場合の検索クラス
 */
public class OptionalSqlFileInspector(fileExtension: String) : SqlFileInspector(fileExtension) {
  override fun doInspection(context: DaoMethodInspectionContext) {
    val psiMethod = context.method
    val daoAnnotation = AnnotationUtil.findAnnotation(psiMethod, context.daoType.getAnnotation())!!
    if (!(AnnotationUtil.getBooleanAttributeValue(daoAnnotation, "sqlFile") ?: false)) {
      return;
    }
    super.doInspection(context)
  }
}