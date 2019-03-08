package dao;

import org.seasar.doma.*
import org.seasar.doma.jdbc.*
import entity.*

@Dao
interface UpdateDaoKotlin {

    // デフォルトSQLファイルは不要
    @Update
    fun SQLファイル不要(entity: MutableEntity): Int 

    // 明示的にSQLファイルの不要設定を
    @Update(sqlFile = false)
    fun SQLファイル不要2(entity: MutableEntity): Int

    // SQLが必要な場合でSQLファイルがある場合
    @Update(sqlFile = true)
    fun SQLファイルあり(entity: MutableEntity): Int

    // SQLが必要な場合でSQLファイルがない場合
    @Update(sqlFile = true)
    fun <error descr="SQLファイルがありません。">SQLファイルなし</error>(entity: MutableEntity): Int
    
}
