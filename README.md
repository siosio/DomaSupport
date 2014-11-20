# IntelliJ IDEAのDomaサポートプラグイン
IntelliJ上で、[Doma](http://doma.seasar.org/)を使った開発を少しだけ便利にすることができるプラグイン


## 機能
このプラグインで実装できている機能について。

### DAO関連
* SQLファイルへの移動

  DAOのメソッドからSQLファイルへの移動が出来ます。
  
  SQLファイルが存在している場合、該当メソッドのGutter部(行番号の右側の部分)にSQLアイコンが表示されます。
  このアイコンをクリックすることで、関連するSQLへ移動できます。

* Selectメソッド

  * SQLファイルの存在チェック
  
    SQLファイルが存在しているかのチェックが行われます。
    SQLファイルが存在していない場合、Quick Fixで空のSQLファイルの作成が行えます。
    
  * 引数のチェック
  
    SelectOptions型の引数が最大でも1つであることのチェックが行われます。
    Quick Fixで、引数を削除することが出来ます。

# ライセンス
This software is released under the MIT License, see LICENSE.txt.
