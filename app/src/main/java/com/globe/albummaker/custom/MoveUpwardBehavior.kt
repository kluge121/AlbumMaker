package com.globe.testproject.custom

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar


open class MoveUpwardBehavior : CoordinatorLayout.Behavior<View>() {

    private val SNACKBAR_BEHAVIOR_ENABLED: Boolean = false
    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return SNACKBAR_BEHAVIOR_ENABLED && dependency is Snackbar.SnackbarLayout;
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        val translationY = Math.min(0f, dependency.translationY - dependency.height)
        child.translationY = translationY
        return true
    }
}