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

package org.kopi.galite.visual

import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.TreeNode

/**
 * A root item must provide its ID and name. The root tree node
 * of this item should be provided for further uses.
 */
class RootItem(id: Int, name: String) {
  // ---------------------------------------------------------------------
  // IMPLEMENTATION
  // ---------------------------------------------------------------------
  /**
   * Creates the item tree nodes for this root item.
   * @param items The accessible items for the connected user.
   * @param isSuperUser Is the connected user is a super user ?
   */
  fun createTree(items: Array<Item>) {
    this.root = createTree(items, rootItem)
  }

  /**
   * Creates the item tree for the given root item.
   * @param items The accessible items.
   * @param root The root item.
   * @param force Should we force item accessibility ?
   * @param isSuperUser Is the connected user is a super user ?
   * @return The local root tree node.
   */
  protected fun createTree(items: Array<Item>, root: Item): DefaultMutableTreeNode? {
    var self: DefaultMutableTreeNode? = null
    var childsCount = 0
    for (i in items.indices) {
      if (items[i].parent === root.id) {
        var node: DefaultMutableTreeNode?
        childsCount++
        items[i].level = root.level + 1
        node = createTree(items, items[i])
        if (node != null) {
          if (self == null) {
            self = DefaultMutableTreeNode(root)
          }
          self.add(node)
        }
      }
    }
    return if (childsCount == 0) {
      DefaultMutableTreeNode(root)
    } else {
      self
    }
  }
  // ---------------------------------------------------------------------
  // ACCESSORS
  // ---------------------------------------------------------------------
  /**
   * Returns true if this root item does not contain any item.
   * @return True if this root item does not contain any item.
   */
  val isEmpty: Boolean
    get() = this.root == null

  // ----------------------------------------------------------------------
  // DATA MEMBERS
  // ----------------------------------------------------------------------
  /**
   * Creates a root item from its ID and name.
   * @param id The root item ID.
   * @param name The root item name.
   */
  private val rootItem: Item = Item(id,
          0,
          name,
          null,
          null,
          false,
          false,
          null,
          name)

  init {
    rootItem.level = 0
  }

  /**
   * Return the root node
   */
  var root: TreeNode? = null
    private set
}
