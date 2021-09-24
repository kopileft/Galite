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

package org.kopi.galite.list

import kotlin.reflect.KClass

import org.jetbrains.exposed.sql.Column
import org.kopi.galite.type.Decimal
import org.kopi.galite.util.base.InconsistencyException

/**
 * Represents a list column.
 */
class VFixnumCodeColumn(title: String,
                        column: Column<*>?,
                        names: Array<String>,
                        private val codes: Array<Decimal?>,
                        sortAscending: Boolean)
          : VCodeColumn(title,
                        column,
                        names,
                        sortAscending) {
  // --------------------------------------------------------------------
  // IMPLEMENTATION
  // --------------------------------------------------------------------
  /**
   * Returns the index.of given object
   */
  override fun getObjectIndex(value: Any): Int {
    codes.forEachIndexed { index, code ->
      if (value == code) {
        return index
      }
    }
    throw InconsistencyException("bad code value " + value as Decimal)
  }

  override fun getDataType(): KClass<*> = Decimal::class
}
