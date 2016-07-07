# IntelliJ IDEAのDomaサポートプラグイン
IntelliJ上で、[Doma](http://doma.seasar.org/)を使った開発を少しだけ便利にすることができるプラグイン


## 機能
このプラグインで実装できている機能について。

### DAO関連
* SQLファイルへの移動

  DAOのメソッドからSQLファイルへの移動が出来ます。
  
  SQLファイルが存在している場合、該当メソッドのGutter部(行番号の右側の部分)にSQLアイコンが表示されます。  
  このアイコンをクリックすることで、関連するSQLへ移動できます。
  
* SQLファイルの存在チェック
  以下のメソッドのSQLファイル存在チェックを行います。
  SQLファイルが存在しない場合、QuickFixによりSQLファイルを作成できます。
 
  * Selectメソッド
  * 更新(Insert、Update、Delete、BatchInsert、BatchUpdate、BatchDelete)メソッド  
    ※SQLファイルが必要な場合のみ(sqlFile=trueの場合のみ)

* 引数のチェック
  * Selectメソッド
    * SelectOptions型の引数が最大でも1つであることのチェックが行われます。  
       Quick Fixで、引数を削除することが出来ます。
    * strategyにSelectType.STREAMを指定時に引数にFunctionが定義されているかチェックします。


# ライセンス
This software is released under the MIT License, see LICENSE.txt.
