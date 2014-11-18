package siosio.doma.inspection;

import com.intellij.codeInspection.InspectionToolProvider;

public class DaoInspectionToolProvider implements InspectionToolProvider {

    @Override
    public Class[] getInspectionClasses() {
        return new Class[]{DaoInspectionTool.class};
    }
}
