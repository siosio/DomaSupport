package siosio.doma;

import com.intellij.codeInsight.AnnotationUtil;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ResourceFileUtil;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;

/**
 * 便利メソッド群。
 */
public class DomaUtils {

    /** DAOクラスを表すアノテーションクラス名 */
    private static final String DAO_ANNOTATION_NAME = "org.seasar.doma.Dao";

    /**
     * DAOクラスか否か。
     *
     * @param psiClass チェック対象のクラス。
     * @return DAOならtrue
     */
    public static boolean isDaoClass(PsiClass psiClass) {
        return AnnotationUtil.isAnnotated(psiClass, DAO_ANNOTATION_NAME, false);
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

        DaoType daoType = toDaoType(method);
        return daoType != null;
    }

    /**
     * DAOのタイプへとな
     *
     * @param method メソッド
     * @return DAOのタイプを取得する
     */
    public static DaoType toDaoType(PsiMethod method) {
        if (!isDaoClass(method.getContainingClass())) {
            return null;
        }

        for (DaoType daoType : DaoType.values()) {
            if (AnnotationUtil.isAnnotated(method, daoType.getAnnotation(), false)) {
                return daoType;
            }
        }
        return null;
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
     * @param method メソッド
     * @param filePath SQLのファイルパス
     * @return SQLファイルを表すVirtualFile(存在しない場合は、null)
     */
    public static VirtualFile findSqlFile(PsiMethod method, String filePath) {
        Module module = ProjectRootManager.getInstance(method.getProject())
                .getFileIndex().getModuleForFile(method.getContainingFile().getVirtualFile());

        if (module == null) {
            return null;
        }

        GlobalSearchScope scope = GlobalSearchScope.moduleRuntimeScope(module, false);
        return ResourceFileUtil.findResourceFileInScope(filePath, method.getProject(), scope);
    }
}

