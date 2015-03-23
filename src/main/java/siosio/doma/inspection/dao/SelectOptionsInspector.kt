package siosio.doma.inspection.dao

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.compiler.RemoveElementQuickFix
import siosio.doma.DomaBundle

/**
 * selectメソッドの検索オプション(SelectOptions)の検査を行うクラス。
 */
class SelectOptionsInspector : Inspector {

  companion object {
    kotlin.platform.platformStatic val SELECT_OPTIONS_CLASS_NAME = "org.seasar.doma.jdbc.SelectOptions";
  }

  override fun doInspection(context: DaoMethodInspectionContext) {

    val selectOptions = context.method.getParameterList().getParameters().filter {
      SELECT_OPTIONS_CLASS_NAME.equals(it.getType().getCanonicalText())
    }

    if (selectOptions.size() <= 1) {
      return;
    }

    selectOptions.forEach {
      context.problemsHolder.registerProblem(
          it,
          DomaBundle.message("inspection.dao.multi-SelectOptions"),
          ProblemHighlightType.ERROR,
          RemoveElementQuickFix(DomaBundle.message("quick-fix.remove", it.getName())))
    }
  }
}
