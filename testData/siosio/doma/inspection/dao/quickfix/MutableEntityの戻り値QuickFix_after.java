package dao.quickfix;

import org.seasar.doma.*;
import entity.*;

@Dao
interface ImmutableEntitiの戻り値QuickFix {
    
    @Insert
    int insert(MutableEntity entity);
    
}