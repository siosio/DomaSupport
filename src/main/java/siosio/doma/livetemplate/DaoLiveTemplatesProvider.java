package siosio.doma.livetemplate;

import com.intellij.codeInsight.template.impl.DefaultLiveTemplatesProvider;
import org.jetbrains.annotations.Nullable;

/**
 * Live Templatesを提供する。
 */
public class DaoLiveTemplatesProvider implements DefaultLiveTemplatesProvider {

    @Override
    public String[] getDefaultLiveTemplateFiles() {
        return new String[]{"doma.xml"};
    }

    @Nullable
    @Override
    public String[] getHiddenLiveTemplateFiles() {
        return new String[0];
    }
}
