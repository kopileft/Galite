/*
 * Copyright (c) 1990-2016 kopiRight Managed Solutions GmbH
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
 * $Id: ResolutionValue.java 34961 2016-11-04 17:20:49Z hacheni $
 */
package org.kopi.galite.util.ipp

import java.io.IOException

class ResolutionValue : IPPValue {
  // --------------------------------------------------------------------
  // CONSTRUCTOR
  // --------------------------------------------------------------------
  constructor(units: Byte, xres: Int, yres: Int) {
    this.units = units
    this.xres = xres
    this.yres = yres
  }

  constructor(`is`: IPPInputStream) {
    `is`.readShort() //value-length
    xres = `is`.readInteger()
    yres = `is`.readInteger()
    units = `is`.readByte()
  }// value-length + value

  // --------------------------------------------------------------------
  // ACCESSORS
  // --------------------------------------------------------------------
  override val size: Int
    get() = 2 + 9 // value-length + value

  override fun write(os: IPPOutputStream) {
    os.writeShort(9)
    os.writeInteger(xres)
    os.writeInteger(yres)
    os.writeByte(units.toInt())
  }

  override fun dump() {
    println("\tunits : " + units +
            "\txres : " + xres +
            "\tyres : " + yres)
  }

  // --------------------------------------------------------------------
  // DATA MEMBERS
  // --------------------------------------------------------------------
  private var units: Byte
  private var xres: Int
  private var yres: Int
}