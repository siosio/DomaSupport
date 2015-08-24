package dao;

import org.seasar.doma.*;
import org.seasar.doma.jdbc.*;

@Dao
public interface DeleteDao {

    // デフォルトSQLファイルは不要
    @Delete
    int SQLファイル不要();

    // 明示的にSQLファイルの不要設定を
    @Delete(sqlFile = false)
    int SQLファイル不要2();

    // SQLが必要な場合でSQLファイルがある場合
    @Delete(sqlFile = true)
    int SQLファイルあり();

    // SQLが必要な場合でSQLファイルがない場合
    @Delete(sqlFile = true)
    int <error descr="SQLファイルがありません。">SQLファイルなし</error>();
}
