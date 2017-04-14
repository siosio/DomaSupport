package siosio.doma

import com.intellij.codeInsight.*
import com.intellij.psi.*
import siosio.doma.inspection.dao.*

/**
 * Daoのメソッドタイプを表す列挙型。
 *
 * @author sioiso
 */
enum class DaoType(
    val annotationName: String,
    val rule: DaoInspectionRule,
    val extension: String = "sql") {

  SELECT("org.seasar.doma.Select", selectMethodRule),
  UPDATE("org.seasar.doma.Update", updateMethodRule),
  INSERT("org.seasar.doma.Insert", insertMethodRule),
  DELETE("org.seasar.doma.Delete", deleteMethodRule),
  BATCH_INSERT("org.seasar.doma.BatchInsert", batchInsertMethodRule),
  BATCH_UPDATE("org.seasar.doma.BatchUpdate", batchUpdateMethodRule),
  BATCH_DELETE("org.seasar.doma.BatchDelete", batchDeleteMethodRule),
  SCRIPT("org.seasar.doma.Script", scriptMethodRule, "script");

  companion object {
    /**
     * メソッドのタイプを取得する。
     *
     * この列挙型がサポートしないタイプの場合は`null`
     *
     * @param method メソッド
     * @return タイプ
     */
    fun valueOf(method: PsiMethod): DaoType? {
      return values().firstOrNull {
        AnnotationUtil.isAnnotated(method, it.annotationName, false)
      }
    }
  }

}
