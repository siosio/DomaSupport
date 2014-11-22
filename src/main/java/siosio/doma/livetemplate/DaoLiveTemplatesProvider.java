package siosio.doma.livetemplate;

import com.intellij.codeInsight.template.impl.DefaultLiveTemplatesProvider;
import org.jetbrains.annotations.Nullable;

public class DaoLiveTemplatesProvider implements DefaultLiveTemplatesProvider {

    @Override
    public String[] getDefaultLiveTemplateFiles() {
        System.out.println("DaoLiveTemplatesProvider.getDefaultLiveTemplateFiles");
        return new String[]{"doma.xml"};
    }

    @Nullable
    @Override
    public String[] getHiddenLiveTemplateFiles() {
        return new String[0];
    }
}
