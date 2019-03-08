package dao;

import java.util.function.Function;
import java.util.stream.Stream;

import org.seasar.doma.*;
import org.seasar.doma.jdbc.*;


@Dao
interface SelectDaoKotlin {
    // SQLファイルが存在しているメソッド
    @Select
    fun SQLファイルあり(): Int

    // SQLファイルが存在しないメソッド
    @Select
    fun <error descr="SQLファイルがありません。">SQLファイルなし</error>(): Int

    // SelectOptionsなしはOK
    @Select
    fun selectOptionsなし(): String

    // SelectOptions1つはOK
    @Select
    fun selectOptions1つ(option1: SelectOptions ): String

    // SelectOptions複数はNG
    @Select
    fun selectOptions2つ(<error descr="SelectOptions型の引数は複数指定出来ません。">option1: SelectOptions</error>, <error descr="SelectOptions型の引数は複数指定出来ません。">option2: SelectOptions</error>): String


}