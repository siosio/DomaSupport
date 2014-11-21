package siosio.doma;

public enum DaoType {
    SELECT("org.seasar.doma.Select"),
    UPDATE("org.seasar.doma.Update"),;

    private final String annotation;

    DaoType(String annotation) {
        this.annotation = annotation;
    }

    public String getAnnotation() {
        return annotation;
    }
}

