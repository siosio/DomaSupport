package dao;

import org.seasar.doma.*;
import org.seasar.doma.jdbc.*;

@Dao
public interface ScriptDao {
    // SQLファイルが存在しているメソッド
    @Script
    int SQLファイルあり();

    // SQLファイルが存在しないメソッド
    @Script
    int <error descr="SQLファイルがありません。">SQLファイルなし</error>();
}

