package dao;

import org.seasar.doma.*;
import org.seasar.doma.jdbc.*;

@Dao
public interface UpdateDao {

    // デフォルトSQLファイルは不要
    @Update
    int SQLファイル不要();

    // 明示的にSQLファイルの不要設定を
    @Update(sqlFile = false)
    int SQLファイル不要2();

    // SQLが必要な場合でSQLファイルがある場合
    @Update(sqlFile = true)
    int SQLファイルあり();

    // SQLが必要な場合でSQLファイルがない場合
    @Update(sqlFile = true)
    int <error descr="SQLファイルがありません。">SQLファイルなし</error>();
}
