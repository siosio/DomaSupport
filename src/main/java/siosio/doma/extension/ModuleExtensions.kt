package siosio.doma.extension

import com.intellij.openapi.module.*
import com.intellij.openapi.roots.*
import com.intellij.openapi.vfs.*
import com.intellij.psi.*
import com.intellij.psi.search.*

/**
 * モジュールの実行スコープ配下からSQLファイルを検索する。
 */
fun Module.findSqlFileFromRuntimeScope(sqlFilePath: String, daoClass: PsiClass): VirtualFile? {
    val scope = GlobalSearchScope.moduleRuntimeScope(this, daoClass.isInTest())
    return ResourceFileUtil.findResourceFileInScope(sqlFilePath, this.project, scope)
}

