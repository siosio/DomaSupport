package siosio.doma;

import siosio.doma.inspection.DaoMethodInspector;
import siosio.doma.inspection.DeleteMethodInspector;
import siosio.doma.inspection.InsertMethodInspector;
import siosio.doma.inspection.SelectMethodInspector;
import siosio.doma.inspection.UpdateMethodInspector;

public enum DaoType {
    SELECT("org.seasar.doma.Select", SelectMethodInspector.getInspection()),
    UPDATE("org.seasar.doma.Update", UpdateMethodInspector.getInstance()),
    INSERT("org.seasar.doma.Insert", InsertMethodInspector.getInstance()),
    DELETE("org.seasar.doma.Delete", DeleteMethodInspector.getInstance());

    /** アノテーションの完全修飾名 */
    private final String annotation;

    /** DAOメソッドに対応したInspectorクラス */
    private DaoMethodInspector inspector;

    DaoType(String annotation, DaoMethodInspector inspector) {
        this.annotation = annotation;
        this.inspector = inspector;
    }

    public String getAnnotation() {
        return annotation;
    }

    public DaoMethodInspector getInspector() {
        return inspector;
    }
}

