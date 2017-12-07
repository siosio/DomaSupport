package dao;

import org.seasar.doma.*;
import org.seasar.doma.jdbc.*;
import entity.*;

@Dao
public interface DeleteDao {

    // デフォルトSQLファイルは不要
    @Delete
    int SQLファイル不要(MutableEntity entity);

    // 明示的にSQLファイルの不要設定を
    @Delete(sqlFile = false)
    int SQLファイル不要2(MutableEntity entity);

    // SQLが必要な場合でSQLファイルがある場合
    @Delete(sqlFile = true)
    int SQLファイルあり(MutableEntity entity);

    // SQLが必要な場合でSQLファイルがない場合
    @Delete(sqlFile = true)
    int <error descr="SQLファイルがありません。">SQLファイルなし</error>(MutableEntity entity);
    
    // 引数がないの場合
    @Delete
    int パラメータなし<error descr="引数にEntityを1つ指定してください。">()</error>;

    // 引数が複数の場合
    @Delete
    int パラメータなし<error descr="引数にEntityを1つ指定してください。">(MutableEntity entity1, MutableEntity entity2)</error>;

    // 引数が非Entityの場合
    @Delete
    int パラメータなし<error descr="引数にEntityを1つ指定してください。">(String entity)</error>;
    
    // SQLファイルありの場合は引数はEntity以外もOK
    @Delete(sqlFile = true)
    int SQLファイルあり(String name, Long version);
}
