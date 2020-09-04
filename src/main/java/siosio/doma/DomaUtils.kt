package siosio.doma

import com.intellij.openapi.module.*
import com.intellij.openapi.project.*
import com.intellij.psi.*
import siosio.doma.extension.*

fun toSqlFilePath(path: String) = "META-INF/$path"

fun toDaoClass(sqlFile: PsiFile): PsiClass? {
    val parent = sqlFile.parent
    if (parent?.let { !it.isPhysical } == true) {
        return null
    }

    val className = toDaoClassName(parent, "")
    val project = sqlFile.project
    val module = getModule(project, sqlFile)
    return JavaPsiFacade.getInstance(project)
        .findClasses(className, module.moduleScope).firstOrNull()
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
    return project.findModule(element.containingFile.virtualFile)!!
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
            ".$className"
        }
    )
}

const val entityAnnotationName = "org.seasar.doma.Entity"
const val sqlAnnotationName = "org.seasar.doma.Sql"
const val sqlExperimentalAnnotationName = "org.seasar.doma.experimental.Sql"
