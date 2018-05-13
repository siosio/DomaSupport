package siosio.doma.inspection.dao.quickfix

import com.intellij.ide.util.*
import com.intellij.openapi.module.*
import com.intellij.openapi.project.*
import com.intellij.openapi.roots.*
import com.intellij.psi.*

open class DirectoryChooser {

    open fun chooseDirectory(project: Project, module: Module, isInTest: Boolean): PsiDirectory? {
        val roots = ModuleRootManager.getInstance(module).getSourceRoots(isInTest)
        val psiDirectories = roots.map { PsiManager.getInstance(project).findDirectory(it) }.toTypedArray()

        return DirectoryChooserUtil.chooseDirectory(
                psiDirectories, null, project, HashMap())
    }
}
