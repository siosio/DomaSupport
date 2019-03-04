package siosio.doma.inspection.dao.quickfix

import org.jetbrains.kotlin.psi.psiUtil.containingClass
import siosio.doma.extension.*
import siosio.doma.psi.*

object CreateSqlFileQuickFixFactory {

    fun create(daoMethod: PsiDaoMethod): CreateSqlFileQuickFix {
        val module = daoMethod.getModule()!!
        return System.getProperty("DirectoryChooser.className")?.let {
            val directoryChooser = Class.forName(it).newInstance() as DirectoryChooser
            CreateSqlFileQuickFix(
                    module,
                    daoMethod.getSqlFilePath(),
                    daoMethod.containingClass!!.isInTest(),
                    directoryChooser)
        } ?: CreateSqlFileQuickFix(
                module,
                daoMethod.getSqlFilePath(),
                daoMethod.containingClass!!.isInTest())
    }


    fun create(daoFunction: PsiDaoFunction): CreateSqlFileQuickFix {
        val module = daoFunction.getModule()!!
        return System.getProperty("DirectoryChooser.className")?.let {
            val directoryChooser = Class.forName(it).newInstance() as DirectoryChooser
            CreateSqlFileQuickFix(
                    module,
                    daoFunction.getSqlFilePath(),
                    false,
                    directoryChooser)
        } ?: CreateSqlFileQuickFix(
                module,
                daoFunction.getSqlFilePath(),
                false)
    }
}
