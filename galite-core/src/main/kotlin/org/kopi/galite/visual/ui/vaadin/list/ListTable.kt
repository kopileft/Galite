/*
 * Copyright (c) 2013-2021 kopiLeft Services SARL, Tunis TN
 * Copyright (c) 1990-2021 kopiRight Managed Solutions GmbH, Wien AT
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
package org.kopi.galite.visual.ui.vaadin.list

import org.kopi.galite.visual.form.VListDialog

import com.vaadin.flow.component.Unit
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.data.value.ValueChangeMode

@CssImport("./styles/galite/list.css")
class ListTable(val model: VListDialog) : Grid<List<Any?>>() {

  init {
    isColumnReorderingAllowed = true
    buildRows()
    buildColumns()
    setTableWidth(model)
    installFilters(model)
  }

  private fun buildRows() {
    val items: Array<List<Any?>?> = arrayOfNulls(model.count)

    for (row in 0 until model.count) {
      items[row] = model.data.map { it[model.translatedIdents[row]] }
    }
    setItems(*items)
  }

  private fun buildColumns() {
    for(col in 0 until model.getColumnCount()) {
      addColumn {
        formatObject(it[col], col)
      }.setHeader(Span(model.getColumnName(col)))
              .setKey(col.toString())
    }
  }

  /**
   * Install filters on all properties.
   */
  fun installFilters(model: VListDialog?) {
    val filterRow = appendHeaderRow()

    filterRow.also { element.classList.add("list-filter") }
    this.columns.forEachIndexed { index, column ->
      val cell = filterRow.getCell(column)
      val filter = TextField()
      filter.setWidthFull()
      val search = Icon(VaadinIcon.SEARCH)

      filter.suffixComponent = search
      filter.className = "filter-text"
      filter.addValueChangeListener {
        (dataProvider as ListDataProvider).filter = ListFilter(index, filter.value, true, false)
      }

      filter.valueChangeMode = ValueChangeMode.EAGER
      cell.setComponent(filter)
    }
    element.classList.add("filtered")
  }

  /**
   * Formats an object according to the property nature.
   * @param o The object to be formatted.
   * @return The formatted property object.
   */
  protected fun formatObject(o: Any?, col: Int): String {
    return model.columns[col]!!.formatObject(o).toString()
  }

  /**
   * Calculates the table width based on its content.
   * @param model The data model.
   */
  internal fun setTableWidth(model: VListDialog) {
    var width = 0
    for (col in 0 until model.getColumnCount()) {
      val columnWidth = getColumnWidth(model, col) + 36
      getColumnByKey(col.toString()).width = columnWidth.toString()+ "px"
      width += columnWidth
    }
    setWidth(width + 20f, Unit.PIXELS)
  }

  /**
   * Calculates the column width based on the column rows content.
   * @param model The list data model.
   * @param col The column index.
   * @return The estimated column width.
   */
  private fun getColumnWidth(model: VListDialog, col: Int): Int {
    var width: Int
    width = 0
    for (row in 0 until model.count) {
      val value = model.columns[col]!!.formatObject(model.getValueAt(row, col)).toString()
      width = width.coerceAtLeast(value.length.coerceAtLeast(model.titles[col]!!.length))
    }
    return 8 * width
  }

  val selectedItem: List<Any?> get() = asSingleSelect().value
}
