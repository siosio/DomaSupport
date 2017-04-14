package siosio.doma.editor

import com.intellij.execution.*
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.fileEditor.*
import com.intellij.psi.*
import com.intellij.psi.util.*
import siosio.doma.*
import siosio.doma.psi.*

class GotoSqlFile : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val dataContext = e.dataContext
        val location = Location.DATA_KEY.getData(dataContext)
        location?.psiElement?.let {
            PsiTreeUtil.getParentOfType(it, PsiMethod::class.java, false)
        }?.let { method ->
            DaoType.valueOf(method)?.let {
                PsiDaoMethod(method, it)
            }
        }?.let { daoMethod ->
            FileEditorManager.getInstance(daoMethod.project)
                .openFile(daoMethod.findSqlFile() ?: return, true)
        }
    }
}
