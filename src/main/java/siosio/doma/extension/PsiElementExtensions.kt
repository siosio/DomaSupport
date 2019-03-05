package siosio.doma.extension

import com.intellij.codeInsight.AnnotationUtil
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiParameter
import com.intellij.psi.PsiType
import com.intellij.psi.util.PsiTypesUtil
import org.jetbrains.kotlin.psi.KtClass

/**
 * SQLファイルを必要とするかどうか
 *
 * このアノテーションの`sqlFile`属性が`true`の場合は必要
 */
fun PsiAnnotation.useSqlFile(): Boolean = AnnotationUtil.getBooleanAttributeValue(this, "sqlFile") ?: false

fun PsiElement.isInTest(): Boolean = ProjectFileIndex.SERVICE.getInstance(project).isInTestSourceContent(this.containingFile.virtualFile)

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
