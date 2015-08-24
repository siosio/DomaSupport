package dao;

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

}

