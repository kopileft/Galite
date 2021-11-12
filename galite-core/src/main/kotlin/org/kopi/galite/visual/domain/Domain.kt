/*
 * Copyright (c) 2013-2021 kopiLeft Services SARL, Tunis TN
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

package org.kopi.galite.visual.domain

import kotlin.reflect.KClass

import org.joda.time.DateTime
import org.kopi.galite.visual.chart.VBooleanDimension
import org.kopi.galite.visual.chart.VColumnFormat
import org.kopi.galite.visual.chart.VDateDimension
import org.kopi.galite.visual.chart.VDimension
import org.kopi.galite.visual.chart.VDecimalDimension
import org.kopi.galite.visual.chart.VDecimalMeasure
import org.kopi.galite.visual.chart.VIntegerDimension
import org.kopi.galite.visual.chart.VIntegerMeasure
import org.kopi.galite.visual.chart.VMeasure
import org.kopi.galite.visual.chart.VMonthDimension
import org.kopi.galite.visual.chart.VStringDimension
import org.kopi.galite.visual.chart.VTimeDimension
import org.kopi.galite.visual.chart.VTimestampDimension
import org.kopi.galite.visual.chart.VWeekDimension
import org.kopi.galite.visual.dsl.chart.ChartDimension
import org.kopi.galite.visual.dsl.chart.ChartMeasure
import org.kopi.galite.visual.dsl.common.LocalizationWriter
import org.kopi.galite.visual.dsl.form.FormField
import org.kopi.galite.visual.dsl.report.ReportField
import org.kopi.galite.visual.form.VBooleanField
import org.kopi.galite.visual.form.VConstants
import org.kopi.galite.visual.form.VDateField
import org.kopi.galite.visual.form.VField
import org.kopi.galite.visual.form.VDecimalField
import org.kopi.galite.visual.form.VImageField
import org.kopi.galite.visual.form.VIntegerField
import org.kopi.galite.visual.form.VMonthField
import org.kopi.galite.visual.form.VStringField
import org.kopi.galite.visual.form.VTextField
import org.kopi.galite.visual.form.VTimeField
import org.kopi.galite.visual.form.VTimestampField
import org.kopi.galite.visual.form.VWeekField
import org.kopi.galite.visual.report.VBooleanColumn
import org.kopi.galite.visual.report.VCalculateColumn
import org.kopi.galite.visual.report.VCellFormat
import org.kopi.galite.visual.report.VDateColumn
import org.kopi.galite.visual.report.VDecimalColumn
import org.kopi.galite.visual.report.VIntegerColumn
import org.kopi.galite.visual.report.VMonthColumn
import org.kopi.galite.visual.report.VReportColumn
import org.kopi.galite.visual.report.VStringColumn
import org.kopi.galite.visual.report.VTimeColumn
import org.kopi.galite.visual.report.VTimestampColumn
import org.kopi.galite.visual.report.VWeekColumn
import org.kopi.galite.visual.type.Date
import org.kopi.galite.visual.type.Decimal
import org.kopi.galite.visual.type.Image
import org.kopi.galite.visual.type.Month
import org.kopi.galite.visual.type.Time
import org.kopi.galite.visual.type.Timestamp
import org.kopi.galite.visual.type.Week
import org.kopi.galite.visual.visual.VColor

/**
 * A domain is a data type with predefined list of allowed values.
 *
 * @param width             the width in char of this field
 * @param height            the height in char of this field
 * @param visibleHeight     the visible height in char of this field.
 */
open class Domain<T>(val width: Int? = null,
                     val height: Int? = null,
                     val visibleHeight: Int? = null) {

  protected var isFraction = false
  protected var styled: Boolean = false
  protected var fixed: Fixed = Fixed.UNDEFINED
  protected var convert: Convert = Convert.NONE
  val ident: String = this::class.java.simpleName

  /**
   * Determines the field data type
   */
  var kClass: KClass<*>? = null

  /**
   * Builds the form field model
   */
  open fun buildFormFieldModel(formField: FormField<T>): VField {
    return with(formField) {
      when (kClass) {
        Int::class, Long::class -> VIntegerField(block.buffer,
                                                 width ?: 0,
                                                 min as? Int ?: Int.MIN_VALUE,
                                                 max as? Int ?: Int.MAX_VALUE)
        String::class -> {
          if(visibleHeight != height) {
            VStringField(block.buffer,
                         width ?: 0,
                         height ?: 1,
                         visibleHeight ?: 0,
                         (fixed.value or convert.value) and
                                 (VConstants.FDO_CONVERT_MASK or VConstants.FDO_DYNAMIC_NL),
                         styled)
          } else {
            VStringField(block.buffer,
                         width ?: 0,
                         height ?: 1,
                         (fixed.value or convert.value) and
                                 VConstants.FDO_CONVERT_MASK or
                                 VConstants.FDO_DYNAMIC_NL,
                         styled)
          }
        }
        Decimal::class -> VDecimalField(block.buffer,
                                        width!!,
                                        height ?: 6,
                                        height == null,
                                        min as? Decimal,
                                        max as? Decimal)
        Boolean::class -> VBooleanField(block.buffer)
        Date::class, java.util.Date::class -> VDateField(block.buffer)
        Month::class -> VMonthField(block.buffer)
        Week::class -> VWeekField(block.buffer)
        Time::class -> VTimeField(block.buffer)
        Timestamp::class, DateTime::class -> VTimestampField(block.buffer)
        Image::class -> VImageField(block.buffer, width!!, height!!)
        else -> {
          if(this@Domain is TEXT) {
            VTextField(block.buffer,
                       width ?: 0,
                       height ?: 1,
                       visibleHeight ?: 0,
                       if (fixed != Fixed.UNDEFINED) fixed.value else VConstants.FDO_CONVERT_NONE,
                       styled)
          }

          throw RuntimeException("Type ${kClass!!.qualifiedName} is not supported")
        }
      }
    }
  }

  /**
   * Builds the chart dimension model
   */
  open fun buildDimensionModel(dimension: ChartDimension<*>, format: VColumnFormat?): VDimension {
    return with(dimension) {
      when (kClass) {
        Int::class, Long::class ->
          VIntegerDimension(ident, format)
        Decimal::class ->
          VDecimalDimension(ident, format, height ?: 6, true)
        String::class ->
          VStringDimension(ident, format)
        Boolean::class ->
          VBooleanDimension(ident, format)
        Date::class, java.util.Date::class ->
          VDateDimension(ident, format)
        Month::class ->
          VMonthDimension(ident, format)
        Week::class ->
          VWeekDimension(ident, format)
        Time::class ->
          VTimeDimension(ident, format)
        Timestamp::class ->
          VTimestampDimension(ident, format)
        else -> throw java.lang.RuntimeException("Type ${kClass!!.qualifiedName} is not supported")
      }
    }
  }

  /**
   * Builds the chart measure model
   */
  open fun buildMeasureModel(measure: ChartMeasure<*>, color: VColor?): VMeasure {
    return with(measure) {
      when (kClass) {
        Int::class, Long::class -> VIntegerMeasure(ident, color)
        Decimal::class -> VDecimalMeasure(ident, color, height!!)
        else -> throw java.lang.RuntimeException("Type ${kClass!!.qualifiedName} is not supported")
      }
    }
  }

  /**
   * Builds the report column model
   */
  open fun buildReportFieldModel(field: ReportField<*>, function: VCalculateColumn?, format: VCellFormat?): VReportColumn {
    return with(field) {
      when (kClass) {
        Int::class, Long::class ->
          VIntegerColumn(ident, options, align.value, groupID, function, width ?: 0, format)
        String::class ->
          VStringColumn(
            ident, options, align.value, groupID, function, width ?: 0,
            height ?: 0, format
          )
        Decimal::class ->
          VDecimalColumn(
            ident, options, align.value, groupID, function, width ?: 0,
            height ?: 0, format
          )
        Boolean::class ->
          VBooleanColumn(ident, options, align.value, groupID, function, width ?: 0, format)
        Date::class, java.util.Date::class ->
          VDateColumn(ident, options, align.value, groupID, function, width ?: 0, format)
        Month::class ->
          VMonthColumn(ident, options, align.value, groupID, function, width ?: 0, format)
        Week::class ->
          VWeekColumn(ident, options, align.value, groupID, function, width ?: 0, format)
        Time::class ->
          VTimeColumn(ident, options, align.value, groupID, function, width ?: 0, format)
        Timestamp::class ->
          VTimestampColumn(ident, options, align.value, groupID, function, width ?: 0, format)
        else -> throw java.lang.RuntimeException("Type ${kClass!!.qualifiedName} is not supported")
      }
    }
  }

  /**
   * Returns the default alignment
   */
  val defaultAlignment: Int
    get() = if (kClass == Decimal::class) {
      VConstants.ALG_RIGHT
    } else {
      VConstants.ALG_LEFT
    }

  // ----------------------------------------------------------------------
  // UTILITIES
  // ----------------------------------------------------------------------
  fun hasSize(): Boolean =
          when (kClass) {
            Decimal::class, Int::class, Long::class, String::class -> true
            else -> false
          }

  // ----------------------------------------------------------------------
  // XML LOCALIZATION GENERATION
  // ----------------------------------------------------------------------
  fun genLocalization(writer: LocalizationWriter) {
    writer.genTypeDefinition(ident, this)
  }

  open fun genTypeLocalization(writer: LocalizationWriter) {
    // DO NOTHING !
  }
}