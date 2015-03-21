package siosio.doma;


public enum DaoType {
    SELECT("org.seasar.doma.Select"),
    UPDATE("org.seasar.doma.Update"),
    INSERT("org.seasar.doma.Insert"),
    DELETE("org.seasar.doma.Delete"),
    BATCH_INSERT("org.seasar.doma.BatchInsert");

    /** アノテーションの完全修飾名 */
    private final String annotation;

    DaoType(String annotation) {
        this.annotation = annotation;
    }

    public String getAnnotation() {
        return annotation;
    }
}

