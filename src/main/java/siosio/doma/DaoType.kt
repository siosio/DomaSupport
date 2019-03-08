package siosio.doma

import com.intellij.codeInsight.*
import com.intellij.psi.*
import org.jetbrains.kotlin.idea.util.findAnnotation
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtAnnotation
import org.jetbrains.kotlin.psi.KtNamedFunction
import siosio.doma.inspection.dao.*

/**
 * Daoのメソッドタイプを表す列挙型。
 *
 * @author sioiso
 */
enum class DaoType(
    val annotationName: String,
    val rule: DaoInspectionRule,
    val kotlinRule: KotlinDaoInspectionRule = kotlinRule{},
    val extension: String = "sql") {

    SELECT("org.seasar.doma.Select", selectMethodRule, kotlinSelectMethodRule),
    UPDATE("org.seasar.doma.Update", updateMethodRule, kotlinUpdateMethodRule),
    INSERT("org.seasar.doma.Insert", insertMethodRule, kotlinInsertMethodRule),
    DELETE("org.seasar.doma.Delete", deleteMethodRule, kotlinDeleteMethodRule),
    BATCH_INSERT("org.seasar.doma.BatchInsert", batchInsertMethodRule),
    BATCH_UPDATE("org.seasar.doma.BatchUpdate", batchUpdateMethodRule),
    BATCH_DELETE("org.seasar.doma.BatchDelete", batchDeleteMethodRule),
    SCRIPT("org.seasar.doma.Script", scriptMethodRule, extension = "script");

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
                AnnotationUtil.isAnnotated(method, it.annotationName, AnnotationUtil.CHECK_TYPE)
            }
        }

        fun valueOf(function: KtNamedFunction): DaoType? {
            return values().firstOrNull {
                function.findAnnotation(FqName(it.annotationName)) != null
            }
        }
    }

}
