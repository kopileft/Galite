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
package org.kopi.galite.db

import java.sql.Connection
import java.sql.SQLException
import java.util.Timer
import java.util.TimerTask

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.kopi.galite.common.Window
import org.kopi.galite.visual.VWindow

/**
 * Starts a protected transaction.
 *
 * @param	message		the message to be displayed.
 * @param       db              the database to execute the statement.
 * @param       statement       the transaction statement.
 */
fun <T> Window.transaction(message: String,
                           db: Database? = null,
                           statement: Transaction.() -> T): T =
        model.transaction(message, db, statement)

/**
 * Starts a protected transaction.
 *
 * @param	message		        the message to be displayed.
 * @param       transactionIsolation    the transaction isolation level (Connection.TRANSACTION_SERIALIZABLE,
 * TRANSACTION_READ_UNCOMMITTED, ...). See [Connection].
 * @param       repetitionAttempts      the number of retries when [SQLException] occurs.
 * @param       db                      the database to execute the statement.
 * @param       statement               the transaction statement.
 */
fun <T> Window.transaction(message: String,
                           transactionIsolation: Int,
                           repetitionAttempts: Int,
                           db: Database? = null,
                           statement: Transaction.() -> T): T =
        model.transaction(message, transactionIsolation, repetitionAttempts, db, statement)

/**
 * Starts a protected transaction.
 *
 * @param	message		the message to be displayed.
 * @param       db              the database to execute the statement.
 * @param       statement       the transaction statement.
 */
internal fun <T> VWindow.transaction(message: String,
                                     db: Database? = null,
                                     statement: Transaction.() -> T): T =
        doAndWait(message) {
          org.jetbrains.exposed.sql.transactions.transaction(db, statement)
        }

/**
 * Starts a protected transaction.
 *
 * @param	message		        the message to be displayed.
 * @param       transactionIsolation    the transaction isolation level (Connection.TRANSACTION_SERIALIZABLE,
 * TRANSACTION_READ_UNCOMMITTED, ...). See [Connection].
 * @param       repetitionAttempts      the number of retries when [SQLException] occurs.
 * @param       db                      the database to execute the statement.
 * @param       statement               the transaction statement.
 */
internal fun <T> VWindow.transaction(message: String,
                                     transactionIsolation: Int,
                                     repetitionAttempts: Int,
                                     db: Database? = null,
                                     statement: Transaction.() -> T): T =
        doAndWait(message) {
          org.jetbrains.exposed.sql.transactions.transaction(
            transactionIsolation,
            repetitionAttempts,
            db,
            statement
          )
        }

/**
 * Display waiting message while executing the task.
 *
 * @param message the waiting message.
 * @param task    the task to execute.
 */
fun <T> VWindow.doAndWait(message: String, task: () -> T): T {
  var finished = false

  doAfter(10) {
    if (!finished) {
      setWaitInfo(message)
    }
  }

  val returnValue = task()

  synchronized(finished) {
    finished = true
    unsetWaitInfo()
  }
  return returnValue
}

/**
 * Executes a task after some delay.
 *
 * @param delay   the delay.
 * @param task    the task to execute.
 */
fun doAfter(delay: Long, task: () -> Unit) {
  Timer().schedule(
    object : TimerTask() {
      override fun run() {
        task()
      }
    },
    delay
  )
}
