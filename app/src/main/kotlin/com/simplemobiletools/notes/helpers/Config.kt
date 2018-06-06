package com.simplemobiletools.notes.helpers

import android.content.Context
import android.os.Environment
import com.simplemobiletools.commons.helpers.BaseConfig

class Config(context: Context) : BaseConfig(context) {
    companion object {
        fun newInstance(context: Context) = Config(context)
    }

    var autosaveNotes: Boolean
        get() = prefs.getBoolean(AUTOSAVE_NOTES, true)
        set(autosaveNotes) = prefs.edit().putBoolean(AUTOSAVE_NOTES, autosaveNotes).apply()

    var displaySuccess: Boolean
        get() = prefs.getBoolean(DISPLAY_SUCCESS, false)
        set(displaySuccess) = prefs.edit().putBoolean(DISPLAY_SUCCESS, displaySuccess).apply()

    var clickableLinks: Boolean
        get() = prefs.getBoolean(CLICKABLE_LINKS, false)
        set(clickableLinks) = prefs.edit().putBoolean(CLICKABLE_LINKS, clickableLinks).apply()

    var monospacedFont: Boolean
        get() = prefs.getBoolean(MONOSPACED_FONT, false)
        set(monospacedFont) = prefs.edit().putBoolean(MONOSPACED_FONT, monospacedFont).apply()

    var showKeyboard: Boolean
        get() = prefs.getBoolean(SHOW_KEYBOARD, true)
        set(showKeyboard) = prefs.edit().putBoolean(SHOW_KEYBOARD, showKeyboard).apply()

    var showNotePicker: Boolean
        get() = prefs.getBoolean(SHOW_NOTE_PICKER, false)
        set(showNotePicker) = prefs.edit().putBoolean(SHOW_NOTE_PICKER, showNotePicker).apply()

    var showWordCount: Boolean
        get() = prefs.getBoolean(SHOW_WORD_COUNT, false)
        set(showWordCount) = prefs.edit().putBoolean(SHOW_WORD_COUNT, showWordCount).apply()

    var fontSize: Int
        get() = prefs.getInt(FONT_SIZE, FONT_SIZE_MEDIUM)
        set(size) = prefs.edit().putInt(FONT_SIZE, size).apply()

    var gravity: Int
        get() = prefs.getInt(GRAVITY, GRAVITY_LEFT)
        set(size) = prefs.edit().putInt(GRAVITY, size).apply()

    var currentNoteId: Int
        get() = prefs.getInt(CURRENT_NOTE_ID, 1)
        set(id) = prefs.edit().putInt(CURRENT_NOTE_ID, id).apply()

    var widgetNoteId: Int
        get() = prefs.getInt(WIDGET_NOTE_ID, 1)
        set(id) = prefs.edit().putInt(WIDGET_NOTE_ID, id).apply()

    var placeCursorToEnd: Boolean
        get() = prefs.getBoolean(CURSOR_PLACEMENT, true)
        set(placement) = prefs.edit().putBoolean(CURSOR_PLACEMENT, placement).apply()

    var enableLineWrap: Boolean
        get() = prefs.getBoolean(ENABLE_LINE_WRAP, true)
        set(enableLineWrap) = prefs.edit().putBoolean(ENABLE_LINE_WRAP, enableLineWrap).apply()

    var lastUsedExtension: String
        get() = prefs.getString(LAST_USED_EXTENSION, "txt")
        set(lastUsedExtension) = prefs.edit().putString(LAST_USED_EXTENSION, lastUsedExtension).apply()

    var lastUsedSavePath: String
        get() = prefs.getString(LAST_USED_SAVE_PATH, Environment.getExternalStorageDirectory().toString())
        set(lastUsedSavePath) = prefs.edit().putString(LAST_USED_SAVE_PATH, lastUsedSavePath).apply()

    var listNotesLayout : Boolean
        get() = prefs.getBoolean(LIST_NOTES_LAYOUT,false)
        set(listNotesLayout) = prefs.edit().putBoolean(LIST_NOTES_LAYOUT, listNotesLayout).apply()
}