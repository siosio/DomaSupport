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

        private val buildInFunctionLookupList = listOf(
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
        ).map {
            LookupElementBuilder.create(it)
                    .withAutoCompletionPolicy(AutoCompletionPolicy.ALWAYS_AUTOCOMPLETE)
        }

        private val percentCommentLookupList = listOf(
                "expand",
                "if",
                "end"
        ).map {
            LookupElementBuilder.create(it)
                    .withAutoCompletionPolicy(AutoCompletionPolicy.ALWAYS_AUTOCOMPLETE)
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
            } else if (isPercentComment(text)) {
                result.addAllElements(percentCommentLookupList)
            } else {
                val paramList = toDaoClass(originalFile)
                        ?.findMethodsByName(toMethodName(originalFile), false)
                        ?.firstOrNull()
                        ?.parameterList
                        ?.parameters
                if (text.contains("(")) {
                    val paramText = text.substringAfter("(")
                    paramList
                            ?.filter {
                                it.name?.startsWith(paramText) ?: false
                            }
                            ?.map(::VariableLookupItem)
                            ?.toList()
                            ?.let { list ->
                                result.addAllElements(list)
                            }
                } else {
                    paramList
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
        private fun isPercentComment(text: String): Boolean = text.startsWith("%")
    }

}
