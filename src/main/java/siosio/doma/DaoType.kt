package siosio.doma

import siosio.doma.inspection.*
import siosio.doma.inspection.dao.*
import siosio.doma.psi.*


public enum class DaoType(val annotationName: String, val rule: DaoInspectionRule, val extension: String = "sql") {

  SELECT("org.seasar.doma.Select", selectMethodRule),
  UPDATE("org.seasar.doma.Update", updateMethodRule),
  INSERT("org.seasar.doma.Insert", insertMethodRule),
  DELETE("org.seasar.doma.Delete", deleteMethodRule),
  BATCH_INSERT("org.seasar.doma.BatchInsert", batchInsertMethodRule),
  SCRIPT("org.seasar.doma.Script", scriptMethodRule, "script")
}
