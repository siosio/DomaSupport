package dao.quickfix;

import org.seasar.doma.*;
import org.seasar.doma.jdbc.*;
import entity.*;

@Dao
interface ImmutableEntitiの戻り値QuickFix {
    
    @Insert
    Result<ImmutableEntity> insert(ImmutableEntity entity);
    
}