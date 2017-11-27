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
    int パラメータなし<error descr="引数にEntityを1つ指定してください。">()</error>;
    
    // 引数が複数の場合
    @Insert
    int パラメータなし<error descr="引数にEntityを1つ指定してください。">(MutableEntity entity1, MutableEntity entity2)</error>;
    
    // 引数が非Entityの場合
    @Insert
    int パラメータなし<error descr="引数にEntityを1つ指定してください。">(String entity)</error>;
    
    // SQLファイルありの場合は引数はEntity以外もOK
    @Insert(sqlFile = true)
    int SQLファイルあり(String name, int age);
}
