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

package org.kopi.galite.visual.util.lpr

import gnu.getopt.Getopt
import gnu.getopt.LongOpt

class LpROptions @JvmOverloads constructor(name: String? = "LpR") : LpdOptions(name) {

  var copies = 1
  var filetype = "l"
  var indent = -1
  var job: String? = null
  var mail = false
  var burst = false
  var remove = false
  var dataFirst = false
  var title: String? = null
  var tmpdir = "/tmp/"
  var width = -1
  var windows = false
  var printClass: String? = null

  override fun processOption(code: Int, g: Getopt): Boolean {
    return when (code) {
      '#'.toInt() -> {
        copies = getInt(g, 0)
        true
      }
      'f'.toInt() -> {
        filetype = getString(g, "")
        true
      }
      'i'.toInt() -> {
        indent = getInt(g, 8)
        true
      }
      'J'.toInt() -> {
        job = getString(g, "")
        true
      }
      'm'.toInt() -> {
        mail = !false
        true
      }
      'b'.toInt() -> {
        burst = !false
        true
      }
      'r'.toInt() -> {
        remove = !false
        true
      }
      'D'.toInt() -> {
        dataFirst = !false
        true
      }
      'T'.toInt() -> {
        title = getString(g, "")
        true
      }
      't'.toInt() -> {
        tmpdir = getString(g, "")
        true
      }
      'w'.toInt() -> {
        width = getInt(g, 80)
        true
      }
      'W'.toInt() -> {
        windows = !false
        true
      }
      'C'.toInt() -> {
        printClass = getString(g, "")
        true
      }
      else -> super.processOption(code, g)
    }
  }

  override val options: Array<String?>
    get() {
      val parent: Array<String?> = super.options
      val total = arrayOfNulls<String>(parent.size + 13)

      System.arraycopy(parent, 0, total, 0, parent.size)
      total[parent.size + 0] = "  --copies, -#<int>:    Sets the number of copies to print [1]"
      total[parent.size + 1] = "  --filetype, -f<String>: Sets the file type [l]"
      total[parent.size + 2] = "  --indent, -i<int>:    Sets the indent size [-1]"
      total[parent.size + 3] = "  --job, -J<String>:    Sets the job identifier"
      total[parent.size + 4] = "  --mail, -m:           Mails when job is done [false]"
      total[parent.size + 5] = "  --burst, -b:          Prints a burst page [false]"
      total[parent.size + 6] = "  --remove, -r:         Removes the file when printing is done [false]"
      total[parent.size + 7] = "  --dataFirst, -D:      Sends data first (for old NT lpd servers) [false]"
      total[parent.size + 8] = "  --title, -T<String>:  Sets the title for the job"
      total[parent.size + 9] = "  --tmpdir, -t<String>: Sets the tmp dir to use [/tmp/]"
      total[parent.size + 10] = "  --width, -w<int>:     Sets the page width [-1]"
      total[parent.size + 11] = "  --windows, -W:        Uses a windows style protocol to communicate with the server [false]"
      total[parent.size + 12] = "  --printClass, -C<String>: Sets the print class"
      return total
    }
  override val shortOptions: String
    get() = "#:f:i::J:mbrDT:t:w::WC:" + super.shortOptions

  override fun version() {
    println("Version 2.3B released 17 September 2007")
  }

  override fun usage() {
    System.err.println("usage: org.kopi.galite.visual.util.lpr.LpR.Main [options] <files>")
  }

  override val longOptions: Array<LongOpt?>
    get() {
      val parent: Array<LongOpt?> = super.longOptions

      val total = arrayOfNulls<LongOpt>(parent.size + LONGOPTS.size)
      System.arraycopy(parent, 0, total, 0, parent.size)
      System.arraycopy(LONGOPTS, 0, total, parent.size, LONGOPTS.size)
      return total
    }

  companion object {
    private val LONGOPTS = arrayOf(
            LongOpt("copies", LongOpt.REQUIRED_ARGUMENT, null, '#'.toInt()),
            LongOpt("filetype", LongOpt.REQUIRED_ARGUMENT, null, 'f'.toInt()),
            LongOpt("indent", LongOpt.OPTIONAL_ARGUMENT, null, 'i'.toInt()),
            LongOpt("job", LongOpt.REQUIRED_ARGUMENT, null, 'J'.toInt()),
            LongOpt("mail", LongOpt.NO_ARGUMENT, null, 'm'.toInt()),
            LongOpt("burst", LongOpt.NO_ARGUMENT, null, 'b'.toInt()),
            LongOpt("remove", LongOpt.NO_ARGUMENT, null, 'r'.toInt()),
            LongOpt("dataFirst", LongOpt.NO_ARGUMENT, null, 'D'.toInt()),
            LongOpt("title", LongOpt.REQUIRED_ARGUMENT, null, 'T'.toInt()),
            LongOpt("tmpdir", LongOpt.REQUIRED_ARGUMENT, null, 't'.toInt()),
            LongOpt("width", LongOpt.OPTIONAL_ARGUMENT, null, 'w'.toInt()),
            LongOpt("windows", LongOpt.NO_ARGUMENT, null, 'W'.toInt()),
            LongOpt("printClass", LongOpt.REQUIRED_ARGUMENT, null, 'C'.toInt())
    )
  }
}
