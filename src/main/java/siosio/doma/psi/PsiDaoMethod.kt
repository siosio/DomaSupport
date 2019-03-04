package siosio.doma.psi

import com.intellij.codeInsight.*
import com.intellij.openapi.module.*
import com.intellij.openapi.vfs.*
import com.intellij.psi.*
import org.jetbrains.kotlin.asJava.toLightAnnotation
import org.jetbrains.kotlin.idea.util.findAnnotation
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.psi.KtFunction
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.psiUtil.containingClass
import siosio.doma.*
import siosio.doma.extension.*

/**
 * [PsiMethod]のDaoメソッド表現。
 *
 * @author siosio
 */
class PsiDaoMethod(
    val psiMethod: PsiMethod,
    val daoType: DaoType) : PsiMethod by psiMethod {

    val daoAnnotation: PsiAnnotation = AnnotationUtil.findAnnotation(this, daoType.annotationName)!!

    /**
     * このDaoメソッドが存在しているモジュール
     */
    fun getModule(): Module? {
        return project.findModule(this.containingFile.virtualFile)
    }

    /**
     * このDaoメソッドがSQLファイルを必要とするかどうか
     */
    fun useSqlFile(): Boolean = daoAnnotation.useSqlFile()

    fun getSqlFilePath(): String {
        return "META-INF/${fqcnToFilePath()}/$name.${daoType.extension}"
    }

    /**
     * SQLファイルの存在有無
     *
     * @return 存在している場合`true`
     */
    fun containsSqlFile(): Boolean {
        return findSqlFile() != null
    }

    /**
     * SQLファイルを検索する。
     */
    fun findSqlFile(): VirtualFile? {
        return getModule()?.findSqlFileFromRuntimeScope(getSqlFilePath(), this.containingClass!!)
    }

    /**
     * このメソッドを持つクラスをパス形式の文字列で取得する
     */
    private fun fqcnToFilePath(): String {
        return containingClass!!.qualifiedName!!.replace('.', '/')
    }
}

class PsiDaoFunction(
        val psiFunction: KtNamedFunction,
        val daoType: DaoType) : KtFunction by psiFunction {

    val daoAnnotation: PsiAnnotation = psiFunction.findAnnotation(FqName(daoType.annotationName))!!.toLightAnnotation()!!

    /**
     * このDaoメソッドが存在しているモジュール
     */
    fun getModule(): Module? {
        return project.findModule(this.containingFile.virtualFile)
    }

    /**
     * このDaoメソッドがSQLファイルを必要とするかどうか
     */
    fun useSqlFile(): Boolean = daoAnnotation.useSqlFile()

    fun getSqlFilePath(): String {
        return "META-INF/${fqcnToFilePath()}/$name.${daoType.extension}"
    }

    /**
     * SQLファイルの存在有無
     *
     * @return 存在している場合`true`
     */
    fun containsSqlFile(): Boolean {
        return findSqlFile() != null
    }

    /**
     * SQLファイルを検索する。
     */
    fun findSqlFile(): VirtualFile? {
        return getModule()?.findSqlFileFromRuntimeScope(getSqlFilePath(), this.containingClass()!!)
    }

    /**
     * このメソッドを持つクラスをパス形式の文字列で取得する
     */
    private fun fqcnToFilePath(): String {
        return containingClass()!!.fqName!!.asString().replace('.', '/')
    }
}

