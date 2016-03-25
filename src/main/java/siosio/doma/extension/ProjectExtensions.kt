package siosio.doma.extension

import com.intellij.openapi.project.*
import com.intellij.openapi.vfs.*
import com.intellij.psi.*

/**
 * [VirtualFile]をプロジェクト内に存在している[PsiFile]として取得する。
 */
fun Project.findFile(virtualFile: VirtualFile): PsiFile? = PsiManager.getInstance(this).findFile(virtualFile)
