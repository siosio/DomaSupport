package siosio.doma.editor

import com.intellij.codeInsight.daemon.*
import com.intellij.codeInsight.navigation.*
import com.intellij.openapi.application.*
import com.intellij.openapi.util.*
import com.intellij.psi.*
import com.intellij.psi.util.*
import org.jetbrains.kotlin.psi.KtNamedFunction
import siosio.doma.*
import siosio.doma.editor.DaoMethodLineMarkerProvider.Companion.SQL_FILE_ICON
import siosio.doma.extension.*
import siosio.doma.psi.*

class DaoMethodLineMarkerProvider : RelatedItemLineMarkerProvider() {
    
    companion object {
        private val SQL_FILE_ICON = IconLoader.getIcon("/icons/sql.png")
    }

    override fun collectNavigationMarkers(element: PsiElement,
                                          result: MutableCollection<in RelatedItemLineMarkerInfo<PsiElement>>) {
        
        // テストモードは以下の処理は実行しない
        if (ApplicationManager.getApplication().isUnitTestMode) {
            return
        }

        when (element) {
            is PsiMethod -> PsiDaoMethod(element, DaoType.valueOf(element) ?: return ).findSqlFile()
            is KtNamedFunction -> PsiDaoFunction(element, DaoType.valueOf(element) ?: return).findSqlFile()
            else -> null
        }?.let { 
            element.project.findFile(it)
        }?.let {
            result.add(
                    NavigationGutterIconBuilder.create(SQL_FILE_ICON)
                            .setTargets(it)
                            .setTooltipText(DomaBundle.message("editor.goto-sql-file"))
                            .createLineMarkerInfo(element.firstChild)
            )
        }
        
    }
    
}

