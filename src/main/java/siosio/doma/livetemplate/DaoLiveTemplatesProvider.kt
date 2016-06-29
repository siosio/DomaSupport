package siosio.doma.livetemplate

import com.intellij.codeInsight.template.impl.*

/**
 * DAOメソッド用のテンプレートを提供する。
 */
class DaoLiveTemplatesProvider : DefaultLiveTemplatesProvider {

  override fun getDefaultLiveTemplateFiles(): Array<String> = arrayOf("doma")

  override fun getHiddenLiveTemplateFiles(): Array<String>? = emptyArray()
}
