package siosio.doma.psi

import com.intellij.openapi.module.Module
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiAnnotation
import org.jetbrains.kotlin.analysis.api.analyze
import org.jetbrains.kotlin.analysis.api.symbols.KaNamedFunctionSymbol
import org.jetbrains.kotlin.asJava.toLightAnnotation
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.psi.KtFunction
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.psiUtil.containingClass
import siosio.doma.DaoType
import siosio.doma.extension.findModule
import siosio.doma.extension.findSqlFileFromRuntimeScope
import siosio.doma.extension.useSqlFile
import siosio.doma.sqlAnnotationName
import siosio.doma.sqlExperimentalAnnotationName

class PsiDaoFunction(
    val psiFunction: KtNamedFunction,
    val daoType: DaoType
) : KtFunction by psiFunction {

    /**
     * DAO メソッドに付いている Doma アノテーション（@Select, @Update など）
     */
    val daoAnnotation: PsiAnnotation =
        psiFunction.findAnnotationEntryByFqName(FqName(daoType.annotationName))
            ?.toLightAnnotation()
            ?: error("Annotation ${daoType.annotationName} not found on function ${psiFunction.name}")

    /**
     * このDaoメソッドが存在しているモジュール
     */
    fun getModule(): Module? =
        project.findModule(this.containingFile.virtualFile)

    /**
     * このDaoメソッドがSQLファイルを必要とするかどうか
     */
    fun useSqlFile(): Boolean =
        daoAnnotation.useSqlFile() ||
                psiFunction.hasAnnotationByFqName(FqName(sqlAnnotationName)) ||
                psiFunction.hasAnnotationByFqName(FqName(sqlExperimentalAnnotationName))

    fun getSqlFilePath(): String =
        "META-INF/${fqcnToFilePath()}/$name.${daoType.extension}"

    /**
     * SQLファイルの存在有無
     *
     * @return 存在している場合`true`
     */
    fun containsSqlFile(): Boolean =
        findSqlFile() != null

    /**
     * SQLファイルを検索する。
     */
    fun findSqlFile(): VirtualFile? =
        getModule()?.findSqlFileFromRuntimeScope(getSqlFilePath(), this.containingClass()!!)

    /**
     * このメソッドを持つクラスをパス形式の文字列で取得する
     */
    private fun fqcnToFilePath(): String =
        containingClass()!!.fqName!!.asString().replace('.', '/')
}

/**
 * KtNamedFunction から、指定された FQ 名のアノテーションに対応する KtAnnotationEntry を取得する
 * （K2 Analysis API ベース）
 */
private fun KtNamedFunction.findAnnotationEntryByFqName(fqName: FqName): KtAnnotationEntry? =
    analyze(this) {
        val symbol = this@findAnnotationEntryByFqName.symbol as? KaNamedFunctionSymbol ?: return@analyze null
        val anno = symbol.annotations.firstOrNull { annotation ->
            annotation.classId?.asSingleFqName() == fqName
        } ?: return@analyze null
        // KaAnnotation から元の PSI (KtAnnotationEntry) を取得
        anno.psi as? KtAnnotationEntry
    }

/**
 * KtNamedFunction に指定された FQ 名のアノテーションが付いているかどうか
 */
private fun KtNamedFunction.hasAnnotationByFqName(fqName: FqName): Boolean =
    findAnnotationEntryByFqName(fqName) != null
