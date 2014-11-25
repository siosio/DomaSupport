package siosio.doma;

public enum DaoType {
    SELECT("org.seasar.doma.Select"),
    UPDATE("org.seasar.doma.Update"),
    INSERT("org.seasar.doma.Insert");

    private final String annotation;

    DaoType(String annotation) {
        this.annotation = annotation;
    }

    public String getAnnotation() {
        return annotation;
    }
}

