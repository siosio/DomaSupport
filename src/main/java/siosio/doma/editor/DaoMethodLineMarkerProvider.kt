package siosio.doma.editor


import com.intellij.codeInsight.AnnotationUtil
import javax.swing.*

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiMethod
import siosio.doma.DaoType
import siosio.doma.DomaBundle
import siosio.doma.psi.PsiDaoMethod

/**
 * DAOのメソッドからSQLファイルへの移動を実現するやつ
 */
public class DaoMethodLineMarkerProvider : RelatedItemLineMarkerProvider() {

  override fun collectNavigationMarkers(elements: List<PsiElement>, result: MutableCollection<in RelatedItemLineMarkerInfo<PsiElement>>, forNavigation: Boolean) {
    super.collectNavigationMarkers(elements, result, forNavigation)
  }

  override fun collectNavigationMarkers(element: PsiElement, result: MutableCollection<in RelatedItemLineMarkerInfo<PsiElement>>?) {

    if (element !is PsiMethod) {
      return
    }

    val daoType = DaoType.values().firstOrNull {
      AnnotationUtil.isAnnotated(element, it.annotationName, false)
    }
    if (daoType == null) {
      return;
    }
    val psiDaoMethod = PsiDaoMethod(element, daoType)

    val file = psiDaoMethod.findSqlFile()
    if (file == null) {
      return
    }

    val psiFile = PsiManager.getInstance(element.getProject()).findFile(file)
    val builder = NavigationGutterIconBuilder.create(SQL_FILE_ICON).setTargets(psiFile).setTooltipText(DomaBundle.message("editor.goto-sql-file"))
    result!!.add(builder.createLineMarkerInfo(element))
  }

  companion object {

    private val SQL_FILE_ICON = IconLoader.getIcon("/icons/sql.png")
  }
}

