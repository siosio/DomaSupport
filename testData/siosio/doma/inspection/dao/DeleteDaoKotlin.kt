package dao;

import org.seasar.doma.*;
import org.seasar.doma.jdbc.*;
import entity.*;

@Dao
interface DeleteDaoKotlin {

    // デフォルトSQLファイルは不要
    @Delete
    fun SQLファイル不要(entity: MutableEntity): Int

    // 明示的にSQLファイルの不要設定を
    @Delete(sqlFile = false)
    fun SQLファイル不要2(entity: MutableEntity): Int

    // SQLが必要な場合でSQLファイルがある場合
    @Delete(sqlFile = true)
    fun SQLファイルあり(entity: MutableEntity): Int

    // SQLが必要な場合でSQLファイルがない場合
    @Delete(sqlFile = true)
    fun <error descr="SQLファイルがありません。">SQLファイルなし</error>(entity: MutableEntity): Int
    
}
