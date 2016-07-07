package siosio.doma.inspection.dao

import com.intellij.codeInspection.*
import com.intellij.codeInspection.compiler.*
import com.intellij.psi.util.*
import siosio.doma.*

val insertMethodRule =
    rule {
      sql(false)
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

val scriptMethodRule =
    rule {
      sql(true)
    }
