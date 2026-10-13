package com.fsck.k9

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.After
import org.junit.Test
import org.koin.core.component.inject

/**
 * Verifies that [K9.loadPrefs] restores the persisted font sizes on startup.
 *
 * Regression test for https://github.com/thunderbird/thunderbird-android/issues/9709:
 * font sizes were no longer loaded from storage, so changes made in the "Font size" settings
 * were lost after an app restart.
 */
class K9FontSizesTest : K9RobolectricTest() {
    private val testSubject: FontSizes
        get() = K9.fontSizes

    private val preferences: Preferences by inject()

    @After
    fun tearDown() {
        testSubject.messageListSubject = FontSizes.FONT_DEFAULT
        testSubject.messageListPreview = FontSizes.FONT_DEFAULT
        testSubject.messageViewContentAsPercent = DEFAULT_CONTENT_SIZE_IN_PERCENT
    }

    @Test
    fun `loadPrefs() should load font sizes from storage`() {
        testSubject.messageListSubject = FontSizes.FONT_20SP
        testSubject.messageListPreview = FontSizes.FONT_12SP
        testSubject.messageViewContentAsPercent = MESSAGE_VIEW_CONTENT_PERCENT
        saveFontSizesToStorage()

        testSubject.messageListSubject = FontSizes.FONT_DEFAULT
        testSubject.messageListPreview = FontSizes.FONT_DEFAULT
        testSubject.messageViewContentAsPercent = DEFAULT_CONTENT_SIZE_IN_PERCENT

        K9.loadPrefs(preferences.storage)

        assertThat(testSubject.messageListSubject).isEqualTo(FontSizes.FONT_20SP)
        assertThat(testSubject.messageListPreview).isEqualTo(FontSizes.FONT_12SP)
        assertThat(testSubject.messageViewContentAsPercent).isEqualTo(MESSAGE_VIEW_CONTENT_PERCENT)
    }

    private fun saveFontSizesToStorage() {
        val editor = preferences.createStorageEditor()
        testSubject.save(editor)
        editor.commit()
    }

    companion object {
        private const val DEFAULT_CONTENT_SIZE_IN_PERCENT = 100
        private const val MESSAGE_VIEW_CONTENT_PERCENT = 180
    }
}
