package siosio.doma.editor;

import java.util.Collection;
import java.util.List;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiMethod;
import siosio.doma.DaoType;
import siosio.doma.DomaBundle;
import siosio.doma.DomaUtils;

/**
 * DAOのメソッドからSQLファイルへの移動を実現するやつ
 */
public class DaoMethodLineMarkerProvider extends RelatedItemLineMarkerProvider {

    @Override
    public void collectNavigationMarkers(List<PsiElement> elements,
            Collection<? super RelatedItemLineMarkerInfo> result, boolean forNavigation) {
        super.collectNavigationMarkers(elements, result, forNavigation);
    }

    @Override
    protected void collectNavigationMarkers(PsiElement element, Collection<? super RelatedItemLineMarkerInfo> result) {
        if (!(element instanceof PsiMethod)) {
            return;
        }

        DaoType type = DomaUtils.toDaoType((PsiMethod) element);
        if (type == DaoType.INVALID) {
            return;
        }

        String slqFilePath = DomaUtils.makeSqlFilePath((PsiMethod) element);
        VirtualFile file = DomaUtils.findSqlFile(element, slqFilePath);
        if (file == null) {
            return;
        }
        PsiFile psiFile = PsiManager.getInstance(element.getProject()).findFile(file);

        NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(AllIcons.FileTypes.Custom).
                setTargets(psiFile).
                setTooltipText(DomaBundle.message("editor.goto-sql-file"));
        result.add(builder.createLineMarkerInfo(element));
    }
}

