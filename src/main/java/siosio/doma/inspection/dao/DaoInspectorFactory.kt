package siosio.doma.inspection.dao

import siosio.doma.*

val insertMethodRule =
    rule {
        sql(false)
        
        // 引数チェック
        parameterRule { problemsHolder, daoMethod ->
            if (daoMethod.parameterList.parametersCount != 1) {
                problemsHolder.registerProblem(
                        daoMethod.parameterList, DomaBundle.message("inspection.dao.entity-param-not-found"))
            }
        }
    }

val updateMethodRule =
    rule {
        sql(false)
    }

val deleteMethodRule =
    rule {
        sql(false)
    }

val batchInsertMethodRule =
    rule {
        sql(false)
    }

val batchUpdateMethodRule =
    rule {
        sql(false)
    }

val batchDeleteMethodRule =
    rule {
        sql(false)
    }

val scriptMethodRule =
    rule {
        sql(true)
    }
