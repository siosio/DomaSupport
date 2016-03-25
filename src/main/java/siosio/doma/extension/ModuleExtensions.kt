package siosio.doma.extension

import com.intellij.openapi.module.*
import com.intellij.openapi.vfs.*
import com.intellij.psi.search.*

/**
 * モジュールの実行スコープ配下からSQLファイルを検索する。
 */
fun Module.findSqlFileFromRuntimeScope(sqlFilePath: String): VirtualFile? {
  val scope = GlobalSearchScope.moduleRuntimeScope(this, false)
  return ResourceFileUtil.findResourceFileInScope(sqlFilePath, this.project, scope)
}

