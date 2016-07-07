package dao;

import org.seasar.doma.*;
import org.seasar.doma.jdbc.*;

@Dao
public interface BatchDeleteDao {

    // デフォルトSQLファイルは不要
    @BatchDelete
    int SQLファイル不要();

    // 明示的にSQLファイルの不要設定を
    @BatchDelete(sqlFile = false)
    int SQLファイル不要2();

    // SQLが必要な場合でSQLファイルがある場合
    @BatchDelete(sqlFile = true)
    int SQLファイルあり();

    // SQLが必要な場合でSQLファイルがない場合
    @BatchDelete(sqlFile = true)
    int <error descr="SQLファイルがありません。">SQLファイルなし</error>();
}
