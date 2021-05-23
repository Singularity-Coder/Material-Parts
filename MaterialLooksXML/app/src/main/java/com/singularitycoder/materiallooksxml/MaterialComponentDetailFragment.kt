package com.singularitycoder.materiallooksxml

import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD
import com.google.android.material.timepicker.TimeFormat
import com.singularitycoder.materiallooksxml.databinding.FragmentMaterialComponentDetailBinding

class MaterialComponentDetailFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(
            position: Int,
            component: MaterialComponent
        ) = MaterialComponentDetailFragment().apply {
            this.position = position
            this.component = component
        }
    }

    private var position: Int = 0
    private lateinit var component: MaterialComponent
    private lateinit var binding: FragmentMaterialComponentDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMaterialComponentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpDefaults()
    }

    private fun setUpTimePickers() {
        binding.layoutTimePicker.root.visibility = View.VISIBLE
        val clockFormat = if (is24HourFormat(context)) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
        binding.layoutTimePicker.btnTimePicker.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTitleText("Select Appointment time")
                .setTimeFormat(clockFormat)
                .setHour(12)
                .setMinute(10)
                .build()
            timePicker.show(parentFragmentManager, "tag_time_picker")
            timePicker.apply {
                addOnPositiveButtonClickListener { binding.layoutResult.tvResult.text = "Hours: ${timePicker.hour}, Minutes: ${timePicker.minute}, InputMode: ${timePicker.inputMode}." }
                addOnNegativeButtonClickListener { binding.layoutResult.tvResult.text = "You Cancelled!" }
            }
        }
        binding.layoutTimePicker.btnTimePickerKeyboardOnly.setOnClickListener {
            val timePickerKeyboardOnly = MaterialTimePicker.Builder()
                .setTitleText("Select Appointment time")
                .setInputMode(INPUT_MODE_KEYBOARD)
                .setTimeFormat(clockFormat)
                .setHour(12)
                .setMinute(10)
                .build()
            timePickerKeyboardOnly.show(parentFragmentManager, "tag_time_picker_keyboard_only")
            timePickerKeyboardOnly.apply {
                addOnPositiveButtonClickListener { binding.layoutResult.tvResult.text = "Hours: ${timePickerKeyboardOnly.hour}, Minutes: ${timePickerKeyboardOnly.minute}, InputMode: ${timePickerKeyboardOnly.inputMode}." }
                addOnNegativeButtonClickListener { binding.layoutResult.tvResult.text = "You Cancelled!" }
            }
        }
    }

    private fun setUpDefaults() {
        binding.layoutTimePicker.root.visibility = View.GONE
        binding.root.setOnClickListener { }
        binding.layoutHeader.apply {
            ivImage.setImageResource(component.image)
            tvTitle.text = component.title
            tvSubTitle.text = component.subtitle
            tvLink.text = component.link
        }
        when (component.title) {
            Constants.MaterialComponents.APP_BAR_BOTTOM.title -> {
            }
            Constants.MaterialComponents.APP_BAR_TOP.title -> {
            }
            Constants.MaterialComponents.BOTTOM_NAVIGATION.title -> {
            }
            Constants.MaterialComponents.BUTTONS.title -> {
            }
            Constants.MaterialComponents.FLOATING_ACTION_BUTTON.title -> {
            }
            Constants.MaterialComponents.CARDS.title -> {
            }
            Constants.MaterialComponents.CHECK_BOXES.title -> {
            }
            Constants.MaterialComponents.CHIPS.title -> {
            }
            Constants.MaterialComponents.DATE_PICKERS.title -> {
            }
            Constants.MaterialComponents.DIALOGS.title -> {
            }
            Constants.MaterialComponents.MENUS.title -> {
            }
            Constants.MaterialComponents.NAVIGATION_DRAWER.title -> {
            }
            Constants.MaterialComponents.NAVIGATION_RAIL.title -> {
            }
            Constants.MaterialComponents.PROGRESS_INDICATORS.title -> {
            }
            Constants.MaterialComponents.RADIO_BUTTONS.title -> {
            }
            Constants.MaterialComponents.SHEETS_BOTTOM.title -> {
            }
            Constants.MaterialComponents.SLIDERS.title -> {
            }
            Constants.MaterialComponents.SNACKBARS.title -> {
            }
            Constants.MaterialComponents.SWITCHES.title -> {
            }
            Constants.MaterialComponents.TABS.title -> {
            }
            Constants.MaterialComponents.TEXT_FIELDS.title -> {
            }
            Constants.MaterialComponents.TIME_PICKERS.title -> setUpTimePickers()
        }
    }
}