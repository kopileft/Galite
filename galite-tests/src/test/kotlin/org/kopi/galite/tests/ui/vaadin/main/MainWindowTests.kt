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
package org.kopi.galite.tests.ui.vaadin.main

import kotlin.test.assertEquals

import org.junit.BeforeClass
import org.junit.Ignore
import org.junit.Test
import org.kopi.galite.tests.ui.vaadin.GaliteVUITestBase

import org.kopi.galite.ui.vaadin.menu.ModuleList

import com.github.mvysny.kaributesting.v10._expectOne
import com.github.mvysny.kaributesting.v10._find
import com.github.mvysny.kaributesting.v10._get
import com.github.mvysny.kaributesting.v10._text
import com.vaadin.flow.component.contextmenu.MenuItem
import com.vaadin.flow.component.menubar.MenuBar

class MainWindowTests: GaliteVUITestBase() {

  val modulesLayout get() = _get<ModuleList> { id = "module_list" }

  @Test
  // https://github.com/mvysny/karibu-testing/issues/76
  // FIXME: enable this test when using the version > 1.3.1 of karibu-testing
  @Ignore("Main module names are not visible, They are added into ModuleItem which is not visible")
  fun `test modules are loaded in main window after login`() {
    // Login
    login()

    // Expect there is a menu bar showing the modules and get it.
    modulesLayout._expectOne<MenuBar>()
    val modulesMenu = modulesLayout._get<MenuBar>()

    val clientsModules =
      modulesMenu
        ._find<MenuItem>()
        .map { it._text }

   assertEquals(
     listOf(
       "CLIENTS", "CLIENTS", "Test", "Test form1", "Test form2", "Form with list",
       "COMMANDS", "COMMANDS",
       "PRODUCTS", "PRODUCTS",
       "BILLS", "BILLS",
       "BILL PRODUCTS", "BILL PRODUCTS",
       "STOCKS", "STOCKS",
       "TAX RULES", "TAX RULES",
       "PROVIDERS", "PROVIDERS"
     ),
     clientsModules
   )
  }

  companion object {
    /**
     * Defined your test specific modules
     */
    @BeforeClass
    @JvmStatic
    fun initTestModules() {
      // Using modules defined in demo application
      org.kopi.galite.demo.initModules()
    }
  }
}
