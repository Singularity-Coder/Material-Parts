package com.singularitycoder.materiallooksxml

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.format.DateFormat.is24HourFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD
import com.google.android.material.timepicker.TimeFormat
import com.singularitycoder.materiallooksxml.Constants.MaterialComponents.*
import com.singularitycoder.materiallooksxml.databinding.FragmentMaterialComponentDetailBinding
import com.singularitycoder.materiallooksxml.tabs.DemoCollectionAdapter
import java.text.NumberFormat
import java.util.*

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
    private lateinit var myContext: Context
    private lateinit var myActivity: MainActivity
    private lateinit var binding: FragmentMaterialComponentDetailBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myContext = context
        myActivity = context as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMaterialComponentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpDefaults()
    }

    private fun setUpDefaults() {
        binding.apply {
            layoutSheetsBottom.root.visibility = View.GONE
            layoutSliders.root.visibility = View.GONE
            layoutSnackbars.root.visibility = View.GONE
            layoutSwitches.root.visibility = View.GONE
            layoutTabs.root.visibility = View.GONE
            layoutTextFields.root.visibility = View.GONE
            layoutTimePickers.root.visibility = View.GONE
        }
        binding.root.setOnClickListener { }
        binding.layoutHeader.apply {
            ivImage.setImageResource(component.image)
            tvTitle.text = component.title
            tvSubTitle.text = component.subtitle
            tvLink.text = component.link
        }
        when (component.title) {
            APP_BAR_BOTTOM.title -> setUpAppBarBottom()
            APP_BAR_TOP.title -> setUpAppBarTop()
            BOTTOM_NAVIGATION.title -> setUpBottomNavigation()
            BUTTONS.title -> setUpButtons()
            FLOATING_ACTION_BUTTON.title -> setUpFloatingActionButton()
            CARDS.title -> setUpCards()
            CHECK_BOXES.title -> setUpCheckBoxes()
            CHIPS.title -> setUpChips()
            DATE_PICKERS.title -> setUpDatePickers()
            DIALOGS.title -> setUpDialogs()
            MENUS.title -> setUpMenus()
            NAVIGATION_DRAWER.title -> setUpNavigationDrawer()
            NAVIGATION_RAIL.title -> setUpNavigationRail()
            PROGRESS_INDICATORS.title -> setUpProgressIndicators()
            RADIO_BUTTONS.title -> setUpRadioButtons()
            SHEETS_BOTTOM.title -> setUpSheetsBottom()
            SLIDERS.title -> setUpSliders()
            SNACKBARS.title -> setUpSnackbars()
            SWITCHES.title -> setUpSwitches()
            TABS.title -> setUpTabs()
            TEXT_FIELDS.title -> setUpTextFields()
            TIME_PICKERS.title -> setUpTimePickers()
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
        // Standard - Like music player strip
        // Modal - List Dialog replacement
        // Expanding - G Maps, shopping kart, unread messages

        binding.layoutSheetsBottom.root.visibility = View.VISIBLE
        val modalBottomSheet = binding.layoutSheetsBottom.modalBottomSheet.root
        val bottomSheetBehavior = BottomSheetBehavior.from(modalBottomSheet)
        binding.layoutSheetsBottom.btnModalBottomSheet.setOnClickListener {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
            }
        }
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) = Unit
            override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
        })
    }

    private fun setUpSliders() {
        binding.layoutSliders.root.visibility = View.VISIBLE
        binding.layoutSliders.sliderContinuous.apply {
            valueFrom = 0.0F
            valueTo = 100.0F
            value = 10.0F   // starting value
            setLabelFormatter { value: Float -> "$value <<((" }
            addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: Slider) {
                    // Responds to when slider's touch event is being started
                }

                override fun onStopTrackingTouch(slider: Slider) {
                    // Responds to when slider's touch event is being stopped
                }
            })
            addOnChangeListener { slider, value, fromUser ->
                // Responds to when slider's value is changed
                binding.layoutResult.tvResult.text = "Continuous Slider value is $value"
            }
        }
        binding.layoutSliders.sliderDiscreteRangeSelection.apply {
            valueFrom = 0.0F
            valueTo = 10.0F
            stepSize = 1.0F
            values = listOf(1.0F, 3.0F)   // starting values
            setLabelFormatter { value: Float ->
                val format = NumberFormat.getCurrencyInstance()
                format.maximumFractionDigits = 0
                format.currency = Currency.getInstance("USD")
                format.format(value.toDouble())
            }
            addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: RangeSlider) {
                    // Responds to when slider's touch event is being started
                }

                override fun onStopTrackingTouch(slider: RangeSlider) {
                    // Responds to when slider's touch event is being stopped
                }
            })
            addOnChangeListener { rangeSlider, value, fromUser ->
                // Responds to when slider's value is changed
                binding.layoutResult.tvResult.text = "Discrete Range Selection Slider value is ${value}"
            }
        }
        binding.layoutSliders.sliderCustom.apply {
//            trackHeight
//            trackTintList
//            trackActiveTintList
//            trackInactiveTintList
//            thumbTintList
//            thumbRadius
//            setThumbRadiusResource(myContext.resources.getDimension(R.dimen.thumb_radius))
//            setThumbElevationResource()
//            thumbElevation
//            haloTintList
//            setHaloRadiusResource()
//            haloRadius
//            thumbStrokeColor
//            setThumbStrokeColorResource()
//            thumbStrokeWidth
//            setThumbStrokeWidthResource()
//            labelBehavior
//            tickTintList
//            tickActiveTintList
//            tickInactiveTintList
//            isTickVisible
            addOnChangeListener { slider, value, fromUser ->
                binding.layoutResult.tvResult.text = "Custom Slider value is $value"
            }
        }
        binding.layoutSliders.sliderRangeSelectionCustom.apply {
//            minSeparation
            addOnChangeListener { slider, value, fromUser ->
                binding.layoutResult.tvResult.text = "Custom Range Selection Slider value is $value"
            }
        }
    }

    private fun setUpSnackbars() {
        binding.layoutSnackbars.root.visibility = View.VISIBLE
        binding.layoutSnackbars.apply {
            btnSnackBarSimple.setOnClickListener { Snackbar.make(binding.root, "You clicked me!", Snackbar.LENGTH_SHORT).show() }
            btnSnackBarAction.setOnClickListener {
                Snackbar.make(binding.root, "Email deleted!", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") { binding.layoutResult.tvResult.text = "You undid email deletion!" }
                    .show()
            }
            btnSnackBarWithLongText.setOnClickListener {
                Snackbar.make(
                    binding.root,
                    "Email must contain only @. as special characters. Otherwise hackers can bust your email. You will then cry like a baby. So be careful!",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("DON'T TELL ME AGAIN") { binding.layoutResult.tvResult.text = "\"DON'T TELL ME AGAIN\" Clicked!" }.show()
            }
            btnSnackBarCustom.setOnClickListener {
                Snackbar.make(binding.root, "Email must contain only @. as special characters!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK") { binding.layoutResult.tvResult.text = "You got it!" }
                    .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.teal_300))
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.teal_900))
                    .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                    .show()
            }
            btnSnackBarCustomPosition.setOnClickListener {
                Snackbar.make(binding.root, "I am at a different place than usual. How strange!", Snackbar.LENGTH_LONG)
                    .setAnchorView(btnSnackBarSimple)
                    .show()
            }
        }

        fun showMultiPurposeSnackBar(view: View, @StringRes message: Int, anchorView: View? = null, duration: Int = Snackbar.LENGTH_SHORT) {
            Snackbar.make(view, message, duration).apply {
                this.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
                if (null != anchorView) this.anchorView = anchorView
                this.show()
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun setUpSwitches() {
        binding.layoutSwitches.root.visibility = View.VISIBLE
        binding.layoutSwitches.apply {
            switchBasic.isChecked = false
            switchCustom.isChecked = true
        }
        binding.layoutSwitches.switchBasic.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.layoutResult.tvResult.text = "Basic Switch is ${if (isChecked) "On!" else "Off!"}"
        }
        binding.layoutSwitches.switchCustom.apply {
            setOnCheckedChangeListener { buttonView, isChecked ->
                binding.layoutResult.tvResult.text = "Custom Switch is ${if (isChecked) "On!" else "Off!"}"
                if (isChecked) {
                    thumbDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_stars_24)
                    thumbTintList = ContextCompat.getColorStateList(requireContext(), R.color.purple_700)
                    trackDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_dehaze_24)
                    trackTintList = ContextCompat.getColorStateList(requireContext(), R.color.teal_200)
                    text = "Custom Switch is On!"
                    setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.purple_700))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) setTextAppearance(android.R.attr.textAppearanceLarge)
                } else {
                    thumbDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_stars_24)
                    thumbTintList = ContextCompat.getColorStateList(requireContext(), android.R.color.darker_gray)
                    trackDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_dehaze_24)
                    trackTintList = ContextCompat.getColorStateList(requireContext(), android.R.color.darker_gray)
                    text = "Custom Switch is Off!"
                    setTextColor(ContextCompat.getColorStateList(requireContext(), android.R.color.darker_gray))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) setTextAppearance(android.R.attr.textAppearanceSmall)
                }
            }
        }
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
        binding.layoutTimePickers.root.visibility = View.VISIBLE
        val clockFormat = if (is24HourFormat(myContext)) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
        binding.layoutTimePickers.btnTimePicker.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTitleText("Select Appointment time")
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .build()
            timePicker.show(parentFragmentManager, "tag_time_picker")
            timePicker.addOnPositiveButtonClickListener {
                binding.layoutResult.tvResult.text = """
                    Hours: ${timePicker.hour}
                    Minutes: ${timePicker.minute}
                    InputMode: ${timePicker.inputMode}
                """.trimIndent()
            }
            timePicker.addOnNegativeButtonClickListener { binding.layoutResult.tvResult.text = "You Cancelled!" }
        }
        binding.layoutTimePickers.btnTimePickerKeyboardOnly.setOnClickListener {
            val timePickerKeyboardOnly = MaterialTimePicker.Builder()
                .setTitleText("Select Appointment time")
                .setInputMode(INPUT_MODE_KEYBOARD)
                .setTimeFormat(clockFormat)
                .setHour(12)
                .setMinute(10)
                .build()
            timePickerKeyboardOnly.show(parentFragmentManager, "tag_time_picker_keyboard_only")
            timePickerKeyboardOnly.addOnPositiveButtonClickListener {
                binding.layoutResult.tvResult.text = """
                    Hours: ${timePickerKeyboardOnly.hour}
                    Minutes: ${timePickerKeyboardOnly.minute}
                    InputMode: ${timePickerKeyboardOnly.inputMode}
                """.trimIndent()
            }
            timePickerKeyboardOnly.addOnNegativeButtonClickListener { binding.layoutResult.tvResult.text = "You Cancelled!" }
        }
    }
}
