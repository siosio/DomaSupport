package siosio.doma.extension

import com.intellij.codeInsight.*
import com.intellij.openapi.roots.*
import com.intellij.psi.*
import com.intellij.psi.util.*

/**
 * SQLファイルを必要とするかどうか
 *
 * このアノテーションの`sqlFile`属性が`true`の場合は必要
 */
fun PsiAnnotation.useSqlFile(): Boolean = AnnotationUtil.getBooleanAttributeValue(this, "sqlFile") ?: false

/**
 * このクラスがテストスコープにあるかどうか
 */
fun PsiClass.isInTest(): Boolean = ProjectFileIndex.SERVICE.getInstance(project).isInTestSourceContent(this.containingFile.virtualFile)


/**
 * このパラメータがEntityかどうか
 */
fun PsiParameter.isEntity(): Boolean {
    return PsiTypesUtil.getPsiClass(this.type)?.let {
        AnnotationUtil.isAnnotated(it, "org.seasar.doma.Entity", false)
    } ?: false
}

/**
 * このパラメータがImmutableEntityかどうか
 */
fun PsiParameter.isImmutableEntity(): Boolean {
    return PsiTypesUtil.getPsiClass(this.type)?.let {
        val annotation = AnnotationUtil.findAnnotation(it, "org.seasar.doma.Entity") ?: return false
        AnnotationUtil.getBooleanAttributeValue(annotation, "immutable")
    } ?: false
}
