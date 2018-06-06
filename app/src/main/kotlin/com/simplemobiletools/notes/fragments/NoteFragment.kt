package com.simplemobiletools.notes.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.text.util.Linkify
import android.util.TypedValue
import android.view.*
import com.simplemobiletools.commons.extensions.beGone
import com.simplemobiletools.commons.extensions.beVisible
import com.simplemobiletools.commons.extensions.onGlobalLayout
import com.simplemobiletools.notes.R
import com.simplemobiletools.notes.activities.MainActivity
import com.simplemobiletools.notes.extensions.config
import com.simplemobiletools.notes.extensions.dbHelper
import com.simplemobiletools.notes.extensions.getTextSize
import com.simplemobiletools.notes.extensions.updateWidget
import com.simplemobiletools.notes.helpers.*
import com.simplemobiletools.notes.models.Note
import kotlinx.android.synthetic.main.fragment_note.*
import kotlinx.android.synthetic.main.fragment_note.view.*
import kotlinx.android.synthetic.main.note_view_horiz_scrollable.view.*
import java.io.File

class NoteFragment : Fragment() {
    private var noteId = 0
    lateinit var note: Note
    lateinit var view: ViewGroup
    private lateinit var db: DBHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(MainActivity.listNotesLayout) setHasOptionsMenu(true)
        view = inflater.inflate(R.layout.fragment_note, container, false) as ViewGroup
        noteId = arguments!!.getInt(NOTE_ID)
        db = context!!.dbHelper
        note = db.getNote(noteId) ?: return view
        retainInstance = true

        val layoutToInflate = if (context!!.config.enableLineWrap) R.layout.note_view_static else R.layout.note_view_horiz_scrollable
        inflater.inflate(layoutToInflate, view.notes_relative_layout, true)
        if (context!!.config.clickableLinks) {
            view.notes_view.apply {
                linksClickable = true
                autoLinkMask = Linkify.WEB_URLS or Linkify.EMAIL_ADDRESSES
                movementMethod = MyMovementMethod.getInstance()
            }
        }

        view.notes_horizontal_scrollview?.onGlobalLayout {
            view.notes_view.minWidth = view.notes_horizontal_scrollview.width
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        val config = context!!.config
        view.notes_view.apply {
            typeface = if (config.monospacedFont) Typeface.MONOSPACE else Typeface.DEFAULT

            val fileContents = note.getNoteStoredValue()
            if (fileContents == null) {
                (activity as MainActivity).deleteNote(false)
                return
            }

            setColors(config.textColor, config.primaryColor, config.backgroundColor)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getTextSize())
            gravity = getTextGravity()
            if (text.toString() != fileContents) {
                setText(fileContents)
                setSelection(if (config.placeCursorToEnd) text.length else 0)
            }
        }

        if (config.showWordCount) {
            view.notes_counter.beVisible()
            view.notes_counter.setTextColor(config.textColor)
            setWordCounter(view.notes_view.text.toString())
        } else {
            view.notes_counter.beGone()
        }

        if (config.showWordCount || !config.autosaveNotes) {
            view.notes_view.addTextChangedListener(textWatcher)
        } else {
            view.notes_view.removeTextChangedListener(textWatcher)
        }
    }

    override fun onPause() {
        super.onPause()
        if (context!!.config.autosaveNotes) {
            saveText()
        }
        view.notes_view.removeTextChangedListener(textWatcher)
    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        if (!menuVisible && noteId != 0 && context?.config?.autosaveNotes == true) {
            saveText()
        }

        if (menuVisible && noteId != 0) {
            val currentText = getCurrentNoteViewText()
            if (currentText != null) {
                (activity as MainActivity).currentNoteTextChanged(currentText)
            }
        }
    }

    fun getNotesView() = view.notes_view

    fun saveText() {
        if (note.path.isNotEmpty() && !File(note.path).exists()) {
            return
        }

        if (context == null || activity == null) {
            return
        }

        val newText = getCurrentNoteViewText()
        val oldText = note.getNoteStoredValue()
        if (newText != null && newText != oldText) {
            note.value = newText
            saveNoteValue(note)
            context!!.updateWidget()
        }
    }

    fun focusEditText() {
        view.notes_view.requestFocus()
    }

    private fun saveNoteValue(note: Note) {
        if (note.path.isEmpty()) {
            db.updateNoteValue(note)
            MainActivity.mainActivityInstance.noteSavedSuccessfully(note.title)
        } else {
            val currentText = getCurrentNoteViewText()
            if (currentText != null) {
                MainActivity.mainActivityInstance.exportNoteValueToFile(note.path, currentText, true)
            }
        }
    }

    fun getCurrentNoteViewText() = view.notes_view?.text?.toString()

    private fun getTextGravity() = when (context!!.config.gravity) {
        GRAVITY_CENTER -> Gravity.CENTER_HORIZONTAL
        GRAVITY_RIGHT -> Gravity.RIGHT
        else -> Gravity.LEFT
    }

    private fun setWordCounter(text: String) {
        val words = text.replace("\n", " ").split(" ")
        notes_counter.text = words.count { it.isNotEmpty() }.toString()
    }

    private var textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(editable: Editable) {
            val text = editable.toString()
            setWordCounter(text)
            (activity as MainActivity).currentNoteTextChanged(text)
        }
    }

    override fun onPrepareOptionsMenu(menu : Menu) {
        if(MainActivity.listNotesLayout){
            (menu.findItem(R.id.rename_note) as MenuItem).setVisible(true)
            (menu.findItem(R.id.export_as_file) as MenuItem).setVisible(true)
            (menu.findItem(R.id.delete_note) as MenuItem).setVisible(true)
            (menu.findItem(R.id.open_note) as MenuItem).setVisible(false)
        }
    }

}
