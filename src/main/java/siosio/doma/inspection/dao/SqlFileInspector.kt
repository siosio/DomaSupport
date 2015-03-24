package siosio.doma.inspection.dao

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import siosio.doma.DomaBundle
import siosio.doma.DomaUtils
import siosio.doma.inspection.dao.quickfix.CreateSqlFileQuickFix

/**
 * SQLファイルの存在チェックを行うインスペクションクラス。
 */
open class SqlFileInspector(val fileExtension: String) : Inspector {

  override fun doInspection(context: DaoMethodInspectionContext) {
    val psiMethod = context.method
    val sqlFilePath = buildSqlFilePath(context.psiClass, psiMethod)
    val module = ProjectRootManager.getInstance(psiMethod.getProject())
        .getFileIndex()
        .getModuleForFile(psiMethod.getContainingFile().getVirtualFile())

    if (module == null) {
      return
    }

    if (!DomaUtils.containsSqlFile(psiMethod, sqlFilePath)) {
      context.problemsHolder.registerProblem(
          psiMethod.getNameIdentifier()!!,
          DomaBundle.message("inspection.dao.sql-not-found"),
          ProblemHighlightType.ERROR,
          CreateSqlFileQuickFix(module, sqlFilePath))
    }
  }

  /**
   * SQLファイル名を生成する。
   *
   * @param psiClass 検査対象クラス
   * @param method 検査対象メソッド
   * @return SQLファイル名（クラスパス配下からの相対パス）
   */
  fun buildSqlFilePath(psiClass: PsiClass, method: PsiMethod): String {
    return (
        "META-INF/"
            + psiClass.getQualifiedName()!!.replace('.', '/')
            + '/' + method.getName()
            + '.' + fileExtension
        )
  }
}

