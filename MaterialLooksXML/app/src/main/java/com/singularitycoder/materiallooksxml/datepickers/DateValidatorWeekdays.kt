/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.singularitycoder.materiallooksxml.datepickers

import android.os.Parcel
import android.os.Parcelable

import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import java.util.*

/** A {@link DateValidator} that only allows dates from a given point onward to be clicked. */
class DateValidatorWeekdays internal constructor() : DateValidator {

    private val utc: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

    override fun isValid(date: Long): Boolean {
        utc.timeInMillis = date
        val dayOfWeek: Int = utc.get(Calendar.DAY_OF_WEEK)
        return dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {}

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        return o is DateValidatorWeekdays
    }

    override fun hashCode(): Int {
        val hashedFields = arrayOf<Any>()
        return hashedFields.contentHashCode()
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<DateValidatorWeekdays?> = object : Parcelable.Creator<DateValidatorWeekdays?> {
            override fun createFromParcel(source: Parcel): DateValidatorWeekdays? {
                return DateValidatorWeekdays()
            }

            override fun newArray(size: Int): Array<DateValidatorWeekdays?> {
                return arrayOfNulls(size)
            }
        }
    }
}