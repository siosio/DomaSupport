package siosio.doma.psi

import com.intellij.codeInsight.*
import com.intellij.openapi.module.*
import com.intellij.openapi.vfs.*
import com.intellij.psi.*
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

    val daoAnnotation: PsiAnnotation = AnnotationUtil.findAnnotation(psiMethod, daoType.annotationName)!!

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

    override fun getSourceElement(): PsiElement? {
        return psiMethod.sourceElement
    }

    fun parameterCount(): Int {
        return psiMethod.parameterList.parametersCount
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PsiDaoMethod && this.psiMethod === other) return true
        return false
    }
}

