package siosio.doma

import com.intellij.openapi.module.*
import com.intellij.openapi.project.*
import com.intellij.openapi.roots.*
import com.intellij.psi.*

fun toSqlFilePath(path: String) = "META-INF/$path"

fun toDaoClass(sqlFile: PsiFile): PsiClass? {
  val parent = sqlFile.parent
  if (parent?.let { !it.isPhysical } ?: false) {
    return null
  }

  val className = toDaoClassName(parent, "")
  val project = sqlFile.project
  val module = getModule(project, sqlFile)
  return JavaPsiFacade.getInstance(project)
      .findClasses(className, module.moduleScope).first()
}

fun toMethodName(sqlFile: PsiFile): String {
  val name = sqlFile.name
  val lastIndexOf = name.lastIndexOf('.')
  return if (lastIndexOf == -1) {
    name
  } else {
    name.substring(0, lastIndexOf)
  }
}

fun getModule(project: Project, element: PsiElement): Module {
  val module = ProjectRootManager.getInstance(project)
      .fileIndex
      .getModuleForFile(element.containingFile.virtualFile)!!
  return module
}

private fun toDaoClassName(dir: PsiDirectory?, className: String): String {
  if (dir == null || dir.name == "META-INF") {
    return className
  }
  return toDaoClassName(dir.parent,
      dir.name +
          if (className.isEmpty()) {
            className
          } else {
            "." + className
          }
  )
}
