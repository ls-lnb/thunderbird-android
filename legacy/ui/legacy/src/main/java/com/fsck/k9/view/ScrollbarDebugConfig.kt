package com.fsck.k9.view

import android.content.Context
import android.content.SharedPreferences

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
     * Whether scrollbars in the message view fade out when idle.
     */
    var isScrollBarFadingEnabled: Boolean
        get() = preferences.getBoolean(KEY_SCROLLBAR_FADING_ENABLED, DEFAULT_SCROLLBAR_FADING_ENABLED)
        set(value) = preferences.edit().putBoolean(KEY_SCROLLBAR_FADING_ENABLED, value).apply()

    fun registerListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        preferences.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        preferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    companion object {
        private const val PREFERENCES_NAME = "scrollbar_debug"
        private const val KEY_WEBVIEW_SCROLLBAR_ENABLED = "webview_scrollbar_enabled"
        private const val KEY_SCROLLBAR_FADING_ENABLED = "scrollbar_fading_enabled"

        private const val DEFAULT_WEBVIEW_SCROLLBAR_ENABLED = true
        private const val DEFAULT_SCROLLBAR_FADING_ENABLED = true
    }
}
