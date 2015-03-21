package siosio.doma

import com.intellij.codeInsight.AnnotationUtil
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ResourceFileUtil
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiType
import com.intellij.psi.search.GlobalSearchScope

import java.util.Arrays

/**
 * 便利メソッド群。
 * @author siosio
 * @since 1.0
 */
class DomaUtils {

  companion object {

    /**
     * DAOクラスを表すアノテーションクラス名
     */
    val DAO_ANNOTATION_NAME = "org.seasar.doma.Dao"

    /**
     * DAOのタイプへとな
     *
     * @param method メソッド
     * @return DAOのタイプを取得する
     */
    fun toDaoType(method: PsiMethod): DaoType? {
      for (daoType in DaoType.values()) {
        if (AnnotationUtil.isAnnotated(method, daoType.getAnnotation(), false)) {
          return daoType
        }
      }
      return null
    }


    fun containsSqlFile(method: PsiMethod, filePath:String):Boolean {
      return findSqlFile(method, filePath) != null
    }

    /**
     * SQLファイルを検索する。
     *
     * @param method   メソッド
     * @param filePath SQLのファイルパス
     * @return SQLファイルを表すVirtualFile(存在しない場合は、null)
     */
    public fun findSqlFile(method: PsiMethod, filePath: String): VirtualFile? {
      val module = ProjectRootManager.getInstance(method.getProject()).getFileIndex().getModuleForFile(method.getContainingFile().getVirtualFile())

      if (module == null) {
        return null
      }

      val scope = GlobalSearchScope.moduleRuntimeScope(module, false)
      return ResourceFileUtil.findResourceFileInScope(filePath, method.getProject(), scope)
    }
  }
}

