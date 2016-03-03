package dao;

import java.lang.*;
import java.util.function.*;
import java.util.stream.*;

import org.seasar.doma.*;
import org.seasar.doma.jdbc.*;


@Dao
public interface SelectDao {
    // SQLファイルが存在しているメソッド
    @Select
    int SQLファイルあり();

    // SQLファイルが存在しないメソッド
    @Select
    int <error descr="SQLファイルがありません。">SQLファイルなし</error>();

    // SelectOptionsなしはOK
    @Select
    int selectOptionsなし();

    // SelectOptions1つはOK
    @Select
    int selectOptions1つ(SelectOptions option1);

    // SelectOptions複数はNG
    @Select
    int selectOptions2つ(<error descr="SelectOptions型の引数は複数指定出来ません。">SelectOptions option1</error>, <error descr="SelectOptions型の引数は複数指定出来ません。">SelectOptions option2</error>);

    // SelectType.STREAMでメソッドの引数にjava.util.Function<Stream<TARGET>, RESULT>がない場合
    <error descr="引数にjava.util.Function<Stream<TARGET>, RESULT>(サブタイプ含む)を1つ指定してください。">@Select(strategy = SelectType.STREAM)</error>
    String 引数にFunctionなし(String name);

    <error descr="引数にjava.util.Function<Stream<TARGET>, RESULT>(サブタイプ含む)を1つ指定してください。">@Select(strategy = SelectType.STREAM)</error>
    String 引数にFunction複数(String name, Function<Stream<String>, String> mapper, Function<Stream<String>, String> mapper2);

    String 引数にFunctionあり(String name, Function<Stream<String>, String> mapper);

}