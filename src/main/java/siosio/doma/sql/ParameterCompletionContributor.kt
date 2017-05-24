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

        private val buildInFunctionList = listOf(
            "escape",
            "prefix",
            "infix",
            "suffix",
            "roundDownTimePart",
            "roundDownTimePart",
            "roundDownTimePart",
            "roundUpTimePart",
            "roundUpTimePart",
            "roundUpTimePart",
            "isEmpty",
            "isNotEmpty",
            "isBlank",
            "isNotBlank"
        )

        private val buildInFunctionLookupList = run {
            buildInFunctionList.map {
                LookupElementBuilder.create(it)
                    .withAutoCompletionPolicy(AutoCompletionPolicy.ALWAYS_AUTOCOMPLETE)
            }
        }

        override fun addCompletions(parameters: CompletionParameters,
                                    p1: ProcessingContext?,
                                    result: CompletionResultSet) {
            val originalFile = parameters.originalFile
            val text = parameters.originalPosition?.text?.let {
                val text = it.substringAfter("/*%if")
                    .substringAfter("/*")
                    .substringBefore("*/")
                    .trim()
                text
            } ?: ""

            if (isBuiltInFunction(text)) {
                result.addAllElements(buildInFunctionLookupList)
            } else {
                toDaoClass(originalFile)?.let {
                    it.findMethodsByName(toMethodName(originalFile), false)
                        .firstOrNull()
                        ?.parameterList
                        ?.parameters
                        ?.map(::VariableLookupItem)
                        ?.toList()
                        ?.let { list ->
                            result.addAllElements(list)
                        }
                }
            }
            result.stopHere()
        }

        private fun isBuiltInFunction(text: String): Boolean = text.startsWith("@") && !text.contains('(')
    }

}
