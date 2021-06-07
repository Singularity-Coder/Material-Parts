package com.singularitycoder.materiallooksxml

import android.os.Bundle
import android.text.Editable
import android.text.format.DateFormat.is24HourFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD
import com.google.android.material.timepicker.TimeFormat
import com.singularitycoder.materiallooksxml.databinding.FragmentMaterialComponentDetailBinding
import com.singularitycoder.materiallooksxml.tabs.DemoCollectionAdapter

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
    private lateinit var demoCollectionAdapter: DemoCollectionAdapter
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

    private fun setUpDefaults() {
        binding.layoutTabs.root.visibility = View.GONE
        binding.layoutTextFields.root.visibility = View.GONE
        binding.layoutTimePicker.root.visibility = View.GONE
        binding.root.setOnClickListener { }
        binding.layoutHeader.apply {
            ivImage.setImageResource(component.image)
            tvTitle.text = component.title
            tvSubTitle.text = component.subtitle
            tvLink.text = component.link
        }
        when (component.title) {
            Constants.MaterialComponents.APP_BAR_BOTTOM.title -> setUpAppBarBottom()
            Constants.MaterialComponents.APP_BAR_TOP.title -> setUpAppBarTop()
            Constants.MaterialComponents.BOTTOM_NAVIGATION.title -> setUpBottomNavigation()
            Constants.MaterialComponents.BUTTONS.title -> setUpButtons()
            Constants.MaterialComponents.FLOATING_ACTION_BUTTON.title -> setUpFloatingActionButton()
            Constants.MaterialComponents.CARDS.title -> setUpCards()
            Constants.MaterialComponents.CHECK_BOXES.title -> setUpCheckBoxes()
            Constants.MaterialComponents.CHIPS.title -> setUpChips()
            Constants.MaterialComponents.DATE_PICKERS.title -> setUpDatePickers()
            Constants.MaterialComponents.DIALOGS.title -> setUpDialogs()
            Constants.MaterialComponents.MENUS.title -> setUpMenus()
            Constants.MaterialComponents.NAVIGATION_DRAWER.title -> setUpNavigationDrawer()
            Constants.MaterialComponents.NAVIGATION_RAIL.title -> setUpNavigationRail()
            Constants.MaterialComponents.PROGRESS_INDICATORS.title -> setUpProgressIndicators()
            Constants.MaterialComponents.RADIO_BUTTONS.title -> setUpRadioButtons()
            Constants.MaterialComponents.SHEETS_BOTTOM.title -> setUpSheetsBottom()
            Constants.MaterialComponents.SLIDERS.title -> setUpSliders()
            Constants.MaterialComponents.SNACKBARS.title -> setUpSnackbars()
            Constants.MaterialComponents.SWITCHES.title -> setUpSwitches()
            Constants.MaterialComponents.TABS.title -> setUpTabs()
            Constants.MaterialComponents.TEXT_FIELDS.title -> setUpTextFields()
            Constants.MaterialComponents.TIME_PICKERS.title -> setUpTimePickers()
        }
    }

    private fun setUpAppBarBottom() {

    }

    private fun setUpAppBarTop() {

    }

    private fun setUpBottomNavigation() {

    }

    private fun setUpButtons() {

    }

    private fun setUpFloatingActionButton() {

    }

    private fun setUpCards() {

    }

    private fun setUpCheckBoxes() {

    }

    private fun setUpChips() {

    }

    private fun setUpDatePickers() {

    }

    private fun setUpDialogs() {

    }

    private fun setUpMenus() {

    }

    private fun setUpNavigationDrawer() {

    }

    private fun setUpNavigationRail() {

    }

    private fun setUpProgressIndicators() {

    }

    private fun setUpRadioButtons() {

    }

    private fun setUpSheetsBottom() {

    }

    private fun setUpSliders() {

    }

    private fun setUpSnackbars() {

    }

    private fun setUpSwitches() {
        // on off switch
        // switch with loader in the toggle
    }

    private fun setUpTabs() {
        // text tabs
        // leading icon tabs
        // top icon tabs
        // icon only tabs
        // Fixed
        // Scrollable
        // collapse toolbar
        // collapse toolbar & tabs
        // tabs with notification badges

        context ?: return
        binding.layoutTabs.root.visibility = View.VISIBLE

        demoCollectionAdapter = DemoCollectionAdapter(this)
        binding.layoutTabs.viewPager.adapter = demoCollectionAdapter
        val tab = binding.layoutTabs.tabLayout1.getTabAt(0)

        // Get badge from tab (or create one if none exists)
        val badge = tab?.orCreateBadge?.apply {
            // Customize badge
            number = number
            // Remove badge from tab
            tab.removeBadge()
            setContentDescriptionNumberless("contentDescription")
            setContentDescriptionQuantityStringsResource(R.plurals.tab_layout_1)
            setContentDescriptionExceedsMaxBadgeNumberStringResource(R.string.app_name)
        }

        binding.layoutTabs.tabLayout1.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Handle tab select
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselect
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselect
            }
        })

        TabLayoutMediator(binding.layoutTabs.tabLayout1, binding.layoutTabs.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "lable ${position + 1}"
                    tab.icon = requireContext().getDrawable(R.drawable.ic_baseline_alternate_email_24)
                }
                1 -> {
                    tab.text = "lable ${position + 1}"
                    tab.icon = requireContext().getDrawable(R.drawable.ic_baseline_info_24)
                }
            }
        }.attach()
    }

    private fun setUpTextFields() {
        context ?: return
        binding.layoutTextFields.root.visibility = View.VISIBLE
        val professionAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, Constants.professionArray)
        val hobbyAdapter = ArrayAdapter(requireContext(), R.layout.list_item_custom_array_adapter, Constants.hobbyArray)
        (binding.layoutTextFields.etProfession.editText as? AutoCompleteTextView)?.setAdapter(professionAdapter)
        (binding.layoutTextFields.etHobby.editText as? AutoCompleteTextView)?.setAdapter(hobbyAdapter)
        binding.layoutTextFields.etName.editText?.addTextChangedListener(afterTextChanged = { it: Editable? ->
            if (it?.length ?: 0 < 3) binding.layoutTextFields.etName.error = "Name must contain at least 3 characters!"
            else binding.layoutTextFields.etName.error = null
        })
        binding.layoutTextFields.etEmail.apply {
            setEndIconOnClickListener {
                Snackbar.make(binding.root, "Email must contain only @. as special characters!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("GOT IT") { }
                    .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.black))
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.purple_200))
                    .show()
            }
            addOnEditTextAttachedListener { it: TextInputLayout ->
                // Later
            }
            addOnEndIconChangedListener { textInputLayout, previousIcon ->
                // Later
            }
        }
        binding.layoutTextFields.btnLogin.setOnClickListener {
            binding.layoutResult.tvResult.text = """
                Name: ${binding.layoutTextFields.etName.editText?.text}
                Email: ${binding.layoutTextFields.etEmail.editText?.text}@gmail.com
                Profession: ${binding.layoutTextFields.etProfession.editText?.text}
                Hobby: ${binding.layoutTextFields.etHobby.editText?.text}
                Password: ${binding.layoutTextFields.etPassword.editText?.text}
            """.trimIndent()
        }

        // Programmatic Text Fields
        val textInputLayout = TextInputLayout(requireContext())
        val editText = TextInputEditText(textInputLayout.context)
    }

    private fun setUpTimePickers() {
        context ?: return
        binding.layoutTimePicker.root.visibility = View.VISIBLE
        val clockFormat = if (is24HourFormat(context)) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
        binding.layoutTimePicker.btnTimePicker.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTitleText("Select Appointment time")
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .build()
            timePicker.show(parentFragmentManager, "tag_time_picker")
            timePicker.apply {
                addOnPositiveButtonClickListener {
                    binding.layoutResult.tvResult.text = """
                    Hours: ${timePicker.hour}
                    Minutes: ${timePicker.minute}
                    InputMode: ${timePicker.inputMode}
                """.trimIndent()
                }
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
                addOnPositiveButtonClickListener {
                    binding.layoutResult.tvResult.text = """
                    Hours: ${timePickerKeyboardOnly.hour}
                    Minutes: ${timePickerKeyboardOnly.minute}
                    InputMode: ${timePickerKeyboardOnly.inputMode}
                """.trimIndent()
                }
                addOnNegativeButtonClickListener { binding.layoutResult.tvResult.text = "You Cancelled!" }
            }
        }
    }
}