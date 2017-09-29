package siosio.doma

import com.intellij.compiler.*
import com.intellij.compiler.options.*
import com.intellij.notification.*
import com.intellij.openapi.components.*
import com.intellij.openapi.module.*
import com.intellij.openapi.project.*
import com.intellij.psi.*
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.openapi.options.*


class DomaProjectComponent(val project: Project) : AbstractProjectComponent(project) {

    companion object {
        val GROUP_DISPLAY_ID_INFO = NotificationGroup("doma support", NotificationDisplayType.BALLOON, true)
    }

    val annotationProcessorsConfigName = AnnotationProcessorsConfigurable(project).getDisplayName()

    override fun projectOpened() {
        if (JavaPsiFacade.getInstance(project).findPackage("org.seasar.doma") == null) {
            return
        }
        if (!isEnabledAnnotationProcessing()) {
            val notification = GROUP_DISPLAY_ID_INFO.createNotification(
                "doma",
                """今の設定では、IntelliJでビルドした際にDaoImplが出力されません。<br/>
                    | annotation processorsを有効にしてください。<br/>
                    | <a href='#'>設定画面へ</a>
                """.trimMargin(),
                NotificationType.ERROR,
                { _, _ ->
                    if (!project.isDisposed) {
                        ShowSettingsUtil.getInstance().showSettingsDialog(project, annotationProcessorsConfigName)
                    }
                })
            Notifications.Bus.notify(notification, project)

        }
    }

    private fun isEnabledAnnotationProcessing(): Boolean {
        val config = CompilerConfiguration.getInstance(project)
        val moduleManager = ModuleManager.getInstance(project)
        return moduleManager.modules
            .asSequence()
            .firstOrNull {
                config.getAnnotationProcessingConfiguration(it).isEnabled
            } != null
    }
}
