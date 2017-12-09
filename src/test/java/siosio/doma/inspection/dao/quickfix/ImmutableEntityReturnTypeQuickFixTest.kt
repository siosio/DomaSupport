package siosio.doma.inspection.dao.quickfix

import siosio.doma.*
import siosio.doma.inspection.dao.*

class ImmutableEntityReturnTypeQuickFixTest: DaoTestCase() {
    
    override fun getTestDataPath(): String = "testData/siosio/doma/inspection/dao/quickfix"

    override fun setUp() {
        super.setUp()
        myFixture.enableInspections(DaoInspectionTool())
    }

    fun `test_QuickFixで戻り値をResultに変換できること`() {
        myFixture.configureByFile("ImmutableEntityの戻り値QuickFix_before.java")
        val intention = myFixture.findSingleIntention(DomaBundle.message("quick-fix.immutable-return-type"))
        myFixture.launchAction(intention)
        myFixture.checkResultByFile("ImmutableEntityの戻り値QuickFix_after.java")
    }

    fun `test_引数がMutableEntityだからquickfixは適用できないこと`() {
        myFixture.configureByFile("MutableEntityの戻り値QuickFix_before.java")
        assertEmpty(myFixture.filterAvailableIntentions(DomaBundle.message("quick-fix.immutable-return-type")))
    }
}