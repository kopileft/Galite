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
package org.kopi.galite.form

import org.kopi.galite.base.Utils
import org.kopi.galite.db.DBDeadLockException
import org.kopi.galite.db.DBInterruptionException
import org.kopi.galite.util.base.InconsistencyException
import org.kopi.galite.visual.Message
import org.kopi.galite.visual.MessageCode
import org.kopi.galite.visual.VException
import org.kopi.galite.visual.VExecFailedException
import org.kopi.galite.visual.VWindow
import org.kopi.galite.visual.VlibProperties
import java.sql.SQLException

/**
 * This class implements predefined commands
 */
object Commands : VConstants {
  /**
   * Aborts current processing, resets form.
   * @exception        VException        an exception may occur in form.reset()
   */
  fun resetForm(form: VForm) {
    if (!form.isChanged() || form.ask(Message.getMessage("confirm_break"))) {
      form.reset()
    }
  }

  /**
   * Aborts current processing, closes form.
   * @exception        VException        an exception may occur in form.close()
   */
  fun quitForm(form: VForm, code: Int = VWindow.CDE_QUIT) {
    if (!form.isChanged() || form.ask(Message.getMessage("confirm_quit"))) {
      form.close(code)
    }
  }

  /*
   * ----------------------------------------------------------------------
   * BLOCK-LEVEL COMMANDS
   * ----------------------------------------------------------------------
   */
  fun showHideFilter(b: VBlock) {
    assert(!b.noChart()) { "The command showHideFilter cannot be used for no chart blocks" }
    b.showHideFilter()
  }

  /**
   * Switches view between list and detail mode.
   */
  fun switchBlockView(b: VBlock) {
    assert(b.isMulti()) { "The command switchBlockView can be used only with a multi block." }
    (b.getDisplay() as UMultiBlock).switchView(-1)
  }

  /**
   * Aborts current processing (old)
   * @exception        VException        an exception may occur in b.clear()
   */
  fun resetBlock(b: VBlock) {
    if (!b.isChanged() || b.getForm().ask(Message.getMessage("confirm_break"))) {
      b.clear()
      if (b.getForm() is VDictionaryForm) {
        if ((b.getForm() as VDictionaryForm).isRecursiveQuery()) {
          b.getForm().reset()
        } else if (!(b.getForm() as VDictionaryForm).isNewRecord()) {
          b.setMode(VConstants.MOD_QUERY)
        }
      } else {
        b.setMode(VConstants.MOD_QUERY)
      }
    }
  }

  private fun gotoFieldIfNoActive(lastBlock: VBlock) {
    // it is possible that (for example) the load method is
    // overridden and it include now a gotoBlock(..)
    val form: VForm = lastBlock.getForm()
    val activeBlock = form.getActiveBlock()

    if (activeBlock == null) {
      form.gotoBlock(lastBlock)
    } else {
      if (activeBlock.getActiveField() == null) {
        activeBlock.gotoFirstField()
      }
    }
  }

  /**
   * Menu query block, fetches selected record.
   * @exception        VException        an exception may occur during DB access
   */
  fun menuQuery(b: VBlock) {
    val form: VForm = b.getForm()


    Utils.freeMemory()
    b.validate()
    if (form is VDictionaryForm) {
      form.setMenuQuery(true)
    }

    val id: Int = b.singleMenuQuery(false) as Int

    if (id != -1) {
      while (true) {
        try {
          form.startProtected(Message.getMessage("loading_record"))
          b.fetchRecord(id)
          form.commitProtected()
          gotoFieldIfNoActive(b)
          break
        } catch (e: VException) {
          try {
            form.abortProtected(e)
          } catch (abortEx: VException) {
            throw abortEx
          }
        } catch (e: SQLException) {
          try {
            form.abortProtected(e)
          } catch (abortEx: DBDeadLockException) {
            throw VExecFailedException(MessageCode.getMessage(VIS))
          } catch (abortEx: DBInterruptionException) {
            throw VExecFailedException(MessageCode.getMessage(VIS))
          } catch (abortEx: SQLException) {
            throw VExecFailedException(abortEx)
          }
        } catch (e: Error) {
          try {
            form.abortProtected(e)
          } catch (abortEx: Error) {
            throw InconsistencyException(abortEx)
          }
        } catch (e: RuntimeException) {
          try {
            form.abortProtected(e)
          } catch (abortEx: RuntimeException) {
            throw InconsistencyException(abortEx)
          }
        }
      }
    }
    Utils.freeMemory()
  }

  /**
   * Menu query block, fetches selected record.
   * @exception        VException        an exception may occur during DB access
   */
  fun recursiveQuery(b: VBlock) {
    val form = b.getForm() as VDictionaryForm

    Utils.freeMemory()
    b.validate()
    form.saveFilledField()

    val id : Int = b.singleMenuQuery(false) as Int

    if (id != -1) {
      while (true) {
        try {
          form.startProtected(Message.getMessage("loading_record"))

          // fetches data to active record
          b.fetchRecord(id)
          form.commitProtected()
          gotoFieldIfNoActive(b)
          break
        } catch (e: VException) {
          try {
            form.abortProtected(e)
          } catch (abortEx: VException) {
            throw abortEx
          }
        } catch (e: SQLException) {
          try {
            form.abortProtected(e)
          } catch (abortEx: DBDeadLockException) {
            throw VExecFailedException(MessageCode.getMessage(VIS))
          } catch (abortEx: DBInterruptionException) {
            throw VExecFailedException(MessageCode.getMessage(VIS))
          } catch (abortEx: SQLException) {
            throw VExecFailedException(abortEx)
          }
        } catch (e: Error) {
          try {
            form.abortProtected(e)
          } catch (abortEx: Error) {
            throw InconsistencyException(abortEx)
          }
        } catch (e: RuntimeException) {
          try {
            form.abortProtected(e)
          } catch (abortEx: RuntimeException) {
            throw InconsistencyException(abortEx)
          }
        }
      }
    }
    if (id == -1) {
      form.interruptRecursiveQuery()
    }
  }

  /**
   * Menu query block, fetches selected record, then moves to next block
   * @exception        VException        an exception may occur during DB access
   */
  fun queryMove(b: VBlock) {
    val form: VForm = b.getForm()

    b.validate()

    val id: Int = b.singleMenuQuery(false) as Int

    if (id != -1) {
      try {
        while (true) {
          try {
            form.startProtected(Message.getMessage("loading_record"))
            b.fetchRecord(id)
            form.commitProtected()
            break
          } catch (e: VException) {
            try {
              form.abortProtected(e)
            } catch (abortEx: VException) {
              throw VExecFailedException(abortEx.message!!, abortEx)
            }
          } catch (e: SQLException) {
            try {
              form.abortProtected(e)
            } catch (abortEx: SQLException) {
              throw VExecFailedException(abortEx)
            }
          } catch (e: Error) {
            try {
              form.abortProtected(e)
            } catch (abortEx: Error) {
              throw InconsistencyException(abortEx)
            }
          } catch (e: RuntimeException) {
            try {
              form.abortProtected(e)
            } catch (abortEx: RuntimeException) {
              throw InconsistencyException(abortEx)
            }
          }
        }
      } catch (e: VException) {
        throw e
      }

      // goto next block
      var i = 0

      while (i < form.getBlockCount() - 1) {
        if (b === form.getBlock(i)) {
          form.gotoBlock(form.getBlock(i + 1))
          return
        }
        i += 1
      }
      throw InconsistencyException("FATAL ERROR: NO BLOCK ACCESSIBLE")
    }
  }

  /**
   * Queries block, fetches first record.
   * @exception        VException        an exception may occur during DB access
   */
  fun serialQuery(b: VBlock) {
    val form: VForm = b.getForm()

    b.validate()
    Utils.freeMemory()
    try {
      while (true) {
        try {
          form.startProtected(Message.getMessage("searching_database"))
          try {
            b.load()
            gotoFieldIfNoActive(b)
          } catch (e: VQueryOverflowException) {
            // !!! HANDLE OVERFLOW WARNING
          }
          form.commitProtected()
          break
        } catch (e: VException) {
          try {
            form.abortProtected(e)
          } catch (abortEx: VException) {
            throw abortEx
          }
        } catch (e: SQLException) {
          try {
            form.abortProtected(e)
          } catch (abortEx: DBDeadLockException) {
            throw VExecFailedException(MessageCode.getMessage(VIS))
          } catch (abortEx: DBInterruptionException) {
            throw VExecFailedException(MessageCode.getMessage(VIS))
          } catch (abortEx: SQLException) {
            throw VExecFailedException(abortEx)
          }
        } catch (e: Error) {
          try {
            form.abortProtected(e)
          } catch (abortEx: Error) {
            throw InconsistencyException(abortEx)
          }
        } catch (e: RuntimeException) {
          try {
            form.abortProtected(e)
          } catch (abortEx: RuntimeException) {
            throw InconsistencyException(abortEx)
          }
        }
      }
    } catch (e: VException) {
      gotoFieldIfNoActive(b)
      throw e
    }
  }

  /**
   * Sets the block into insert mode.
   * @exception        VException        an exception may occur during DB access
   */
  fun insertMode(b: VBlock) {
    assert(!b.isMulti()) { "The command InsertMode can be used only with a single block." }
    assert(b.getMode() != VConstants.MOD_INSERT) {
      "The block " + b.getName() + " is already in INSERT mode."
    }

    if (b.getMode() == VConstants.MOD_UPDATE
            && b.isChanged()
            && !b.getForm().ask(Message.getMessage("confirm_insert_mode"))) {
      return
    }

    b.apply {
      val changed: Boolean = isRecordChanged(0)

      setMode(VConstants.MOD_INSERT)
      setDefault()
      setRecordFetched(0, false)
      setRecordChanged(0, changed)
      if (!isMulti() && getForm().getActiveBlock() === this) {
        gotoFirstUnfilledField()
      }
    }
  }

  /**
   * Saves current block (insert or update)
   * @exception        VException        an exception may occur during DB access
   */
  fun saveBlock(b: VBlock) {
    val form: VForm = b.getForm()

    Utils.freeMemory()
    assert(!b.isMulti()) { "saveBlock can be used only with a single block." }
    b.validate()
    if (!b.isChanged() && !form.ask(Message.getMessage("confirm_save_unchanged"))) {
      return
    }
    try {
      while (true) {
        try {
          form.startProtected(Message.getMessage("saving_record"))
          b.save()
          form.commitProtected()
          break
        } catch (e: VException) {
          try {
            form.abortProtected(e)
          } catch (abortEx: VException) {
            throw VExecFailedException(abortEx)
          }
        } catch (e: SQLException) {
          try {
            form.abortProtected(e)
          } catch (abortEx: DBDeadLockException) {
            throw VExecFailedException(MessageCode.getMessage(VIS))
          } catch (abortEx: DBInterruptionException) {
            throw VExecFailedException(MessageCode.getMessage(VIS))
          } catch (abortEx: SQLException) {
            throw VExecFailedException(abortEx)
          }
        } catch (e: Error) {
          try {
            form.abortProtected(e)
          } catch (abortEx: Error) {
            throw VExecFailedException(abortEx)
          }
        } catch (e: RuntimeException) {
          try {
            form.abortProtected(e)
          } catch (abortEx: RuntimeException) {
            throw VExecFailedException(abortEx)
          }
        }
      }
    } catch (e: VException) {
      throw e
    }
    saveDone(b, true)
  }

  /**
   * saveDone
   * This method should be called after a self made save trigger
   * @exception        VException        an exception may occur during DB access
   */
  fun saveDone(b: VBlock) {
    saveDone(b, false)
  }

  /**
   * saveDone
   * This method should be called after a self made save trigger
   * @exception        VException        an exception may occur during DB access
   */
  private fun saveDone(b: VBlock, single: Boolean) {
    val form: VForm = b.getForm()
    val mode: Int = b.getMode()

    if (form is VDictionaryForm) {
      if ((form).isNewRecord()) {
        form.close(VWindow.CDE_VALIDATE)
        return
      } else if ((form).isRecursiveQuery() ||
              (form).isMenuQuery()) {
        form.reset()
        return
      }
    }
    when (mode) {
      VConstants.MOD_INSERT -> {
        if (single) {
          b.clear()
          b.setDefault()
          b.gotoFirstUnfilledField()
        } else {
          form.reset()
        }
        return
      }
      VConstants.MOD_UPDATE -> {
        try {
          b.fetchNextRecord(1)
          return
        } catch (e: VException) {}
        b.clear()
        b.setMode(VConstants.MOD_QUERY)
        return
      }
      else -> throw InconsistencyException()
    }
  }

  /**
   * Deletes current block
   * @exception        VException        an exception may occur during DB access
   */
  fun deleteBlock(b: VBlock) {
    val form: VForm = b.getForm()

    if (!form.ask(Message.getMessage("confirm_delete"))) {
      return
    }
    try {
      while (true) {
        try {
          form.startProtected(Message.getMessage("deleting_record"))
          b.delete()
          form.commitProtected()
          break
        } catch (e: VException) {
          try {
            form.abortProtected(e)
          } catch (abortEx: VException) {
            throw VExecFailedException(abortEx)
          }
        } catch (e: SQLException) {
          try {
            form.abortProtected(e)
          } catch (abortEx: DBDeadLockException) {
            throw VExecFailedException(MessageCode.getMessage(VIS))
          } catch (abortEx: DBInterruptionException) {
            throw VExecFailedException(MessageCode.getMessage(VIS))
          } catch (abortEx: SQLException) {
            throw VExecFailedException(abortEx)
          }
        } catch (e: Error) {
          try {
            form.abortProtected(e)
          } catch (abortEx: Error) {
            throw VExecFailedException(abortEx)
          }
        } catch (e: RuntimeException) {
          try {
            form.abortProtected(e)
          } catch (abortEx: RuntimeException) {
            throw VExecFailedException(abortEx)
          }
        }
      }
    } catch (e: VException) {
      throw e
    }
    if (b.getForm() is VDictionaryForm && (b.getForm() as VDictionaryForm).isRecursiveQuery()) {
      b.getForm().reset()
      return
    }
    b.setMode(VConstants.MOD_QUERY)

    // Fetch record (forward)
    try {
      b.fetchNextRecord(1)
      return
    } catch (e: VException) {
      // ignore it
    }

    // Fetch record (backward)
    try {
      b.fetchNextRecord(-1)
      return
    } catch (e: VException) {
      // ignore it
    }

    // No more records
    b.setMode(VConstants.MOD_QUERY)
    b.clear()
  }

  /**
   * Inserts an empty line in multi-block.
   * @exception        VException        an exception may occur during DB access
   */
  fun insertLine(b: VBlock) {
    assert(b.isMulti()) { "The command InsertLine can be used only with a multi block." }
    assert(b === b.getForm().getActiveBlock()) { b.getName().toString() + " is not the active block. (" + b.getForm().getActiveBlock()?.getName() + ")" }
    val recno: Int = b.activeRecord

    b.leaveRecord(true)
    try {
      b.insertEmptyRecord(recno)
    } catch (e: VException) {
      throw VExecFailedException(MessageCode.getMessage("VIS-00028"))
    } finally {
      b.gotoRecord(recno)
    }
  }

  /**
   * Sets the search operator for the current field
   */
  fun setSearchOperator(b: VBlock) {
    val f: VField? = b.getActiveField()

    if (f != null) {
      val v: Int = VListDialog(VlibProperties.getString("search_operator"), arrayOf(
              VlibProperties.getString("operator_eq"),
              VlibProperties.getString("operator_lt"),
              VlibProperties.getString("operator_gt"),
              VlibProperties.getString("operator_le"),
              VlibProperties.getString("operator_ge"),
              VlibProperties.getString("operator_ne")
      )).selectFromDialog(b.getForm(), f)
      if (v != -1) {
        f.setSearchOperator(v)
        f.getForm().setFieldSearchOperator(f.getSearchOperator())
      }
    }
  }

  /**
   * Navigate between accessible blocks
   * @exception        VException        an exception may occur during block focus transfer
   */
  fun changeBlock(b: VBlock) {
    b.validate()
    Utils.freeMemory()
    val blockCount: Int = b.getForm().getBlockCount()
    val blockTable = arrayOfNulls<VBlock>(blockCount - 1)
    val titleTable = arrayOfNulls<String>(blockCount - 1)
    var otherBlocks = 0

    for (i in 0 until blockCount) {
      if (b === b.getForm().getBlock(i)) {
        continue
      }
      if (!b.getForm().getBlock(i).isAccessible()) {
        continue
      }
      blockTable[otherBlocks] = b.getForm().getBlock(i)
      titleTable[otherBlocks] = blockTable[otherBlocks]!!.getTitle()
      otherBlocks += 1
    }
    val sel: Int

    sel = when (otherBlocks) {
      0 -> -1
      1 -> 0
      else -> VListDialog(VlibProperties.getString("pick_in_list"),
                          titleTable,
                          otherBlocks).selectFromDialog(b.getForm(), null, null)
    }
    if (sel < 0) {
      b.getForm().gotoBlock(b)
    } else {
      b.getForm().gotoBlock(blockTable[sel])
    }
  }

  /*
   * ----------------------------------------------------------------------
   * FIELD-LEVEL COMMANDS
   * ----------------------------------------------------------------------
   */
  /**
   * Increment the value of the field
   */
  fun increment(field: VField) {
    if (field is VIntegerField) {
      val r: Int = field.block.activeRecord

      field.requestFocus()
      field.validate()
      if (field.isNull(r)) {
        field.setInt(r, 1)
      } else {
        field.setInt(r, field.getInt(r) + 1)
      }
    }
  }

  /**
   * Decrement the value of the field
   */
  fun decrement(field: VField?) {
    if (field is VIntegerField) {
      val r: Int = field.block.activeRecord

      field.requestFocus()
      field.validate()
      if (field.isNull(r)) {
        field.setInt(r, 1)
      } else {
        field.setInt(r, field.getInt(r) - 1)
      }
    }
  }

  private const val VIS = "VIS-00058"
}
