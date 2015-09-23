package siosio.doma;

import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.testFramework.IdeaTestUtil;
import com.intellij.testFramework.fixtures.DefaultLightProjectDescriptor;

public class DomaProjectDescriptor extends DefaultLightProjectDescriptor {

    @Override
    public Sdk getSdk() {
        return IdeaTestUtil.getMockJdk18();
    }
}
