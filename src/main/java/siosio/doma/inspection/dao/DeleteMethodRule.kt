package siosio.doma.inspection.dao

val deleteMethodRule =
    rule {
        sql(false)

        // 引数チェック
        parameterRule(parameterTypeCheck)

        returnRule(updateMethodWithImmutableEntityReturnRule)
    }
