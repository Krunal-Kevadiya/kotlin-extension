package com.extensions.dialogs.customsnackbar

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.HapticFeedbackConstants.VIRTUAL_KEY
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.RelativeLayout
import com.extensions.dialogs.customsnackbar.CustomSnackbar.Companion.DURATION_INDEFINITE
import com.extensions.dialogs.customsnackbar.CustomSnackbar.DismissEvent
import com.extensions.dialogs.customsnackbar.CustomSnackbar.Vibration.DISMISS
import com.extensions.dialogs.customsnackbar.SwipeDismissTouchListener.DismissCallbacks
import com.extensions.dialogs.customsnackbar.anim.CustomSnackAnim
import com.extensions.dialogs.customsnackbar.anim.CustomSnackAnimBarBuilder
import com.extensions.dialogs.customsnackbar.anim.CustomSnackAnimIconBuilder
import com.extensions.dialogs.customsnackbar.util.NavigationBarPosition.*
import com.extensions.dialogs.customsnackbar.util.afterMeasured
import com.extensions.dialogs.customsnackbar.util.getNavigationBarPosition
import com.extensions.dialogs.customsnackbar.util.getNavigationBarSizeInPx
import com.extensions.dialogs.customsnackbar.util.getRootView

/**
 * Container withView matching the height and width of the parent to hold a CustomSnackbarView.
 * It will occupy the entire screens size but will be completely transparent. The
 * CustomSnackbarView inside is the only visible component in it.
 */
internal class CustomSnackbarContainerView(context: Context)
    : RelativeLayout(context), DismissCallbacks {

    internal lateinit var parentFlashbar:CustomSnackbar

    private lateinit var flashbarView:CustomSnackbarView

    private lateinit var enterAnimBuilder:CustomSnackAnimBarBuilder
    private lateinit var exitAnimBuilder:CustomSnackAnimBarBuilder
    private lateinit var vibrationTargets: List<CustomSnackbar.Vibration>

    private var onBarShowListener: CustomSnackbar.OnBarShowListener? = null
    private var onBarDismissListener: CustomSnackbar.OnBarDismissListener? = null
    private var onTapOutsideListener: CustomSnackbar.OnTapListener? = null
    private var overlayColor: Int? = null
    private var iconAnimBuilder: CustomSnackAnimIconBuilder? = null

    private var duration = DURATION_INDEFINITE
    private var isBarShowing = false
    private var isBarShown = false
    private var isBarDismissing = false
    private var barDismissOnTapOutside: Boolean = false
    private var showOverlay: Boolean = false
    private var overlayBlockable: Boolean = false

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            ACTION_DOWN -> {
                val rect = Rect()
                flashbarView.getHitRect(rect)

                // Checks if the tap was outside the bar
                if (!rect.contains(event.x.toInt(), event.y.toInt())) {
                    onTapOutsideListener?.onTap(parentFlashbar)

                    if (barDismissOnTapOutside) {
                        dismissInternal(DismissEvent.TAP_OUTSIDE)
                    }
                }
            }
        }
        return super.onInterceptTouchEvent(event)
    }

    override fun onSwipe(isSwiping: Boolean) {
        isBarDismissing = isSwiping
        if (isSwiping) {
            onBarDismissListener?.onDismissing(parentFlashbar, true)
        }
    }

    override fun onDismiss(view: View) {
        (parent as? ViewGroup)?.removeView(this@CustomSnackbarContainerView)
        isBarShown = false

        flashbarView.stopIconAnimation()

        if (vibrationTargets.contains(DISMISS)) {
            performHapticFeedback(VIRTUAL_KEY)
        }

        onBarDismissListener?.onDismissed(parentFlashbar, DismissEvent.SWIPE)
    }

    internal fun attach(flashbarView:CustomSnackbarView) {
        this.flashbarView = flashbarView
    }

    internal fun construct() {
        isHapticFeedbackEnabled = true

        if (showOverlay) {
            setBackgroundColor(overlayColor!!)

            if (overlayBlockable) {
                isClickable = true
                isFocusable = true
            }
        }

        addView(flashbarView)
    }

    internal fun addParent(flashbar:CustomSnackbar) {
        this.parentFlashbar = flashbar
    }

    internal fun adjustOrientation(activity: Activity) {
        val flashbarContainerViewLp = RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)

        val navigationBarPosition = activity.getNavigationBarPosition()
        val navigationBarSize = activity.getNavigationBarSizeInPx()

        when (navigationBarPosition) {
            LEFT -> flashbarContainerViewLp.leftMargin = navigationBarSize
            RIGHT -> flashbarContainerViewLp.rightMargin = navigationBarSize
            BOTTOM -> flashbarContainerViewLp.bottomMargin = navigationBarSize
        }

        layoutParams = flashbarContainerViewLp
    }


    internal fun show(activity: Activity) {
        if (isBarShowing || isBarShown) return

        val activityRootView = activity.getRootView() ?: return

        // Only add the withView to the parent once
        if (this.parent == null) activityRootView.addView(this)

        activityRootView.afterMeasured {
            val enterAnim = enterAnimBuilder.withView(flashbarView).build()
            enterAnim.start(object : CustomSnackAnim.InternalAnimListener {
                override fun onStart() {
                    isBarShowing = true
                    onBarShowListener?.onShowing(parentFlashbar)
                }

                override fun onUpdate(progress: Float) {
                    onBarShowListener?.onShowProgress(parentFlashbar, progress)
                }

                override fun onStop() {
                    isBarShowing = false
                    isBarShown = true

                    flashbarView.startIconAnimation(iconAnimBuilder)

                    if (vibrationTargets.contains(CustomSnackbar.Vibration.SHOW)) {
                        performHapticFeedback(VIRTUAL_KEY)
                    }

                    onBarShowListener?.onShown(parentFlashbar)
                }
            })

            handleDismiss()
        }
    }

    internal fun dismiss() {
        dismissInternal(DismissEvent.MANUAL)
    }

    internal fun isBarShowing() = isBarShowing

    internal fun isBarShown() = isBarShown

    internal fun setDuration(duration: Long) {
        this.duration = duration
    }

    internal fun setBarShowListener(listener: CustomSnackbar.OnBarShowListener?) {
        this.onBarShowListener = listener
    }

    internal fun setBarDismissListener(listener: CustomSnackbar.OnBarDismissListener?) {
        this.onBarDismissListener = listener
    }

    internal fun setBarDismissOnTapOutside(dismiss: Boolean) {
        this.barDismissOnTapOutside = dismiss
    }

    internal fun setOnTapOutsideListener(listener: CustomSnackbar.OnTapListener?) {
        this.onTapOutsideListener = listener
    }

    internal fun setOverlay(overlay: Boolean) {
        this.showOverlay = overlay
    }

    internal fun setOverlayColor(color: Int) {
        this.overlayColor = color
    }

    internal fun setOverlayBlockable(blockable: Boolean) {
        this.overlayBlockable = blockable
    }

    internal fun setEnterAnim(builder:CustomSnackAnimBarBuilder) {
        this.enterAnimBuilder = builder
    }

    internal fun setExitAnim(builder:CustomSnackAnimBarBuilder) {
        this.exitAnimBuilder = builder
    }

    internal fun enableSwipeToDismiss(enable: Boolean) {
        this.flashbarView.enableSwipeToDismiss(enable, this)
    }

    internal fun setVibrationTargets(targets: List<CustomSnackbar.Vibration>) {
        this.vibrationTargets = targets
    }

    internal fun setIconAnim(builder: CustomSnackAnimIconBuilder?) {
        this.iconAnimBuilder = builder
    }

    private fun handleDismiss() {
        if (duration != DURATION_INDEFINITE) {
            postDelayed({ dismissInternal(DismissEvent.TIMEOUT) }, duration)
        }
    }

    private fun dismissInternal(event: DismissEvent) {
        if (isBarDismissing || isBarShowing || !isBarShown) {
            return
        }

        val exitAnim = exitAnimBuilder.withView(flashbarView).build()
        exitAnim.start(object : CustomSnackAnim.InternalAnimListener {
            override fun onStart() {
                isBarDismissing = true
                onBarDismissListener?.onDismissing(parentFlashbar, false)
            }

            override fun onUpdate(progress: Float) {
                onBarDismissListener?.onDismissProgress(parentFlashbar, progress)
            }

            override fun onStop() {
                isBarDismissing = false
                isBarShown = false

                if (vibrationTargets.contains(DISMISS)) {
                    performHapticFeedback(VIRTUAL_KEY)
                }

                onBarDismissListener?.onDismissed(parentFlashbar, event)

                post { (parent as? ViewGroup)?.removeView(this@CustomSnackbarContainerView) }
            }
        })
    }
}
