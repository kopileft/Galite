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
package org.kopi.galite.ui.vaadin.main

import com.vaadin.flow.component.contextmenu.ContextMenu
import com.vaadin.flow.component.contextmenu.MenuItem
import com.vaadin.flow.function.SerializableRunnable

/**
 * Class for menu items in the already opened windows menu.
 */
class VWindowsMenuItem(contextMenu : ContextMenu,
                       contentReset : SerializableRunnable? = null) : MenuItem(contextMenu, contentReset) {

  companion object {
    //---------------------------------------------------
    // DATA MEMBERS
    //---------------------------------------------------
    private const val STYLENAME_DEFAULT = "item"
  }

  //---------------------------------------------------
  // CONSTRUCTOR
  //---------------------------------------------------
  /**
   * Creates a new already opened window item.
   */
  init {
    element.setAttribute("name",STYLENAME_DEFAULT)
    element.setAttribute("whiteSpace","nowrap")
  }
}
