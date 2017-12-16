package siosio.doma.editor

import com.intellij.codeInsight.daemon.*
import com.intellij.codeInsight.navigation.*
import com.intellij.openapi.application.*
import com.intellij.openapi.util.*
import com.intellij.psi.*
import com.intellij.psi.util.*
import siosio.doma.*
import siosio.doma.extension.*
import siosio.doma.psi.*

/**
 * Daoのメソッドから対応するSQLファイルへ移動するためのアイコンを表示するクラス。
 *
 * @author siosio
 */
class DaoMethodLineMarkerProvider : RelatedItemLineMarkerProvider() {

    override fun collectNavigationMarkers(
            element: PsiElement,
            result: MutableCollection<in RelatedItemLineMarkerInfo<PsiElement>>) {
        
        // テストモードは以下の処理は実行しない
        if (ApplicationManager.getApplication().isUnitTestMode) {
            return
        }


        if (element !is PsiMethod) {
            return
        }

        val sqlFile = DaoType.valueOf(element)?.let {
            PsiDaoMethod(element, it).findSqlFile()
        } ?: return

        val psiFile = element.project.findFile(sqlFile) ?: return
        val annotation = PsiTreeUtil.findChildrenOfType(element, PsiAnnotation::class.java).firstOrNull() ?: return

        result.add(
                NavigationGutterIconBuilder.create(SQL_FILE_ICON)
                        .setTargets(psiFile)
                        .setTooltipText(DomaBundle.message("editor.goto-sql-file"))
                        .createLineMarkerInfo(annotation)
        )
    }

    companion object {
        private val SQL_FILE_ICON = IconLoader.getIcon("/icons/sql.png")
    }
}

