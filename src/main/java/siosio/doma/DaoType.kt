package siosio.doma

import siosio.doma.inspection.*
import siosio.doma.inspection.dao.*
import siosio.doma.psi.*


public enum class DaoType(val annotationName: String) {

  SELECT("org.seasar.doma.Select") {
    override fun rule(): DaoInspectionRule = selectMethodRule
  },
  UPDATE("org.seasar.doma.Update") {
    override fun rule(): DaoInspectionRule = updateMethodRule
  },
  INSERT("org.seasar.doma.Insert") {
    override fun rule(): DaoInspectionRule = insertMethodRule
  },
  DELETE("org.seasar.doma.Delete") {
    override fun rule(): DaoInspectionRule = deleteMethodRule
  },
  BATCH_INSERT("org.seasar.doma.BatchInsert") {
    override fun rule(): DaoInspectionRule = batchInsertMethodRule
  },
  SCRIPT("org.seasar.doma.Script") {
    override fun rule(): DaoInspectionRule = scriptMethodRule
  };

  abstract fun rule(): DaoInspectionRule
}
