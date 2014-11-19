package siosio.doma;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ResourceFileUtil;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.search.GlobalSearchScope;

/**
 * 便利メソッド群。
 */
public class DomaUtils {

    /** DAOクラスを表すアノテーションクラス名 */
    private static final String DAO_ANNOTATION_NAME = "org.seasar.doma.Dao";

    /** SELECTメソッドを表すアノテーション */
    private static final String DAO_SELECT_ANNOTATION = "org.seasar.doma.Select";

    /**
     * DAOクラスか否か。
     *
     * @param psiClass チェック対象のクラス。
     * @return DAOならtrue
     */
    public static boolean isDaoClass(PsiClass psiClass) {
        PsiModifierList modifierList = psiClass.getModifierList();
        if (modifierList == null) {
            return false;
        }
        PsiAnnotation[] annotations = modifierList.getAnnotations();
        for (PsiAnnotation annotation : annotations) {
            String name = annotation.getQualifiedName();
            if (DAO_ANNOTATION_NAME.equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * DAOのメソッドか否か
     *
     * @param method
     * @return
     */
    public static boolean isDaoMethod(PsiMethod method) {
        if (!isDaoClass(method.getContainingClass())) {
            return false;
        }

        PsiAnnotation[] annotations = method.getModifierList().getAnnotations();
        for (PsiAnnotation annotation : annotations) {
            if (DAO_SELECT_ANNOTATION.equals(annotation.getQualifiedName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * DAOのタイプへとな
     *
     * @param method
     * @return
     */
    public static DaoType toDaoType(PsiMethod method) {
        if (!isDaoClass(method.getContainingClass())) {
            return DaoType.INVALID;
        }

        PsiAnnotation[] annotations = method.getModifierList().getAnnotations();
        for (PsiAnnotation annotation : annotations) {
            if (DAO_SELECT_ANNOTATION.equals(annotation.getQualifiedName())) {
                return DaoType.SELECT;
            }
        }
        return DaoType.INVALID;
    }

    /**
     * SQLのファイルパスを生成する。
     *
     * @param method DAOメソッド
     * @return SQLのファイルパス(クラスパス配下からのパス)
     */
    public static String makeSqlFilePath(PsiMethod method) {
        return makeSqlFilePath(method.getContainingClass(), method);
    }


    /**
     * SQLのファイルパスを生成する。
     *
     * @param psiClass DAOクラス
     * @param method DAOメソッド
     * @return SQLのファイルパス(クラスパス配下からのパス)
     */
    public static String makeSqlFilePath(PsiClass psiClass, PsiMethod method) {
        return "META-INF/" + psiClass.getQualifiedName().replace('.', '/')
                + '/' + method.getName() + ".sql";
    }

    /**
     * SQLファイルを検索する。
     *
     * @param element
     * @param filePath
     * @return
     */
    public static VirtualFile findSqlFile(PsiElement element, String filePath) {
        Module module = ProjectRootManager.getInstance(element.getProject())
                .getFileIndex().getModuleForFile(element.getContainingFile().getVirtualFile());

        if (module == null) {
            return null;
        }

        GlobalSearchScope scope = GlobalSearchScope.moduleRuntimeScope(module, false);
        return ResourceFileUtil.findResourceFileInScope(filePath, element.getProject(), scope);
    }
}

