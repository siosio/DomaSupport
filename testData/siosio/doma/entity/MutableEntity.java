package entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;

@Entity
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
