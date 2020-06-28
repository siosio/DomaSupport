package siosio.doma.inspection.dao.entity

import org.seasar.doma.Entity
import org.seasar.doma.Id

@Entity
data class MutableEntityKt(
    @Id
    val id: Long
)
