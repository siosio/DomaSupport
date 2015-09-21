package siosio.doma.inspection.dao

import com.intellij.codeInspection.*

public class DaoInspectionToolProvider : InspectionToolProvider {

  override fun getInspectionClasses(): Array<Class<Any>> {
    @suppress("CAST_NEVER_SUCCEEDS")
    return arrayOf(javaClass<DaoInspectionTool>()) as Array<Class<Any>>
  }
}
