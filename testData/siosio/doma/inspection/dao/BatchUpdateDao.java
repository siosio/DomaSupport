package dao;

import org.seasar.doma.*;
import org.seasar.doma.jdbc.*;

@Dao
public interface BatchUpdateDao {

    // デフォルトSQLファイルは不要
    @BatchUpdate
    int SQLファイル不要();

    // 明示的にSQLファイルの不要設定を
    @BatchUpdate(sqlFile = false)
    int SQLファイル不要2();

    // SQLが必要な場合でSQLファイルがある場合
    @BatchUpdate(sqlFile = true)
    int SQLファイルあり();

    // SQLが必要な場合でSQLファイルがない場合
    @BatchUpdate(sqlFile = true)
    int <error descr="SQLファイルがありません。">SQLファイルなし</error>();
}
