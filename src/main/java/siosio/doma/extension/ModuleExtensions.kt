package siosio.doma.extension

import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ResourceFileUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiClass
import com.intellij.psi.search.GlobalSearchScope
import org.jetbrains.kotlin.psi.KtClass

/**
 * モジュールの実行スコープ配下からSQLファイルを検索する。
 */
fun Module.findSqlFileFromRuntimeScope(sqlFilePath: String, daoClass: PsiClass): VirtualFile? {
    val scope = GlobalSearchScope.moduleRuntimeScope(this, daoClass.isInTest())
    return ResourceFileUtil.findResourceFileInScope(sqlFilePath, this.project, scope)
}

/**
 * モジュールの実行スコープ配下からSQLファイルを検索する。
 */
fun Module.findSqlFileFromRuntimeScope(sqlFilePath: String, daoClass: KtClass): VirtualFile? {
    val scope = GlobalSearchScope.moduleRuntimeScope(this, false)
    return ResourceFileUtil.findResourceFileInScope(sqlFilePath, this.project, scope)
}

