package com.singularitycoder.materiallooksxml

import android.app.Activity
import android.app.Application
import android.os.Bundle

class MaterialApp : Application() {

    override fun onCreate() {
        super.onCreate()
        stuffToDoOnAllActivities()
    }

    // https://stackoverflow.com/questions/28606689/how-to-prevent-screen-capture-in-android
    private fun stuffToDoOnAllActivities() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                // Disable screenshot on all activities
                activity.window.disableScreenshot()
            }

            override fun onActivityStarted(activity: Activity) = Unit
            override fun onActivityResumed(activity: Activity) = Unit
            override fun onActivityPaused(activity: Activity) = Unit
            override fun onActivityStopped(activity: Activity) = Unit
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
            override fun onActivityDestroyed(activity: Activity) = Unit
        })
    }
}