package siosio.doma.inspection.dao.quickfix

import com.intellij.codeInsight.intention.*
import siosio.doma.*
import siosio.doma.inspection.dao.*

class InsertReturnTypeQuickFixTest: DaoTestCase() {
    
    override fun getTestDataPath(): String = "testData/siosio/doma/inspection/dao/quickfix"

    override fun setUp() {
        super.setUp()
        myFixture.enableInspections(DaoInspectionTool())
    }

    fun `test_QuickFixで戻り値をResultに変換できること`() {
        myFixture.configureByFile("ImmutableEntityの戻り値QuickFix_before.java")
        myFixture.launchAction(getFixReturnTypeIntention()!!)
        myFixture.checkResultByFile("ImmutableEntityの戻り値QuickFix_after.java")
    }

    fun `test_QuickFixで戻り値をintに変換できること`() {
        myFixture.configureByFile("MutableEntityの戻り値QuickFix_before.java")
        myFixture.launchAction(getFixReturnTypeIntention()!!)
        myFixture.checkResultByFile("MutableEntityの戻り値QuickFix_after.java")
    }
    
    private fun getFixReturnTypeIntention(): IntentionAction? {
        return myFixture.availableIntentions.firstOrNull {
            it.familyName == "Fix return type"
        }
    }
}