/*
 * Copyright (c) 2013-2022 kopiLeft Services SARL, Tunis TN
 * Copyright (c) 1990-2022 kopiRight Managed Solutions GmbH, Wien AT
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

package org.kopi.galite.visual.report

import org.kopi.galite.visual.visual.VActor
import org.kopi.galite.visual.visual.VlibProperties

class VDefaultReportActor(menuIdent: String,
                          actorIdent: String,
                          iconName: String,
                          acceleratorKey: Int,
                          acceleratorModifier: Int)
                 : VActor(menuIdent,
                          null,
                          actorIdent,
                          null,
                          null,
                          acceleratorKey,
                          acceleratorModifier) {

  init {
    this.iconName = iconName
    localize()
  }

  // ----------------------------------------------------------------------
  // LOCALIZATION
  // ----------------------------------------------------------------------

  private fun localize() {
    menuName = VlibProperties.getString(menuIdent)
    menuItem = VlibProperties.getString(actorIdent)
    help = VlibProperties.getString("$actorIdent-help")
  }
}