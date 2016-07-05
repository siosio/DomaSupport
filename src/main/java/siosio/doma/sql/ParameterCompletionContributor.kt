package siosio.doma.sql

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.*
import com.intellij.patterns.*
import com.intellij.psi.*
import com.intellij.util.*
import siosio.doma.*

open class ParameterCompletionContributor : CompletionContributor() {

  init {
    extend(CompletionType.BASIC,
        PlatformPatterns.psiElement(PsiComment::class.java)
            .inFile(PlatformPatterns.psiFile().withName(StandardPatterns.string().endsWith(".sql"))),
        SqlParameterCompletionProvider())
  }

  private class SqlParameterCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, p1: ProcessingContext?, result: CompletionResultSet) {
      val originalFile = parameters.originalFile
      parameters.originalPosition?.text?.let {
        val text = it.replace("/*", "").replace("*/", "")
        if (!text.trim().isEmpty() && text.last().isWhitespace() || text.trim().split(" ").size != 1) {
          result.stopHere()
          return
        }
      }

      toDaoClass(originalFile)?.let {
        it.findMethodsByName(toMethodName(originalFile), false).first()?.parameterList?.parameters?.forEach {
          result.addElement(VariableLookupItem(it))
        }
      }
      result.stopHere()
    }
  }
}
