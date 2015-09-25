package siosio.doma.editor


import com.intellij.codeInsight.*
import com.intellij.codeInsight.daemon.*
import com.intellij.codeInsight.navigation.*
import com.intellij.openapi.util.*
import com.intellij.psi.*
import siosio.doma.*
import siosio.doma.psi.*

/**
 * DAOのメソッドからSQLファイルへの移動を実現するやつ
 */
public class DaoMethodLineMarkerProvider : RelatedItemLineMarkerProvider() {

  override fun collectNavigationMarkers(
      elements: List<PsiElement>,
      result: MutableCollection<in RelatedItemLineMarkerInfo<PsiElement>>,
      forNavigation: Boolean) {
    super.collectNavigationMarkers(elements, result, forNavigation)
  }

  override fun collectNavigationMarkers(element: PsiElement, result: MutableCollection<in RelatedItemLineMarkerInfo<PsiElement>>?) {

    if (element !is PsiMethod) {
      return
    }

    DaoType.values().firstOrNull {
      AnnotationUtil.isAnnotated(element, it.annotationName, false)
    }?.let {
      val psiDaoMethod = PsiDaoMethod(element, it)
      psiDaoMethod.findSqlFile()
    }?.let {
      val psiFile = PsiManager.getInstance(element.getProject()).findFile(it)
      val builder = NavigationGutterIconBuilder.create(SQL_FILE_ICON)
          .setTargets(psiFile).setTooltipText(DomaBundle.message("editor.goto-sql-file"))
      result!!.add(builder.createLineMarkerInfo(element))
    }
  }

  companion object {
    private val SQL_FILE_ICON = IconLoader.getIcon("/icons/sql.png")
  }
}

