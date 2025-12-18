package siosio.doma.extension

import com.intellij.codeInsight.AnnotationUtil
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiParameter
import com.intellij.psi.PsiType
import com.intellij.psi.util.PsiTypesUtil
import org.jetbrains.kotlin.analysis.api.analyze
import org.jetbrains.kotlin.analysis.api.symbols.KaClassSymbol
import org.jetbrains.kotlin.psi.KtParameter
import org.jetbrains.kotlin.name.FqName

/**
 * SQLファイルを必要とするかどうか
 *
 * このアノテーションの`sqlFile`属性が`true`の場合は必要
 */
fun PsiAnnotation.useSqlFile(): Boolean =
    AnnotationUtil.getBooleanAttributeValue(this, "sqlFile") ?: false

fun PsiElement.isInTest(): Boolean =
    ProjectFileIndex.SERVICE.getInstance(project)
        .isInTestSourceContent(this.containingFile.virtualFile)

/**
 * このパラメータがEntityかどうか (Java)
 */
fun PsiParameter.isEntity(): Boolean =
    this.type.isEntity()

/**
 * このパラメータがImmutableEntityかどうか (Java)
 */
fun PsiParameter.isImmutableEntity(): Boolean =
    this.type.isImmutableEntity()

/**
 * このTypeがEntityかどうか (Java)
 */
fun PsiType.isEntity(): Boolean =
    PsiTypesUtil.getPsiClass(this)?.let {
        AnnotationUtil.isAnnotated(it, "org.seasar.doma.Entity", AnnotationUtil.CHECK_TYPE)
    } == true

/**
 * このTypeがImmutableEntityかどうか (Java)
 */
fun PsiType.isImmutableEntity(): Boolean {
    val annotation = AnnotationUtil.findAnnotation(
        PsiTypesUtil.getPsiClass(this),
        "org.seasar.doma.Entity"
    ) ?: return false
    return AnnotationUtil.getBooleanAttributeValue(annotation, "immutable") == true
}

/**
 * この Kotlin パラメータがEntityかどうか (K2 Analysis API)
 *
 * Java版 `PsiType.isEntity()` と同様、
 * 型のクラスに org.seasar.doma.Entity が付いているかを判定する。
 */
fun KtParameter.isEntity(): Boolean = analyze(this) {
    // K2 Analysis API でシンボルと型を取得（getParameterSymbol() → symbol）
    val paramSymbol = this@isEntity.symbol
    val paramType = paramSymbol.returnType

    // クラス型に対応するシンボルを取得（expandedClassSymbol → expandedSymbol）
    val classSymbol = paramType.expandedSymbol as? KaClassSymbol ?: return@analyze false

    val entityFqName = FqName("org.seasar.doma.Entity")

    // クラスに @Entity が付いているか？
    classSymbol.annotations.any { anno ->
        anno.classId?.asSingleFqName() == entityFqName
    }
}
