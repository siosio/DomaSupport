package dao;

import org.seasar.doma.*;
import org.seasar.doma.jdbc.*;
import entity.*;

import java.util.*;

@Dao
public interface BatchInsertDao {

    // デフォルトSQLファイルは不要
    @BatchInsert
    int SQLファイル不要(List<MutableEntity> entities);

    // 明示的にSQLファイルの不要設定を
    @BatchInsert(sqlFile = false)
    int SQLファイル不要2(List<MutableEntity> entities);

    // SQLが必要な場合でSQLファイルがある場合
    @BatchInsert(sqlFile = true)
    int SQLファイルあり(List<MutableEntity> entities);

    // SQLが必要な場合でSQLファイルがない場合
    @BatchInsert(sqlFile = true)
    int <error descr="SQLファイルがありません。">SQLファイルなし</error>(

    List<MutableEntity> entities);

    @BatchInsert

    int[] 引数がIterableではない<error descr="引数にはIterableのサブタイプで要素にEntityを指定してください。">(

    String error)</error>;
}
