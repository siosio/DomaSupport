package siosio.doma.inspection.dao

import com.intellij.codeInspection.*

class DaoInspectionToolProvider : InspectionToolProvider {
    override fun getInspectionClasses(): Array<Class<out LocalInspectionTool>> {
        return arrayOf(DaoInspectionTool::class.java, KotlinDaoInspectionTool::class.java)
    }
}
