package siosio.doma.inspection.dao

import com.intellij.codeInspection.InspectionToolProvider
import siosio.doma.inspection.dao.DaoInspectionTool

public class DaoInspectionToolProvider : InspectionToolProvider {

  override fun getInspectionClasses(): Array<Class<Any>> {
    @suppress("CAST_NEVER_SUCCEEDS")
    return arrayOf(DaoInspectionTool::class) as Array<Class<Any>>
  }
}
