package siosio.doma


public enum class DaoType(
    val annotationName: String) {


  SELECT : DaoType("org.seasar.doma.Select")
  UPDATE : DaoType("org.seasar.doma.Update")
  INSERT : DaoType("org.seasar.doma.Insert")
  DELETE : DaoType("org.seasar.doma.Delete")
  BATCH_INSERT : DaoType("org.seasar.doma.BatchInsert")
}
