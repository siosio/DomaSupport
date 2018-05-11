package siosio.doma.refactoring

import com.intellij.psi.*
import com.intellij.refactoring.listeners.*
import com.intellij.refactoring.rename.*
import com.intellij.usageView.*
import siosio.doma.*
import siosio.doma.psi.*

/**
 * Daoのメソッド名がリファクタリングで変更された時に、SQLファイル名を連動して変更するクラス。
 *
 * @author siosio
 */
class DaoMethodRenameProcessor : RenameJavaMethodProcessor() {

    override fun canProcessElement(element: PsiElement): Boolean {
        return if (super.canProcessElement(element)) {
            createPsiDaoMethod(element as PsiMethod)?.findSqlFile() != null
        } else {
            false
        }
    }

    override fun renameElement(element: PsiElement,
                               newName: String,
                               p2: Array<out UsageInfo>,
                               p3: RefactoringElementListener?) {
        createPsiDaoMethod(element as PsiMethod)?.findSqlFile()?.let { sqlFile ->
            sqlFile.rename(sqlFile, "$newName.${sqlFile.extension}")
        }
        super.renameElement(element, newName, p2, p3)
    }

    private fun createPsiDaoMethod(method: PsiMethod): PsiDaoMethod? = DaoType.valueOf(method)?.let {
        PsiDaoMethod(method, it)
    }
}