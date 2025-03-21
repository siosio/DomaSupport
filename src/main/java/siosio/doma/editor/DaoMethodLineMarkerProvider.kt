package siosio.doma.editor

import com.intellij.codeInsight.daemon.*
import com.intellij.codeInsight.navigation.*
import com.intellij.openapi.application.*
import com.intellij.openapi.util.*
import com.intellij.psi.*
import org.jetbrains.kotlin.psi.*
import siosio.doma.*
import siosio.doma.extension.*
import siosio.doma.psi.*

class DaoMethodLineMarkerProvider : RelatedItemLineMarkerProvider() {

    override fun collectNavigationMarkers(element: PsiElement,
                                          result: MutableCollection<in RelatedItemLineMarkerInfo<*>>) {
        
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
            println(SQL_FILE_ICON)
            result.add(
                    NavigationGutterIconBuilder.create(SQL_FILE_ICON)
                            .setTargets(it)
                            .setTooltipText(DomaBundle.message("editor.goto-sql-file"))
                            .createLineMarkerInfo(element.firstChild)
            )
        }
    }
}

private val SQL_FILE_ICON = IconLoader.getIcon("database-solid.svg", DaoMethodLineMarkerProvider::class.java.classLoader)