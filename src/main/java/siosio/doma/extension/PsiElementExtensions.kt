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
    return this.type.isEntity()
}

/**
 * このパラメータがImmutableEntityかどうか
 */
fun PsiParameter.isImmutableEntity(): Boolean {
    return this.type.isImmutableEntity()
}

/**
 * このTypeがEntityかどうか
 */
fun PsiType.isEntity(): Boolean {
    return PsiTypesUtil.getPsiClass(this)?.let {
        AnnotationUtil.isAnnotated(it, "org.seasar.doma.Entity", AnnotationUtil.CHECK_TYPE)
    } == true
}

/**
 * このTypeがImmutableEntityかどうか
 */
fun PsiType.isImmutableEntity(): Boolean {
    val annotation = AnnotationUtil.findAnnotation(PsiTypesUtil.getPsiClass(this), "org.seasar.doma.Entity") ?: return false
    return AnnotationUtil.getBooleanAttributeValue(annotation, "immutable") == true
}
