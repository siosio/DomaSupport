package siosio.doma.editor

import com.intellij.codeInsight.daemon.*
import com.intellij.codeInsight.navigation.*
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
        elements: List<PsiElement>,
        result: MutableCollection<in RelatedItemLineMarkerInfo<PsiElement>>,
        forNavigation: Boolean) {
        super.collectNavigationMarkers(elements, result, forNavigation)
    }

    override fun collectNavigationMarkers(element: PsiElement,
                                          result: MutableCollection<in RelatedItemLineMarkerInfo<PsiElement>>?) {
        if (element !is PsiMethod || result == null) {
            return
        }

        DaoType.valueOf(element)?.let {
            PsiDaoMethod(element, it).findSqlFile()
        }?.let {
            val psiFile = element.project.findFile(it)
            PsiTreeUtil.findChildrenOfType(element, PsiAnnotation::class.java).firstOrNull()?.let {
                val builder = NavigationGutterIconBuilder.create(SQL_FILE_ICON)
                    .setTargets(psiFile).setTooltipText(DomaBundle.message("editor.goto-sql-file"))
                result.add(builder.createLineMarkerInfo(it))
            }
        }
    }

    companion object {
        private val SQL_FILE_ICON = IconLoader.getIcon("/icons/sql.png")
    }
}

