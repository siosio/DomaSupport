package siosio.doma.editor;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;
import siosio.doma.DaoType;
import siosio.doma.DomaBundle;
import siosio.doma.DomaUtils;

import javax.swing.*;
import java.util.Collection;
import java.util.List;

/**
 * DAOのメソッドからSQLファイルへの移動を実現するやつ
 */
public class DaoMethodLineMarkerProvider extends RelatedItemLineMarkerProvider {

    /**
     * SQLファイルのアイコン
     */
    private static final Icon SQL_FILE_ICON = IconLoader.getIcon("/icons/sql.png");

    @Override
    public void collectNavigationMarkers(List<PsiElement> elements,
                                         Collection<? super RelatedItemLineMarkerInfo> result, boolean forNavigation) {
        super.collectNavigationMarkers(elements, result, forNavigation);
    }

    @Override
    protected void collectNavigationMarkers(
            @NotNull PsiElement element,
            Collection<? super RelatedItemLineMarkerInfo> result) {

        if (!(element instanceof PsiMethod)) {
            return;
        }

        VirtualFile file = makeSqlFilePath((PsiMethod) element);
        if (file == null) {
            return;
        }

        PsiFile psiFile = PsiManager.getInstance(element.getProject()).findFile(file);
        NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder
                .create(SQL_FILE_ICON)
                .setTargets(psiFile)
                .setTooltipText(DomaBundle.message("editor.goto-sql-file"));
        result.add(builder.createLineMarkerInfo(element));
    }

    /**
     * メソッドに対応したSQLファイルのパスを取得する。
     *
     * @param method メソッド
     * @return SQLファイルのパス
     */
    private VirtualFile makeSqlFilePath(PsiMethod method) {
        DaoType type = DomaUtils.toDaoType(method);
        if (type == null) {
            return null;
        }
        String slqFilePath = DomaUtils.makeSqlFilePath(method);
        return DomaUtils.findSqlFile(method, slqFilePath);
    }
}

