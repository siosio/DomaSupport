package dao;

import org.seasar.doma.*;
import org.seasar.doma.jdbc.*;
import siosio.doma.inspection.dao.entity.*;

@Dao
public interface InsertDao {

    // デフォルトSQLファイルは不要
    @Insert
    int SQLファイル不要(MutableEntity entity);

    // 明示的にSQLファイルの不要設定を
    @Insert(sqlFile = false)
    int SQLファイル不要2(MutableEntity entity);

    // SQLが必要な場合でSQLファイルがある場合
    @Insert(sqlFile = true)
    int SQLファイルあり(MutableEntity entity);

    // SQLが必要な場合でSQLファイルがない場合
    @Insert(sqlFile = true)
    int <error descr="SQLファイルがありません。">SQLファイルなし</error>(MutableEntity entity);
    
    // 引数がないの場合
    @Insert
    int パラメータなし<error descr="引数にEntityを指定してください。">()</error>;
}
