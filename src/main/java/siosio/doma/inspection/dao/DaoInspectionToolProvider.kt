package siosio.doma.inspection.dao

import com.intellij.codeInspection.*

class DaoInspectionToolProvider : InspectionToolProvider {

    override fun getInspectionClasses(): Array<Class<Any>> {
        @Suppress("CAST_NEVER_SUCCEEDS", "UNCHECKED_CAST")
        return arrayOf(DaoInspectionTool::class.java, KotlinDaoInspectionTool::class.java) as Array<Class<Any>>
    }
}
