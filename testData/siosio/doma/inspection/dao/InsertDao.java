package dao;

import org.seasar.doma.*;
import org.seasar.doma.jdbc.*;
import entity.*;

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

    // MutableEntityの戻り値不正
    @Insert
    <error descr="戻り値は更新件数を示すintにしてください。">String</error> MutableEntityの戻りが不正(MutableEntity entity);
    
    // MutableEntityの戻り値不正
    @Insert
    <error descr="戻り値は更新件数を示すintにしてください。">void</error> MutableEntityの戻りが不正_void(MutableEntity entity);
    
    // ImmutableEntityの戻り値OK
    @Insert
    Result<ImmutableEntity> ImmutableEntityの戻り値がOK(ImmutableEntity entity);
    
    // ImmutableEntityの戻り値NG
    @Insert
    <error descr="戻り値はResult<entity.ImmutableEntity>にしてください。">int</error> ImmutableEntityの戻り値がNG(ImmutableEntity entity);
    
    // ImmutableEntityの戻り値の型パラメータがNG
    @Insert
    <error descr="戻り値はResult<entity.ImmutableEntity>にしてください。">Result<String></error> ImmutableEntityの戻り値の型パラメータがNG(ImmutableEntity entity);
    
    // sqlファイルありの場合でMutableEntityの場合の戻り値OK
    @Insert(sqlFile = true)
    int SQLファイルありのMutableEntity(int num, MutableEntity entity);
    
    // sqlファイルありの場合でMutableEntityの場合の戻り値NG
    @Insert(sqlFile = true)
    <error descr="戻り値は更新件数を示すintにしてください。">String</error> SQLファイルありのMutableEntity(String name, MutableEntity entity);

    // sqlファイルありの場合でMutableEntityの場合の戻り値OK
    @Insert(sqlFile = true)
    <error descr="戻り値はResult<entity.ImmutableEntity>にしてください。">int</error> SQLファイルありのImmutableEntity(int num, ImmutableEntity entity);
    
    // sqlファイルありの場合で引数にEntityがない場合
    @Insert(sqlFile = true)
    <error descr="戻り値は更新件数を示すintにしてください。">void</error> SQLファイルありのEntityなし(int num, String str);
    
}
