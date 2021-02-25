/*
 * Copyright (c) 1990-2017 kopiRight Managed Solutions GmbH
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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * $Id: GridEditorTextAreaField.java 35180 2017-07-14 16:31:39Z hacheni $
 */
package org.kopi.galite.ui.vaadin.grid

/**
 * A text area editor for grid block
 */
class GridEditorTextAreaField(
        width: Int,
        height: Int,
        visibleHeight: Int,
        fixedNewLine: Boolean
) : GridEditorTextField(width) {
  override var value: String? = TODO()
  // TODO
}
