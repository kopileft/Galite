/*
 * Copyright (c) 2013-2020 kopiLeft Services SARL, Tunis TN
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

package org.kopi.galite.l10n

import org.jdom2.Document
import org.jdom2.Element
import org.kopi.galite.util.base.InconsistencyException

/**
 * Implements an actor localizer.
 */
class ActorLocalizer(document: Document, ident: String) {
    // ----------------------------------------------------------------------
    // ACCESSORS
    // ----------------------------------------------------------------------
    /**
     * Returns the value of the label attribute.
     */
    val label: String
        get() = self.getAttributeValue("label")

    /**
     * Returns the value of the help attribute.
     */
    val help: String
        get() = self.getAttributeValue("help")

    // ----------------------------------------------------------------------
    // DATA MEMBERS
    // ----------------------------------------------------------------------
    private val self: Element

    // ----------------------------------------------------------------------
    // CONSTRUCTOR
    // ----------------------------------------------------------------------
    /**
     * Constructor
     *
     * @param             document        the document containing the actor localization
     * @param             ident           the identifier of the actor localization
     */
    init {
        val root: Element = document.rootElement
        val names = listOf ("form", "insert")

        if (root.name !in names) {
            throw InconsistencyException("bad root element $root")
        }
        self = Utils.lookupChild(root, "actor", "ident", ident)
    }
}
