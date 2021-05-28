/*
 * Copyright (c) 2013-2020 kopiLeft Services SARL, Tunis TN
 * Copyright (c) 1990-2020 kopiRight Managed Solutions GmbH, Wien AT
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.kopi.galite.ui.vaadin.field

import java.io.Serializable
import java.util.Locale

import kotlin.collections.ArrayList

import org.kopi.galite.ui.vaadin.base.FontMetrics
import org.vaadin.klaudeta.quill.QuillEditor

import com.vaadin.flow.component.AbstractField
import com.vaadin.flow.component.Focusable
import com.vaadin.flow.component.HasValue
import com.vaadin.flow.component.Unit
import com.vaadin.flow.component.customfield.CustomField
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.shared.Registration

/**
 * A rich text field implementation based on QuillEditor
 */
class RichTextField(
        var col: Int,
        var rows: Int,
        visibleRows: Int,
        var noEdit: Boolean,
        locale: Locale
) : ObjectField<Any?>() {

  //---------------------------------------------------
  // DATA MEMBERS
  //---------------------------------------------------
  /**
   * Minimal field width to see the toolbar in 56 px height (2 lines)
   */
  private val MIN_WIDTH = 540
  private val LINE_HEIGHT = 20
  private val TOOLBAR_HEIGHT = 66

  var editor = QuillEditor()
  init {
    add(editor)
    editor.setHeight((TOOLBAR_HEIGHT + LINE_HEIGHT * visibleRows).toFloat(), Unit.PIXELS)
    editor.isReadOnly = noEdit

    if (FontMetrics.LETTER.width * col < MIN_WIDTH) {
      editor.setHeight(MIN_WIDTH.toFloat(), Unit.PIXELS)
    } else {
      editor.setHeight((FontMetrics.LETTER.width * col).toFloat(), Unit.PIXELS)

    }
    //registerRpc(NavigationHandler())
  }

  private val navigationListeners = ArrayList<NavigationListener>()

  override fun getValue() = editor.value

  /**
   * Creates the configuration to be used for this rich text.
   * @return the configuration to be used for this rich text.
   */
  protected fun createConfiguration(locale: Locale, visibleRows: Int) {
    // The component CKEditorVaadin contain a Config
   // access {
   /* val configuration: Config = Config()
     // configuration.useCompactTags()
     // configuration.disableElementsPath()
    configuration.setUILanguage(Constants.Language.valueOf(locale.language))
     // configuration.disableSpellChecker()
     // configuration.setHeight(RichTextField.LINE_HEIGHT * visibleRows.toString() + "px")
     /* configuration.addExtraConfig("toolbarGroups", createEditorToolbarGroups())
      configuration.addExtraConfig("removeButtons", getRemovedToolbarButtons())
      configuration.addExtraConfig("contentsCss", "'" + Utils.getThemeResourceURL("ckeditor.css").toString() + "'")*/
      editor!!.config = configuration*/
   // }
  }

  /**
   * Creates the editor toolbar groups configurations
   * @return The editor toolbar groups configurations
   */
  protected fun createEditorToolbarGroups(): String {
    return buildString {
      append("[")
      append("{ name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ] },")
      append("{ name: 'paragraph', groups: [ 'list', 'indent', 'blocks', 'align', 'bidi', 'paragraph' ] },")
      append("{ name: 'clipboard', groups: [ 'clipboard', 'undo' ] },")
      append("'/',")
      append("{ name: 'styles', groups: [ 'styles' ] },")
      append("{ name: 'links', groups: [ 'links' ] },")
      append("{ name: 'colors', groups: [ 'colors' ] },")
      append("{ name: 'insert', groups: [ 'insert' ] },")
      append("{ name: 'document', groups: [ 'mode', 'document', 'doctools' ] },")
      append("{ name: 'editing', groups: [ 'find', 'selection', 'spellchecker', 'editing' ] },")
      append("{ name: 'forms', groups: [ 'forms' ] },")
      append("{ name: 'tools', groups: [ 'tools' ] },")
      append("'/',")
      append("{ name: 'others', groups: [ 'others' ] },")
      append("{ name: 'about', groups: [ 'about' ] }")
      append("]")
    }
  }

  protected fun getRemovedToolbarButtons(): String {
    return ("'Blockquote,"
            + "CreateDiv,"
            + "BidiLtr,"
            + "BidiRtl,"
            + "Language,"
            + "Source,"
            + "Save,"
            + "Templates,"
            + "NewPage,"
            + "Preview,"
            + "Print,"
            + "Anchor,"
            + "Flash,"
            + "Smiley,"
            + "PageBreak,"
            + "Iframe,"
            + "PasteFromWord,"
            + "PasteText,"
            + "Paste,"
            + "ImageButton,"
            + "Button,"
            + "Select,"
            + "Textarea,"
            + "TextField,"
            + "Radio,"
            + "Checkbox,"
            + "HiddenField,"
            + "Form,"
            + "Styles,"
            + "About,"
            + "Replace,"
            + "SelectAll,"
            + "RemoveFormat,"
            + "CopyFormatting,"
            + "ShowBlocks'")
  }


  override fun setReadOnly(readOnly: Boolean) {
    editor.isReadOnly = readOnly
  }

  override fun isReadOnly() = editor.isReadOnly


  override fun setRequiredIndicatorVisible(requiredIndicatorVisible: Boolean) {
    editor.isRequiredIndicatorVisible = requiredIndicatorVisible
  }

  override fun isRequiredIndicatorVisible() = editor.isRequiredIndicatorVisible

  //---------------------------------------------------
  // NAVGATION
  //---------------------------------------------------
  /**
   * Registers a navigation listener on this rich text.
   * @param l The listener to be registered.
   */
  fun addNavigationListener(l: NavigationListener) {
    navigationListeners.add(l)
  }

  /**
   * The grid editor field navigation listener
   */
  interface NavigationListener : Serializable {
    /**
     * Fired when a goto next field event is called by the user.
     */
    fun onGotoNextField()

    /**
     * Fired when a goto previous field event is called by the user.
     */
    fun onGotoPrevField()

    /**
     * Fired when a goto next block event is called by the user.
     */
    fun onGotoNextBlock()

    /**
     * Fired when a goto previous record event is called by the user.
     */
    fun onGotoPrevRecord()

    /**
     * Fired when a goto next field event is called by the user.
     */
    fun onGotoNextRecord()

    /**
     * Fired when a goto first record event is called by the user.
     */
    fun onGotoFirstRecord()

    /**
     * Fired when a goto last record event is called by the user.
     */
    fun onGotoLastRecord()

    /**
     * Fired when a goto next empty mandatory field event is called by the user.
     */
    fun onGotoNextEmptyMustfill()
  }

  override fun setPresentationValue(newPresentationValue: Any?) {
    editor.value = newPresentationValue.toString()
  }

  override fun generateModelValue() : String = editor.value
  override val isNull: Boolean
    get() = editor.value.isNullOrEmpty()

  override fun setColor(foreground: String?, background: String?) {
    TODO("Not yet implemented")
  }

  override fun checkValue(rec: Int) {
    TODO("Not yet implemented")
  }

  override fun setParentVisibility(visible: Boolean) {
    TODO("Not yet implemented")
  }

  override fun addFocusListener(function: () -> kotlin.Unit) {
    TODO("Not yet implemented")
  }
}
