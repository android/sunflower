package com.google.samples.apps.sunflower.utilities

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor

/**
 * Helper methods for serves as a central point to execute common tasks, from
 * https://android.googlesource.com/platform/frameworks/support/+/refs/tags/android-p-preview-1/app-toolkit/runtime/src/main/java/android/arch/core/executor/ArchTaskExecutor.java
 *
 * Sets a delegate to handle task execution requests using TaskExecutor as the delegate.
 * App Toolkit components will use a TaskExecutors.
 */
fun registerTaskExecutor() {
    ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
        override fun executeOnDiskIO(runnable: Runnable) = runnable.run()
        override fun isMainThread() = true
        override fun postToMainThread(runnable: Runnable) = runnable.run()
    })
}

/**
 * Helper methods for serves as a central point to execute common tasks, from
 * https://android.googlesource.com/platform/frameworks/support/+/refs/tags/android-p-preview-1/app-toolkit/runtime/src/main/java/android/arch/core/executor/ArchTaskExecutor.java
 *
 * Sets the default delegate to handle task execution requests.
 * App Toolkit components will use the default TaskExecutor.
 */
fun unRegisterTaskExecutor() {
    ArchTaskExecutor.getInstance().setDelegate(null)
}