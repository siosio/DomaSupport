package siosio.doma.extension

import com.intellij.openapi.module.*
import com.intellij.openapi.project.*
import com.intellij.openapi.roots.*
import com.intellij.openapi.vfs.*
import com.intellij.psi.*

/**
 * [VirtualFile]をプロジェクト内に存在している[PsiFile]として取得する。
 */
fun Project.findFile(virtualFile: VirtualFile): PsiFile? = PsiManager.getInstance(this).findFile(virtualFile)

/**
 * [VirtualFile]が存在しているも[Module]を返す
 */
fun Project.findModule(virtualFile: VirtualFile): Module? =
    ProjectRootManager.getInstance(this)
        .fileIndex
        .getModuleForFile(virtualFile)
