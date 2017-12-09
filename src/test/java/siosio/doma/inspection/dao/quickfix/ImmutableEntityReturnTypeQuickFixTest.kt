package siosio.doma.inspection.dao.quickfix

import siosio.doma.*
import siosio.doma.inspection.dao.*

class ImmutableEntityReturnTypeQuickFixTest: DaoTestCase() {
    
    override fun getTestDataPath(): String = "testData/siosio/doma/inspection/dao/quickfix"

    override fun setUp() {
        super.setUp()
        myFixture.enableInspections(DaoInspectionTool())
    }


    fun `test_Result_Entity_になること`() {
        myFixture.configureByFile("ImmutableEntityの戻り値QuickFix_before.java")
        val intention = myFixture.findSingleIntention(DomaBundle.message("quick-fix.immutable-return-type"))
        myFixture.launchAction(intention)
        myFixture.checkResultByFile("ImmutableEntityの戻り値QuickFix_after.java")
    }
}