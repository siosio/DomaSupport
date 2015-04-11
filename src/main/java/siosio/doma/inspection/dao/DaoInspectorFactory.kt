package siosio.doma.inspection.dao

import siosio.doma.DaoType

class DaoInspectorFactory {
  companion object {

    fun createDaoMethodInspector(daoType:DaoType):Dao {
      when (daoType) {
        DaoType.SELECT ->
          return createSelectMethod()
        DaoType.INSERT ->
          return createInsertMethod()
        DaoType.UPDATE ->
          return createUpdateMethod()
        DaoType.DELETE ->
          return createDeleteMethod()
        else -> throw IllegalArgumentException("invalid dao type.")
      }
    }

    private fun createSelectMethod() =
        Dao.dao {
          sql(required = true)

          parameter {
            type("org.seasar.doma.jdbc.SelectOptions", min = 0, max = 1)
          }
        }

    private fun createInsertMethod() =
        Dao.dao {
          sql(required = false)
        }

    private fun createUpdateMethod() =
        Dao.dao {
          sql(required = false)
        }

    private fun createDeleteMethod() =
        Dao.dao {
          sql(required = false)
        }
  }
}
