package dao.quickfix;

import org.seasar.doma.*;
import org.seasar.doma.jdbc.*;
import entity.*;

@Dao
interface ImmutableEntitiの戻り値QuickFix {
    
    @Insert
    <caret>int insert(ImmutableEntity entity);
    
}