package dao;

import org.seasar.doma.*
import org.seasar.doma.jdbc.*
import siosio.doma.inspection.dao.entity.*

@Dao
interface InsertDaoKotlin {

    // デフォルトSQLファイルは不要
    @Insert
    fun SQLファイル不要(entity: MutableEntity ): Int

    // 明示的にSQLファイルの不要設定を
    @Insert(sqlFile = false)
    fun SQLファイル不要2(entity: MutableEntity ): Int

    // SQLが必要な場合でSQLファイルがある場合
    @Insert(sqlFile = true)
    fun SQLファイルあり(entity: MutableEntity ): Int

    // SQLが必要な場合でSQLファイルがない場合
    @Insert(sqlFile = true)
    fun <error descr="SQLファイルがありません。">SQLファイルなし</error>(entity: MutableEntity ): Int
    
}
