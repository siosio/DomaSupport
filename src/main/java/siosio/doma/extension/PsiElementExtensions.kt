package siosio.doma.extension

import com.intellij.codeInsight.*
import com.intellij.openapi.roots.*
import com.intellij.psi.*

/**
 * SQLファイルを必要とするかどうか
 *
 * このアノテーションの`sqlFile`属性が`true`の場合は必要
 */
fun PsiAnnotation.useSqlFile() :Boolean = AnnotationUtil.getBooleanAttributeValue(this, "sqlFile") ?: false

/**
 * このクラスがテストスコープにあるかどうか
 */
fun PsiClass.isInTest():Boolean = ProjectFileIndex.SERVICE.getInstance(project).isInTestSourceContent(this.containingFile.virtualFile)
