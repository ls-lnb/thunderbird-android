package com.fsck.k9.view

import android.content.Context
import android.content.SharedPreferences
import android.view.View

/**
 * Debug-only configuration to A/B test message view scrollbar rendering.
 *
 * This is deliberately not part of the regular settings model so the experiment can be removed
 * without a settings migration.
 *
 * See https://github.com/thunderbird/thunderbird-android/issues/10472
 */
class ScrollbarDebugConfig(context: Context) {
    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    /**
     * Whether the message WebView draws its own vertical scrollbar, in addition to the scrollbar of
     * the outer message scroll view.
     */
    var isWebViewScrollBarEnabled: Boolean
        get() = preferences.getBoolean(KEY_WEBVIEW_SCROLLBAR_ENABLED, DEFAULT_WEBVIEW_SCROLLBAR_ENABLED)
        set(value) = preferences.edit().putBoolean(KEY_WEBVIEW_SCROLLBAR_ENABLED, value).apply()

    /**
     * How scrollbar fading is applied to the message view. [ScrollBarFadingMode.DEFAULT] leaves the
     * value untouched, which is the behaviour before this experiment was added.
     */
    var scrollBarFadingMode: ScrollBarFadingMode
        get() = ScrollBarFadingMode.fromValue(preferences.getString(KEY_SCROLLBAR_FADING_MODE, null))
        set(value) = preferences.edit().putString(KEY_SCROLLBAR_FADING_MODE, value.value).apply()

    fun applyScrollBarFadingTo(view: View) {
        when (scrollBarFadingMode) {
            ScrollBarFadingMode.DEFAULT -> Unit
            ScrollBarFadingMode.ENABLED -> view.isScrollbarFadingEnabled = true
            ScrollBarFadingMode.DISABLED -> view.isScrollbarFadingEnabled = false
        }
    }

    fun registerListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        preferences.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        preferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    enum class ScrollBarFadingMode(val value: String) {
        DEFAULT("DEFAULT"),
        ENABLED("ENABLED"),
        DISABLED("DISABLED"),
        ;

        companion object {
            fun fromValue(value: String?): ScrollBarFadingMode {
                return entries.firstOrNull { it.value == value } ?: DEFAULT
            }
        }
    }

    companion object {
        private const val PREFERENCES_NAME = "scrollbar_debug"
        private const val KEY_WEBVIEW_SCROLLBAR_ENABLED = "webview_scrollbar_enabled"
        private const val KEY_SCROLLBAR_FADING_MODE = "scrollbar_fading_mode"

        private const val DEFAULT_WEBVIEW_SCROLLBAR_ENABLED = true
    }
}
