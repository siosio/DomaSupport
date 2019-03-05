package siosio.doma.extension

import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ResourceFileUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.resolve.lazy.data.KtClassInfoUtil

/**
 * モジュールの実行スコープ配下からSQLファイルを検索する。
 */
fun Module.findSqlFileFromRuntimeScope(sqlFilePath: String, daoClass: PsiElement): VirtualFile? {
    val scope = GlobalSearchScope.moduleRuntimeScope(this, daoClass.isInTest())
    return ResourceFileUtil.findResourceFileInScope(sqlFilePath, this.project, scope)
}

