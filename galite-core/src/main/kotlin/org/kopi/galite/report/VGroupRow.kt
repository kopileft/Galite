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

package org.kopi.galite.report

/**
 * Represents a description for a grouping row
 *
 * @param data The grouping row data
 * @param level the grouping row level
 */
class VGroupRow(data: Array<Any?>, private val level: Int) : VReportRow(data) {
  // TODO()

  /**
   * Sets data row
   *
   * @param        column                the index of the column
   * @param        value                the value for the column
   */
  override fun setValueAt(column: Int, value: Any) {
    data[column] = value
  }

  /**
   * Return the level of the node in the grouping tree
   */
  override fun getLevel(): Int {
    return level
  }

  /**
   * Returns true iff all the child nodes of the level generation are visible.
   *
   * @param level level to test
   */
  fun isUnfolded(level: Int): Boolean {
    TODO()
  }

  /**
   * Sets child node of the level generation to visible
   *
   * @param level level to be set visible
   */
  fun setChildNodesVisible(level: Int) {
    TODO()
  }

  /**
   * Sets child node of the level generation to invisible
   *
   * @param level level to be set visible
   */
  fun setChildNodesInvisible(level: Int) {
   TODO()
  }

  private fun setChildNodesInvisible() {
    TODO()
  }
}
