package com.globe.testproject.custom

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewPropertyAnimator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

class FooterViewBehavior(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<View>(context, attrs) {

    var dyDirectionSum: Int = 0
    var isShowing: Boolean? = null
    var isHiding: Boolean? = null

    val ANIMATION_DURATION = 200;
    val INTERPOLATOR = FastOutSlowInInterpolator();


    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: View, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {

        if (dy > 0 && dyDirectionSum!! < 0 || dy < 0 && dyDirectionSum!! > 0) {
            child.animate().cancel()
            dyDirectionSum = 0
        }

        dyDirectionSum = dyDirectionSum!! + dy

        if (dyDirectionSum!! > child.height) {
            hideView(child)

        } else if (dyDirectionSum!! < -child.height) {
            showView(child)

        }
    }


    fun hideView(view: View) {
        if (isHiding!! || view.visibility != View.VISIBLE) return
        val animator = view.animate()
                .translationY(view.height.toFloat())
                .setInterpolator(INTERPOLATOR)
                .setDuration(ANIMATION_DURATION.toLong())
        animator.setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                isHiding = false
                view.visibility = View.INVISIBLE
            }

            override fun onAnimationCancel(animation: Animator?) {
                isHiding = false
                showView(view)
            }

            override fun onAnimationStart(animation: Animator?) {
                isHiding = true;
            }
        })
        animator.start()
    }

    fun showView(view: View) {
        if (isShowing!! || view.visibility == View.VISIBLE) return
        val animator = view.animate()
                .translationY(0F)
                .setInterpolator(INTERPOLATOR)
                .setDuration(ANIMATION_DURATION.toLong())
        animator.setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                isShowing = false
            }

            override fun onAnimationCancel(animation: Animator?) {
                isShowing = false
                hideView(view)
            }

            override fun onAnimationStart(animation: Animator?) {
                isShowing = true
                view.visibility = View.VISIBLE
            }
        })
        animator.start()
    }
}
