package siosio.doma.inspection.dao.entity;

import org.seasar.doma.Id;

@org.seasar.doma.Entity
public class MutableEntity {

    @Id
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }
}
