package entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;

@Entity(immutable = true)
public class ImmutableEntity {

    @Id
    public final Long id;

    public ImmutableEntity(final Long id) {
        this.id = id;
    }
}
