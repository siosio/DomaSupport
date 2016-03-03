package siosio.doma;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.pom.java.LanguageLevel;
import com.intellij.testFramework.IdeaTestUtil;
import com.intellij.testFramework.fixtures.DefaultLightProjectDescriptor;
import org.jetbrains.annotations.NotNull;

public class DomaProjectDescriptor extends DefaultLightProjectDescriptor {

    @Override
    public Sdk getSdk() {
        return IdeaTestUtil.getMockJdk18();
    }

    @Override
    public void configureModule(@NotNull Module module, @NotNull ModifiableRootModel model,
            @NotNull ContentEntry contentEntry) {
        IdeaTestUtil.setModuleLanguageLevel(module, LanguageLevel.JDK_1_8);
    }
}
