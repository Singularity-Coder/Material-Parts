package com.singularitycoder.materiallooksxml

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.Spanned
import android.text.format.DateFormat.is24HourFormat
import android.text.style.ImageSpan
import android.view.*
import android.view.MenuItem.SHOW_AS_ACTION_WITH_TEXT
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.annotation.MenuRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.core.util.toAndroidXPair
import androidx.core.view.*
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.progressindicator.BaseProgressIndicator
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator
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
import com.singularitycoder.materiallooksxml.Constants.MAX_DURATION_IN_MILLIS
import com.singularitycoder.materiallooksxml.Constants.REPEAT_DURATION_IN_MILLIS
import com.singularitycoder.materiallooksxml.Constants.TAG_MODAL_BOTTOM_SHEET
import com.singularitycoder.materiallooksxml.MaterialComponents.*
import com.singularitycoder.materiallooksxml.databinding.FragmentMaterialComponentDetailBinding
import com.singularitycoder.materiallooksxml.databinding.ItemBottomSheetBinding
import com.singularitycoder.materiallooksxml.dialogs.FullScreenDialogFragment
import com.singularitycoder.materiallooksxml.dialogs.Person
import com.singularitycoder.materiallooksxml.sheetsbottom.ModalBottomSheetDialogFragment
import com.singularitycoder.materiallooksxml.tabs.DemoCollectionAdapter
import com.singularitycoder.materiallooksxml.tabs.DummyFragment
import java.security.SecureRandom
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class MaterialComponentDetailFragment(val component: MaterialComponent) : Fragment() {

    private lateinit var myContext: Context
    private lateinit var myActivity: MainActivity
    private lateinit var binding: FragmentMaterialComponentDetailBinding

    // Fragments must have public no-arg constructor
    constructor() : this(MaterialComponent(image = 0, title = "", subtitle = "", link = ""))

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myContext = context
        myActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMaterialComponentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        setUpDefaults()
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.window?.enableScreenshot()    // Not working
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.invokeSetMenuIconMethod()
        if (component.title == MENUS.title) inflater.inflate(R.menu.overflow_menu, menu)
        setMarginBtwMenuIconAndText(context = myContext, menu = menu, iconMarginDp = 16)
        menu.forEach {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                it.iconTintList = ContextCompat.getColorStateList(myContext, R.color.purple_500)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        fun onMenuItemClick(item: MenuItem): Boolean {
            binding.layoutResult.tvResult.text = "Overflow Menu Item \"${item.title}\" got selected!"
            return true
        }

        when (item.itemId) {
            R.id.item_mail -> onMenuItemClick(item = item)
            R.id.item_message -> onMenuItemClick(item = item)
            R.id.item_share -> onMenuItemClick(item = item)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        menu.invokeSetMenuIconMethod()  // This should happen before menu inflation for icons to show up

        val tvContextMenu = v as TextView

        fun onContextMenuItemClick(menuItem: MenuItem?, tvContextMenu: TextView): Boolean {
            val myMenuInfo = menuItem?.menuInfo as? AdapterView.AdapterContextMenuInfo // Another way to get menu info
            binding.layoutResult.tvResult.text = "${menuItem?.title} \"${tvContextMenu.text}\""
            return true
        }

        // Icons not showing up
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            menu.add("Cut")
                .setIcon(R.drawable.ic_baseline_content_cut_24)
                .setIconTintList(ContextCompat.getColorStateList(myContext, R.color.purple_500))
                .setOnMenuItemClickListener { it: MenuItem? -> onContextMenuItemClick(menuItem = it, tvContextMenu = tvContextMenu) }
                .setShowAsAction(SHOW_AS_ACTION_WITH_TEXT)
            menu.add("Copy")
                .setIcon(R.drawable.ic_baseline_content_copy_24)
                .setIconTintList(ContextCompat.getColorStateList(myContext, R.color.purple_500))
                .setOnMenuItemClickListener { it: MenuItem? -> onContextMenuItemClick(menuItem = it, tvContextMenu = tvContextMenu) }
                .setShowAsAction(SHOW_AS_ACTION_WITH_TEXT)
            menu.add("Paste")
                .setIcon(R.drawable.ic_baseline_content_paste_24)
                .setIconTintList(ContextCompat.getColorStateList(myContext, R.color.purple_500))
                .setOnMenuItemClickListener { it: MenuItem? -> onContextMenuItemClick(menuItem = it, tvContextMenu = tvContextMenu) }
                .setShowAsAction(SHOW_AS_ACTION_WITH_TEXT)
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            title = getString(R.string.app_name)
            setTitleTextColor(ContextCompat.getColor(myContext, R.color.white))
            navigationIcon = ContextCompat.getDrawable(myContext, R.drawable.ic_baseline_arrow_back_24)
            setNavigationIconTint(ContextCompat.getColor(myContext, R.color.white))
        }

        myActivity.apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }

        // Click wont work until toolbar is assigned to activity
        binding.toolbar.setNavigationOnClickListener { myActivity.onBackPressed() }
    }

    private fun setUpDefaults() {
        binding.apply {
            layoutChips.root.gone()
            layoutDataTables.root.gone()
            layoutDatePickers.root.gone()
            layoutDialogs.root.gone()
            layoutDividers.root.gone()
            layoutImageLists.root.gone()
            layoutLists.root.gone()
            layoutMenus.root.gone()
            layoutNavigationDrawers.root.gone()
            layoutNavigationRail.root.gone()
            layoutProgressIndicators.root.gone()
            layoutRadioButtons.root.gone()
            layoutSheetsBottom.root.gone()
            layoutSheetsSide.root.gone()
            layoutSliders.root.gone()
            layoutSnackbars.root.gone()
            layoutSwitches.root.gone()
            layoutTabs.root.gone()
            layoutTextFields.root.gone()
            layoutTooltips.root.gone()
            layoutTimePickers.root.gone()
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
            BACKDROP.title -> setUpBackdrop()
            BANNERS.title -> setUpBanners()
            BOTTOM_NAVIGATION.title -> setUpBottomNavigation()
            BUTTONS.title -> setUpButtons()
            FLOATING_ACTION_BUTTON.title -> setUpFloatingActionButton()
            CARDS.title -> setUpCards()
            CHECK_BOXES.title -> setUpCheckBoxes()
            CHIPS.title -> setUpChips()
            DATA_TABLES.title -> setUpDataTables()
            DATE_PICKERS.title -> setUpDatePickers()
            DIALOGS.title -> setUpDialogs()
            DIVIDERS.title -> setUpDividers()
            IMAGE_LISTS.title -> setUpImageLists()
            LISTS.title -> setUpLists()
            MENUS.title -> setUpMenus()
            NAVIGATION_DRAWER.title -> setUpNavigationDrawer()
            NAVIGATION_RAIL.title -> setUpNavigationRail()
            PROGRESS_INDICATORS.title -> setUpProgressIndicators()
            RADIO_BUTTONS.title -> setUpRadioButtons()
            SHEETS_BOTTOM.title -> setUpSheetsBottom()
            SHEETS_SIDE.title -> setUpSheetsSide()
            SLIDERS.title -> setUpSliders()
            SNACKBARS.title -> setUpSnackbars()
            SWITCHES.title -> setUpSwitches()
            TABS.title -> setUpTabs()
            TEXT_FIELDS.title -> setUpTextFields()
            TOOLTIPS.title -> setUpTooltips()
            TIME_PICKERS.title -> setUpTimePickers()
        }
    }

    private fun setUpAppBarBottom() {
        binding.toolbar.title = APP_BAR_BOTTOM.title
    }

    private fun setUpAppBarTop() {
        binding.toolbar.title = APP_BAR_TOP.title
    }

    private fun setUpBackdrop() {
        binding.toolbar.title = BACKDROP.title
    }

    private fun setUpBanners() {
        binding.toolbar.title = BANNERS.title
    }

    private fun setUpBottomNavigation() {
        binding.toolbar.title = BOTTOM_NAVIGATION.title
    }

    private fun setUpButtons() {
        binding.toolbar.title = BUTTONS.title
    }

    private fun setUpFloatingActionButton() {
        // lift fab when snackbar shown
        binding.toolbar.title = FLOATING_ACTION_BUTTON.title
    }

    private fun setUpCards() {
        binding.toolbar.title = CARDS.title
    }

    private fun setUpCheckBoxes() {
        binding.toolbar.title = CHECK_BOXES.title
    }

    private fun setUpChips() {
        // Input Chips - any number - like entering hash tags in twitter (OR) adding recipients in gmail. They have delete option
        // Choice Chips - single choice - veg, non-veg, both (OR) small, medium, large, extra-large
        // Filter Chips - multiple choice - filter stuff while shopping in amazon
        // Action Chips - these are just buttons/switches with an icon and rounded corners with on/off states

        // Action Chips with progress bar inside
        // Chips allow users to enter information, make selections, filter content, or trigger actions.

        binding.apply {
            toolbar.title = CHIPS.title
            layoutChips.root.visible()
        }

        // RTL-friendly chip layout
        binding.layoutChips.chip.apply {
            layoutDirection = View.LAYOUT_DIRECTION_LOCALE
            setOnClickListener {
                binding.layoutResult.tvResult.text = "Chip Selected"
            }
            setOnCloseIconClickListener {
                binding.layoutResult.tvResult.text = "Chip Closed"
            }
            // Not Selected
            setOnCheckedChangeListener { chip, isChecked ->
                Snackbar.make(binding.coordinatorLayoutRoot, if (isChecked) "Chip Selected" else "Chip Unselected", Snackbar.LENGTH_SHORT).show()
            }
        }

        val checkedChipId = binding.layoutChips.chipGroupMultiRow.checkedChipId // Returns View.NO_ID if singleSelection = false
        val checkedChipIds = binding.layoutChips.chipGroupMultiRow.checkedChipIds // Returns a list of the selected chips' IDs, if any

        binding.layoutChips.chipGroupMultiRow.setOnCheckedChangeListener { group, checkedId ->
            // Responds to child chip checked/unchecked
        }

        var spannedLength = 0
        val chipLength = 4
        var textOnTextChanged = ""

        // Input Chips - hashtags, gmail recipients
        fun addInputChip(enteredText: String) {
//            if (enteredText.length - abs(spannedLength) != abs(chipLength)) return
            val chipDrawable = ChipDrawable.createFromResource(myContext, R.xml.item_chip).apply {
                text = binding.layoutChips.etEmail.editText?.text
                setBounds(0, 0, this.intrinsicWidth, this.intrinsicHeight)
            }
            val imageSpan = ImageSpan(chipDrawable)
            val text = binding.layoutChips.etEmail.editText?.text
            text?.setSpan(imageSpan, 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//            spannedLength = enteredText.length
        }

        // https://stackoverflow.com/questions/50574943/how-to-add-chips-from-material-components-library-to-input-field-in-android
        binding.layoutChips.etEmail.editText?.setOnTextChanged { it: String ->
//            if (it.length == (spannedLength - chipLength)) spannedLength = it.length
        }
        binding.layoutChips.etEmail.editText?.doAfterTextChanged { it: Editable? ->
//            if (it?.length ?: 0 > 15 )
//            addInputChip(enteredText = it.toString())
            textOnTextChanged = it.toString()
        }

        binding.layoutChips.etEmail.editText?.setOnEditorActionListener { v, actionId, event ->
            addInputChip(enteredText = textOnTextChanged)
            actionId == EditorInfo.IME_ACTION_DONE
        }
    }

    private fun setUpDataTables() {
        binding.apply {
            toolbar.title = DATA_TABLES.title
            layoutDataTables.root.visible()
            layoutResult.tvResult.text = "Data Tables missing in Material library"
        }
    }

    private fun setUpDatePickers() {
        // Calendar Date Picker
        // Date Range Picker
        // Input Picker for Date
        // Input Picker for Range

        binding.apply {
            toolbar.title = DATE_PICKERS.title
            layoutDatePickers.root.visible()
        }

        fun giveCalendarConstraintBuilder(): CalendarConstraints.Builder {
            // https://www.youtube.com/watch?v=m3yj7JaTTPI
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.clear()

            val today = MaterialDatePicker.todayInUtcMilliseconds()
            calendar.timeInMillis = today

            calendar.set(Calendar.MONTH, Calendar.JANUARY)
            val january = calendar.timeInMillis

            calendar.set(Calendar.MONTH, Calendar.MARCH)
            val defaultMonth = calendar.timeInMillis

            calendar.set(Calendar.MONTH, Calendar.DECEMBER)
            val december = calendar.timeInMillis

            return CalendarConstraints.Builder().apply {
                setValidator(DateValidatorPointForward.now())   // disables all previous dates until today
//                setValidator(DateValidatorWeekdays())   // disables all weekdays
//                setValidator(DateValidatorPointForward.from(defaultMonth))    // disables all dates from march
                setOpenAt(defaultMonth) // With this calendar always opens at March month instead of device date
                setStart(january)
                setEnd(december)
            }
        }

        fun showDatePicker(type: UByte = 0u) {
            if (type.toInt() > 2) {
                showDatePicker(type = 0u)
                return
            }
            val datePicker = when (type.toInt()) {
                0 -> {
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())  // Opens the date picker with today's date selected.
                        .build()
                }
                1 -> {
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setCalendarConstraints(giveCalendarConstraintBuilder().build())
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())  // Opens the date picker with today's date selected.
                        .build()
                }
                else -> {
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())  // Opens the date picker with today's date selected.
                        .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                        .build()
                }
            }
            datePicker.show(myActivity.supportFragmentManager, "TAG_DATE_PICKER")
            datePicker.apply {
                addOnPositiveButtonClickListener { it: Long? ->
                    binding.layoutResult.tvResult.text = convertLongToTime(time = it ?: return@addOnPositiveButtonClickListener, type = 3u)
                }
                addOnNegativeButtonClickListener {
                    binding.layoutResult.tvResult.text = "You cancelled"
                }
            }
        }

        fun showDateRangePicker(type: UByte = 0u) {
            if (type.toInt() > 2) {
                showDateRangePicker(type = 0u)
                return
            }
            val dateRangePicker = when (type.toInt()) {
                0 -> {
                    MaterialDatePicker.Builder.dateRangePicker()
                        .setTitleText("Select dates")
                        .setSelection(Pair(first = MaterialDatePicker.thisMonthInUtcMilliseconds(), second = MaterialDatePicker.todayInUtcMilliseconds()).toAndroidXPair())
                        .build()
                }
                1 -> {
                    MaterialDatePicker.Builder.dateRangePicker()
                        .setTitleText("Select dates")
                        .setSelection(Pair(first = MaterialDatePicker.thisMonthInUtcMilliseconds(), second = MaterialDatePicker.todayInUtcMilliseconds()).toAndroidXPair())
                        .setCalendarConstraints(giveCalendarConstraintBuilder().build())
                        .build()
                }
                else -> {
                    MaterialDatePicker.Builder.dateRangePicker()
                        .setTitleText("Select dates")
                        .setSelection(Pair(first = MaterialDatePicker.thisMonthInUtcMilliseconds(), second = MaterialDatePicker.todayInUtcMilliseconds()).toAndroidXPair())
                        .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                        .build()
                }
            }
            dateRangePicker.show(myActivity.supportFragmentManager, "TAG_DATE_RANGE_PICKER")
            dateRangePicker.apply {
                addOnPositiveButtonClickListener { it: Pair<Long, Long>? ->
                    binding.layoutResult.tvResult.text = "Range from ${
                        convertLongToTime(time = it?.first ?: return@addOnPositiveButtonClickListener, type = 3u)
                    } to ${
                        convertLongToTime(time = it?.second ?: return@addOnPositiveButtonClickListener, type = 3u)
                    }"
                }
                addOnNegativeButtonClickListener { binding.layoutResult.tvResult.text = "You cancelled" }
            }
        }

        binding.layoutDatePickers.apply {
            btnDatePicker.setOnClickListener { showDatePicker() }
            btnDatePickerConstraints.setOnClickListener { showDatePicker(type = 1u) }
            btnDateInputPicker.setOnClickListener { showDatePicker(type = 2u) }
        }

        binding.layoutDatePickers.apply {
            btnDateRangePicker.setOnClickListener { showDateRangePicker() }
            btnDateRangePickerConstraints.setOnClickListener { showDateRangePicker(type = 1u) }
            btnDateRangeInputPicker.setOnClickListener { showDateRangePicker(type = 2u) }
        }
    }

    private fun setUpDialogs() {
        // Super rounded dialogs
        // Dismiss button until item is selected
        // Single action dialog
        // Custom Dialog
        // Full-Screen Dialog - dialogs over full screen dialogs

        binding.apply {
            toolbar.title = DIALOGS.title
            layoutDialogs.root.visible()
        }

        fun showThemedAlertDialog(@StyleRes theme: Int, showIcon: Boolean = true, roundCorners: Boolean = true) {
            MaterialAlertDialogBuilder(myContext, theme).apply {
                setCancelable(false)
                if (showIcon) setIcon(R.drawable.ic_baseline_info_24)
                setTitle("Alert Dialog")
                setMessage(resources.getString(R.string.long_message))
                if (roundCorners) background = ContextCompat.getDrawable(myContext, R.drawable.alert_dialog_bg)
                setNegativeButton("Decline") { dialog, which -> binding.layoutResult.tvResult.text = "You declined" }
                setPositiveButton("Accept") { dialog, which -> binding.layoutResult.tvResult.text = "You accepted" }
                setNeutralButton("Cancel") { dialog, which -> binding.layoutResult.tvResult.text = "You cancelled" }
                show()
            }
        }

        fun showThemedSimpleDialog(@StyleRes theme: Int) {
            MaterialAlertDialogBuilder(myContext, theme).apply {
                setTitle("My Hobbies")
                setItems(Constants.hobbyList.toTypedArray()) { dialog, which ->
                    binding.layoutResult.tvResult.text = Constants.hobbyList[which]
                }
                show()
            }
        }

        fun showThemedConfirmationSingleChoiceDialog(@StyleRes theme: Int) {
            var position = 0
            MaterialAlertDialogBuilder(myContext, theme).apply {
                setTitle("My Professions")
                setNeutralButton("Cancel") { dialog, which ->
                    binding.layoutResult.tvResult.text = "You cancelled"
                }
                setPositiveButton("Ok") { dialog, which ->
                    binding.layoutResult.tvResult.text = Constants.professionList[position]
                }
                setSingleChoiceItems(Constants.professionList.toTypedArray(), /* default checked item */0) { dialog, which ->
                    position = which
                }
                show()
            }
        }

        fun showThemedConfirmationMultipleChoiceDialog(@StyleRes theme: Int) {
            val checkedList = ArrayList<String>()
            val defaultCheckedItems = booleanArrayOf(false, true, true, false, false, true)
            MaterialAlertDialogBuilder(myContext, theme).apply {
                setTitle("My Professions")
                setNeutralButton("Cancel") { dialog, which ->
                    binding.layoutResult.tvResult.text = "You cancelled"
                }
                setPositiveButton("Ok") { dialog, which ->
                    binding.layoutResult.tvResult.text = checkedList.toString()
                }
                setMultiChoiceItems(Constants.professionList.toTypedArray(), /* defaultCheckedItems */ null) { dialog, which, checked ->
                    if (checked) checkedList.add(Constants.professionList[which])
                    else checkedList.remove(Constants.professionList[which])
                }
                show()
            }
        }

        binding.layoutDialogs.apply {
            btnShowAlertDialog.setOnClickListener {
                showThemedAlertDialog(theme = R.style.ThemeOverlay_MaterialComponents_Dialog, showIcon = false, roundCorners = false)
            }
            btnShowThemedAlertDialog.setOnClickListener {
                showThemedAlertDialog(theme = R.style.Theme_MaterialComponents_DayNight_Dialog_Alert, showIcon = false, roundCorners = false)
            }
            btnShowFixedSizeAlertDialog.setOnClickListener {
                showThemedAlertDialog(theme = R.style.Theme_MaterialComponents_DayNight_Dialog_FixedSize)
            }
        }

        binding.layoutDialogs.apply {
            btnShowSimpleDialog.setOnClickListener {
                showThemedSimpleDialog(theme = R.style.ThemeOverlay_MaterialComponents_Dialog)
            }
            btnShowSimpleThemedDialog.setOnClickListener {
                showThemedSimpleDialog(theme = R.style.Theme_MaterialComponents_DayNight_Dialog)
            }
        }

        binding.layoutDialogs.apply {
            btnShowConfirmationSingleChoiceDialog.setOnClickListener {
                showThemedConfirmationSingleChoiceDialog(theme = R.style.ThemeOverlay_MaterialComponents_Dialog)
            }
            btnShowConfirmationSingleChoiceThemedDialog.setOnClickListener {
                showThemedConfirmationSingleChoiceDialog(theme = R.style.Theme_MaterialComponents_DayNight_Dialog)
            }
            btnShowConfirmationMultipleChoiceDialog.setOnClickListener {
                showThemedConfirmationMultipleChoiceDialog(theme = R.style.ThemeOverlay_MaterialComponents_Dialog)
            }
            btnShowConfirmationMultipleChoiceThemedDialog.setOnClickListener {
                showThemedConfirmationMultipleChoiceDialog(theme = R.style.Theme_MaterialComponents_DayNight_Dialog)
            }
        }

        binding.layoutDialogs.btnShowFullscreenDialog.setOnClickListener {
            val fragment = FullScreenDialogFragment(listener = { person: Person ->
                binding.layoutResult.tvResult.text = """
                    Name: ${person.name}
                    DOB: ${person.dob}
                    Profession: ${person.profession}
                """.trimIndent()
            })
            myActivity.supportFragmentManager.beginTransaction().apply {
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                // To make it fullscreen, use the 'content' root view as the container for the fragment, which is always the root view for the activity
                add(android.R.id.content, fragment).addToBackStack(null).commit()
            }
        }
    }

    private fun setUpDividers() {
        // Full-bleed dividers
        // Inset dividers
        // Middle dividers
        // Subheader dividers

        binding.apply {
            toolbar.title = DIVIDERS.title
            layoutDividers.root.visible()
            layoutResult.tvResult.text = "Dividers missing in Material library"
        }
    }

    private fun setUpImageLists() {
        binding.apply {
            toolbar.title = IMAGE_LISTS.title
            layoutImageLists.root.visible()
            layoutResult.tvResult.text = "Image Lists missing in Material library"
        }
    }

    private fun setUpLists() {
        binding.apply {
            toolbar.title = LISTS.title
            layoutLists.root.visible()
            layoutResult.tvResult.text = "Lists missing in Material library"
        }
    }

    private fun setUpMenus() {
        // Drop Down menus
        // Text and Icon list
        // Text, Icon and Keyboard command list
        // Text with selection state list
        // Disabled Actions
        // Scrollable menus
        // Enteranace animation and no animation
        // Cascading Menus
        // Contextual menu - actions are enabled or disabled based on condtions
        // Dividers
        // Menus on lists
        // Editable Drop down menu
        // Filled Drop down menu
        // Cut shaped menu
        // Rounded shaped menu
        // menu with custom font

        binding.apply {
            toolbar.title = MENUS.title
            layoutMenus.root.visible()
        }

        // Register context menu for TextView
        registerForContextMenu(binding.layoutMenus.tvContextMenuText)

        val professionAdapter = ArrayAdapter(myContext, android.R.layout.simple_list_item_1, Constants.professionList)
        (binding.layoutMenus.etFilledExposedDropDownMenu.editText as? AutoCompleteTextView)?.setAdapter(professionAdapter)
        (binding.layoutMenus.etOutlinedExposedDropDownMenu.editText as? AutoCompleteTextView)?.setAdapter(professionAdapter)

        fun onMenuItemClick(item: MenuItem): Boolean {
            binding.layoutResult.tvResult.text = "Popup Menu Item \"${item.title}\" got selected!"
            return true
        }

        fun showPopupMenu(view: View, @MenuRes menuRes: Int) {
            PopupMenu(myContext, view).apply {
                this.menu.invokeSetMenuIconMethod()
                menuInflater.inflate(menuRes, this.menu)
                setOnMenuItemClickListener { menuItem: MenuItem ->
                    when (menuItem.itemId) {
                        R.id.item_preview -> onMenuItemClick(item = menuItem)
                        R.id.item_share -> onMenuItemClick(item = menuItem)
                        R.id.item_get_link -> onMenuItemClick(item = menuItem)
                        else -> false
                    }
                }
                setOnDismissListener { it: PopupMenu? ->
                    showSnackBar(view = binding.coordinatorLayoutRoot, message = R.string.custom_popup_dismiss, duration = Snackbar.LENGTH_SHORT)
                }
                setMarginBtwMenuIconAndText(context = myContext, menu = this.menu, iconMarginDp = 16)
                this.menu.forEach { it: MenuItem ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        it.iconTintList = ContextCompat.getColorStateList(myContext, R.color.purple_500)
                    }
                }
                show()
            }
        }

        fun showListPopupMenu(view: View, adapter: ArrayAdapter<String>) {
            ListPopupWindow(myContext, null, R.attr.listPopupWindowStyle).apply {
                anchorView = view
                setAdapter(adapter)
                setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
                    // Respond to list popup window item click.
                    binding.layoutResult.tvResult.text = "${(parent?.get(position) as TextView).text} Selected"
                    this.dismiss()
                }
                show()
            }
        }

        binding.layoutMenus.btnShowPopupMenu.setOnClickListener { it: View? ->
            showPopupMenu(view = it ?: return@setOnClickListener, menuRes = R.menu.popup_menu)
        }

        binding.layoutMenus.btnShowListPopupMenu.setOnClickListener { it: View? ->
            val adapter = ArrayAdapter(myContext, android.R.layout.simple_list_item_1, Constants.hobbyList)
            showListPopupMenu(view = it ?: return@setOnClickListener, adapter = adapter)
        }
    }

    private fun setUpNavigationDrawer() {
        // Modal Drawer - left to right
        // Bottom Drawer - bottom to top
        // Bottom Drawer full screen with close - bottom to top
        // Dividers
        // Header
        // icon & text

        binding.apply {
            toolbar.title = NAVIGATION_DRAWER.title
            bottomAppBar.title = "Bottom Navigation Drawer" // Not working
            bottomAppBar.setTitleTextColor(ContextCompat.getColor(myContext, R.color.white))    // Not working
        }

        binding.apply {
            layoutNavigationDrawers.root.visible()
            navigationDrawerModal.visible()
            bottomAppBar.visible()
            navigationDrawerBottom.visible()
            flScrim.gone()
        }

        fun onMenuItemClick(menuItem: MenuItem, isChecked: Boolean = true): Boolean {
            binding.layoutResult.tvResult.text = "${menuItem.title} Selected"
            menuItem.isChecked = isChecked
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return true
        }

        fun showHideBottomNavigationDrawer(bottomSheetBehavior: BottomSheetBehavior<NavigationView>) {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                binding.flScrim.gone()
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                binding.flScrim.visible()
            }
        }

        // If you want the nav drawer to be open at all times
        // binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN)

        // If you want to change the scrim color
        // binding.drawerLayout.setScrimColor(ContextCompat.getColor(myContext, android.R.color.transparent))

        // Without toolbar Nav Drawer wont work.
        val toggle = ActionBarDrawerToggle(
            myActivity,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ).apply {
            syncState()
            isDrawerIndicatorEnabled = true
            setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
        }
        binding.drawerLayout.addDrawerListener(toggle)

        binding.navigationDrawerModal.setNavigationItemSelectedListener { it: MenuItem ->
            // Handle menu item selected
            when (it.itemId) {
                R.id.item_recent -> onMenuItemClick(menuItem = it)
                R.id.item_photos -> onMenuItemClick(menuItem = it)
                R.id.item_videos -> onMenuItemClick(menuItem = it)
                R.id.item_selfies -> onMenuItemClick(menuItem = it)
                R.id.item_favorites -> onMenuItemClick(menuItem = it, isChecked = false)
                R.id.item_screenshots -> onMenuItemClick(menuItem = it, isChecked = false)
                R.id.item_downloads -> onMenuItemClick(menuItem = it, isChecked = false)
                else -> false
            }
        }

        binding.layoutNavigationDrawers.btnShowModalNavigationDrawer.setOnClickListener {
            // GravityCompat.START is the location of the nav drawer. It has nothing to do with motion or direction
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        // Bottom Navigation Drawer --------------------------------------------------------------------------
        // This is so painful and confusing to use.
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.navigationDrawerBottom).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.navigationDrawerBottom.setNavigationItemSelectedListener { menuItem ->
            // Handle menu item selected
            onMenuItemClick(menuItem)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            binding.flScrim.gone()
            true
        }

        binding.flScrim.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            binding.flScrim.gone()
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                val baseColor = Color.BLACK
//                // 60% opacity
//                val baseAlpha = ResourcesCompat.getFloat(resources, R.dimen.material_emphasis_medium)
//                // Map slideOffset from [-1.0, 1.0] to [0.0, 1.0]
//                val offset = (slideOffset - (-1f)) / (1f - (-1f)) * (1f - 0f) + 0f
//                val alpha = MathUtils.clamp(0f, 255f, offset * baseAlpha).toInt()
//                val color = Color.argb(alpha, baseColor.red, baseColor.green, baseColor.blue)
//                binding.flScrim.setBackgroundColor(color)
                binding.flScrim.setBackgroundColor(ContextCompat.getColor(myContext, R.color.sixty_percent_transparent_black))
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    binding.flScrim.gone()
                }
            }
        })

        binding.bottomAppBar.setNavigationOnClickListener {
            showHideBottomNavigationDrawer(bottomSheetBehavior)
        }

        binding.layoutNavigationDrawers.btnShowBottomNavigationDrawer.setOnClickListener {
            showHideBottomNavigationDrawer(bottomSheetBehavior)
        }

        // On Fragment Backpress --------------------------------------------------------------------------
        myActivity.onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    myActivity.supportFragmentManager.popBackStackImmediate()
                }
            }
        })
    }

    private fun setUpNavigationRail() {
        // 72dp with text
        // 56dp without text
        // always with text, selected text, no text
        // Alignment - top, center, bottom
        // Divider & Elevated
        // Rail + Nav Drawer at top

        binding.apply {
            toolbar.title = NAVIGATION_RAIL.title
            layoutNavigationRail.root.visible()
        }

        fun loadFragment(fragment: Fragment?) {
            fragment ?: return
            myActivity.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
        }

        // Default Fragment for Nav Rail
        loadFragment(DummyFragment(route = Route.NAV_RAIL_RECENT))

        binding.layoutNavigationRail.navigationRail.getOrCreateBadge(R.id.item_photos).apply {
            isVisible = true
            number = 99   // An icon only badge will be displayed unless a number is set:
        }

        // Set default selection on Nav Rail
        binding.layoutNavigationRail.navigationRail.selectedItemId = R.id.item_recent

        // FAB icon color not changing
        val fabDrawable = ContextCompat.getDrawable(myContext, R.drawable.ic_baseline_camera_alt_24)?.changeColor(context = myContext, color = R.color.teal_900)
        binding.layoutNavigationRail.navigationRail.headerView?.findViewById<FloatingActionButton>(R.id.floating_action_button).apply {
            this?.setOnClickListener {
                binding.layoutResult.tvResult.text = "Fab Header Camera Tapped"
            }
            this?.colorFilter = null
            this?.setImageDrawable(fabDrawable)
        }

        binding.layoutNavigationRail.navigationRail.setOnItemSelectedListener { it: MenuItem ->
            when (it.itemId) {
                R.id.item_recent -> {
                    binding.layoutResult.tvResult.text = "${it.title} Selected"
                    loadFragment(DummyFragment(route = Route.NAV_RAIL_RECENT))
                    true
                }
                R.id.item_photos -> {
                    binding.layoutNavigationRail.navigationRail.removeBadge(R.id.item_photos)
                    binding.layoutResult.tvResult.text = "${it.title} Selected"
                    loadFragment(DummyFragment(route = Route.NAV_RAIL_PHOTOS))
                    true
                }
                R.id.item_videos -> {
                    binding.layoutResult.tvResult.text = "${it.title} Selected"
                    loadFragment(DummyFragment(route = Route.NAV_RAIL_VIDEOS))
                    true
                }
                R.id.item_selfies -> {
                    binding.layoutResult.tvResult.text = "${it.title} Selected"
                    loadFragment(DummyFragment(route = Route.NAV_RAIL_SELFIES))
                    true
                }
                else -> false
            }
        }

        binding.layoutNavigationRail.navigationRail.setOnItemReselectedListener { it: MenuItem ->
            when (it.itemId) {
                R.id.item_recent -> {
                    binding.layoutResult.tvResult.text = "${it.title} Reselected"
                    loadFragment(DummyFragment(route = Route.NAV_RAIL_RECENT))
                }
                R.id.item_photos -> {
                    binding.layoutResult.tvResult.text = "${it.title} Reselected"
                    loadFragment(DummyFragment(route = Route.NAV_RAIL_PHOTOS))
                }
                R.id.item_videos -> {
                    binding.layoutResult.tvResult.text = "${it.title} Reselected"
                    loadFragment(DummyFragment(route = Route.NAV_RAIL_VIDEOS))
                }
                R.id.item_selfies -> {
                    binding.layoutResult.tvResult.text = "${it.title} Reselected"
                    loadFragment(DummyFragment(route = Route.NAV_RAIL_SELFIES))
                }
            }
        }
    }

    private fun setUpProgressIndicators() {
        // Linear - Determinate, Indeterminate, Rounded Corners, Varied Thickness, Contiguous, Disjoint, Varied Colors, Multiple Colors, Right to left,
        // Circular - Determinate, Indeterminate, Rounded Corners, Varied Thickness, Contiguous, Disjoint, Varied Colors, Multiple Colors, Right to left,
        // Progress on cards, center of screen, attached to toolbar if data already exists in the center
        // Circular progress inside button

        binding.apply {
            toolbar.title = PROGRESS_INDICATORS.title
            layoutProgressIndicators.root.visible()
        }
        var countDownTimer: CountDownTimer? = null

        /** To switch between indeterminate to determinate and vice-versa. This was painful:
         *
         * HIDE PROGRESS
         * SWITCH PROGRESS TYPE
         * SHOW PROGRESS
         *
         **/
        fun hideLinearIndicators() {
            binding.layoutProgressIndicators.linearProgressIndicator.apply {
                hideAnimationBehavior = BaseProgressIndicator.HIDE_INWARD
                gone()
            }
        }

        fun hideCircularIndicators() {
            binding.layoutProgressIndicators.circularProgressIndicator.apply {
                hideAnimationBehavior = BaseProgressIndicator.HIDE_INWARD
                gone()
            }
        }

        fun showLinearIndicators() {
            binding.layoutProgressIndicators.linearProgressIndicator.apply {
                showAnimationBehavior = BaseProgressIndicator.SHOW_OUTWARD
                visible()
            }
        }

        fun showCircularIndicators() {
            binding.layoutProgressIndicators.circularProgressIndicator.apply {
                showAnimationBehavior = BaseProgressIndicator.SHOW_OUTWARD
                visible()
            }
        }

        // Switching from determinate to indeterminate is crashing
        binding.layoutProgressIndicators.btnShowIndeterminateProgress.setOnClickListener {
            if (null != countDownTimer) countDownTimer?.cancel()
            binding.layoutResult.tvResult.text = "Indeterminate Progress Indicator"

            hideLinearIndicators()
            hideCircularIndicators()

            binding.layoutProgressIndicators.linearProgressIndicator.apply {
                postInvalidate()
                trackThickness = 24
                isIndeterminate = true
                indicatorDirection = LinearProgressIndicator.INDICATOR_DIRECTION_START_TO_END
                trackColor = Color.BLACK
                setIndicatorColor(Color.CYAN, Color.RED, Color.GREEN)
                trackCornerRadius = 0   // Rounded corners not supported in CONTIGUOUS type
                indeterminateAnimationType = LinearProgressIndicator.INDETERMINATE_ANIMATION_TYPE_CONTIGUOUS
            }

            binding.layoutProgressIndicators.circularProgressIndicator.apply {
                postInvalidate()
                trackThickness = 24
                isIndeterminate = true
                indicatorSize = 120
                indicatorInset = 8
                indicatorDirection = CircularProgressIndicator.INDICATOR_DIRECTION_CLOCKWISE
                trackColor = Color.BLACK
                setIndicatorColor(Color.CYAN, Color.RED, Color.GREEN)
            }

            showLinearIndicators()
            showCircularIndicators()
        }

        binding.layoutProgressIndicators.btnShowDeterminateProgress.setOnClickListener {
            if (null != countDownTimer) countDownTimer?.cancel()

            hideLinearIndicators()
            hideCircularIndicators()

            countDownTimer = object : CountDownTimer(MAX_DURATION_IN_MILLIS, REPEAT_DURATION_IN_MILLIS) {
                override fun onTick(millisUntilFinished: Long) {
                    // This method is called every "REPEAT_DURATION_IN_MILLIS" duration. This method stops getting called after "MAX_DURATION_IN_MILLIS" is over.
                    val progress = ((MAX_DURATION_IN_MILLIS - millisUntilFinished) + 1000) / 1000
                    myActivity.runOnUiThread {
                        binding.layoutResult.tvResult.text = "$progress% finished"
                        binding.layoutProgressIndicators.linearProgressIndicator.apply {
                            setProgressCompat(progress.toInt(), true)
                            indeterminateAnimationType = LinearProgressIndicator.INDETERMINATE_ANIMATION_TYPE_DISJOINT
                            indicatorDirection = LinearProgressIndicator.INDICATOR_DIRECTION_START_TO_END
                            trackCornerRadius = 16
                            isIndeterminate = false
                        }
                        binding.layoutProgressIndicators.circularProgressIndicator.apply {
                            setProgressCompat(progress.toInt(), true)
                            trackCornerRadius = 16
                            isIndeterminate = false
                        }
                    }

                    showLinearIndicators()
                    showCircularIndicators()
                }

                override fun onFinish() {
                    // This method is called after "MAX_DURATION_IN_MILLIS" duration is complete
                    hideLinearIndicators()
                    hideCircularIndicators()
                    showSnackBar(view = binding.coordinatorLayoutRoot, message = R.string.finished_downloading, duration = Snackbar.LENGTH_SHORT)
                }
            }.start()
        }

        /** On Physical Back Button Pressed **/
        binding.root.apply {
            isFocusableInTouchMode = true
            requestFocus()
            setOnKeyListener { v: View?, keyCode: Int, event: KeyEvent? ->
                if (null != countDownTimer) countDownTimer?.cancel()
                myActivity.onBackPressed()
                keyCode == KeyEvent.KEYCODE_BACK && event?.action == KeyEvent.ACTION_DOWN
            }
        }
    }

    private fun setUpRadioButtons() {
        binding.apply {
            toolbar.title = RADIO_BUTTONS.title
            layoutRadioButtons.root.visible()
        }

        binding.layoutRadioButtons.radioGroup.children.forEach { it: View ->
            val radioButton = if (it is RadioButton) it as RadioButton else return@forEach
            if (radioButton.isChecked) {
                binding.layoutResult.tvResult.text = radioButton.text.toString().plus(" Selected")
                return@forEach
            }
        }

        binding.layoutRadioButtons.apply {
            radioGroup.setOnCheckedChangeListener { group, checkedId ->
                // Responds to child RadioButton checked/unchecked
                binding.layoutResult.tvResult.text = when (group.checkedRadioButtonId) {
                    rb1.id -> "Radio Button 1 Selected"
                    rb2.id -> "Radio Button 2 Selected"
                    rb3.id -> "Radio Button 3 Selected"
                    rbCustom.id -> "Custom Radio Button Selected"
                    rbDisabled.id -> "Radio Button 5 Selected"
                    else -> "Nothing got selected"
                }
            }
        }

        // Order matters. if you put setTextAppearance after color n size, it overrides all those.
        binding.layoutRadioButtons.rbCustom.apply {
            setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    showSnackBar(view = binding.coordinatorLayoutRoot, message = R.string.custom_radio_message, duration = Snackbar.LENGTH_SHORT)
                    setTextColor(ContextCompat.getColorStateList(myContext, R.color.purple_500))
                    buttonTintList = ContextCompat.getColorStateList(myContext, R.color.purple_500)
                    buttonDrawable = ContextCompat.getDrawable(myContext, R.drawable.ic_baseline_sentiment_very_satisfied_24)
                    setChecked(true)
                } else {
                    setTextColor(Color.DKGRAY)
                    buttonTintList = ContextCompat.getColorStateList(myContext, R.color.title_color)
                    buttonDrawable = ContextCompat.getDrawable(myContext, R.drawable.ic_baseline_sentiment_very_dissatisfied_24)
                    setChecked(false)
                }
            }
            text = "Custom Radio Button"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Widget_ActionMode_Title)
            minWidth = 26
            minHeight = 26
        }

        binding.layoutRadioButtons.btnClearRadioBtnSelection.setOnClickListener {
            for (i in 0 until binding.layoutRadioButtons.radioGroup.size) {
                val view = binding.layoutRadioButtons.radioGroup[i]
                val radioButton = if (view is RadioButton) view as RadioButton else break
                if (radioButton.isChecked) {
                    radioButton.isChecked = false
                    binding.layoutResult.tvResult.text = "Cleared \"${radioButton.text}\" Selection"
                    break
                }
            }
        }

        // Dynamic Radio Buttons -------------------------------------------------------------------------
        // For some reason if you declare this first the main radio group is not responding
        val radioButtonParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
            topMargin = 16
            bottomMargin = 16
        }
        val radioButton1 = RadioButton(myContext).apply {
            text = "Dynamic Radio Button 1"
            layoutParams = radioButtonParams
        }
        val radioButton2 = RadioButton(myContext).apply {
            text = "Dynamic Radio Button 2"
            layoutParams = radioButtonParams
        }
        val radioGroupParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
            leftMargin = 0
            topMargin = 0
            bottomMargin = 0
        }
        val radioGroup = RadioGroup(myContext).apply {
            addView(radioButton1)
            addView(radioButton2)
            orientation = RadioGroup.VERTICAL
            layoutParams = radioGroupParams
            setPadding(0, 16, 0, 16)
        }
        binding.layoutRadioButtons.llDynamicRadioButtons.addView(radioGroup)
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            binding.layoutResult.tvResult.text = when (group.checkedRadioButtonId) {
                radioButton1.id -> "Dynamic Radio Button 1 Selected"
                radioButton2.id -> "Dynamic Radio Button 2 Selected"
                else -> "Nothing got selected"
            }
        }
    }

    private fun setUpSheetsBottom() {
        // Standard - Like music player strip
        // Modal - List Dialog replacement
        // Persistent - G Maps, shopping kart, unread messages

        binding.apply {
            toolbar.title = SHEETS_BOTTOM.title
            layoutSheetsBottom.root.visible()
            layoutPersistentBottomSheet.root.visible()
        }

        for (i in 1..20) {
            val itemBinding = ItemBottomSheetBinding.inflate(LayoutInflater.from(myContext), binding.layoutPersistentBottomSheet.llSongs, false)
            itemBinding.root.text = "My Song $i"
            binding.layoutPersistentBottomSheet.llSongs.addView(itemBinding.root)
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.layoutPersistentBottomSheet.root).apply {
            addBottomSheetCallback(object : BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> binding.layoutResult.tvResult.text = "STATE HIDDEN"
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            binding.layoutResult.tvResult.text = "STATE EXPANDED"
                            binding.layoutSheetsBottom.btnShowPersistentBottomSheet.text = "Hide Persistent Bottom Sheet"
                            binding.layoutPersistentBottomSheet.llBottomSheetHeader.findViewById<TextView>(R.id.tv_song).showHideIcon(
                                context = myContext,
                                showTick = true,
                                icon1 = R.drawable.ic_baseline_play_arrow_24,
                                icon3 = R.drawable.ic_baseline_keyboard_arrow_down_24,
                                iconColor1 = R.color.purple_500,
                                iconColor3 = R.color.purple_500,
                                direction = 5
                            )
                        }
                        BottomSheetBehavior.STATE_HALF_EXPANDED -> binding.layoutResult.tvResult.text = "STATE HALF EXPANDED"
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            binding.layoutResult.tvResult.text = "STATE COLLAPSED"
                            binding.layoutSheetsBottom.btnShowPersistentBottomSheet.text = "Show Persistent Bottom Sheet"
                            binding.layoutPersistentBottomSheet.llBottomSheetHeader.findViewById<TextView>(R.id.tv_song).showHideIcon(
                                context = myContext,
                                showTick = true,
                                icon1 = R.drawable.ic_baseline_pause_24,
                                icon3 = R.drawable.ic_baseline_keyboard_arrow_up_24,
                                iconColor1 = R.color.purple_500,
                                iconColor3 = R.color.purple_500,
                                direction = 5
                            )
                        }
                        BottomSheetBehavior.STATE_DRAGGING -> binding.layoutResult.tvResult.text = "STATE DRAGGING"
                        BottomSheetBehavior.STATE_SETTLING -> binding.layoutResult.tvResult.text = "STATE SETTLING"
                        else -> binding.layoutResult.tvResult.text = "Persistent Bottom Sheet State"
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
            })
        }

        binding.layoutSheetsBottom.btnShowPersistentBottomSheet.setOnClickListener {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        binding.layoutSheetsBottom.btnShowModalBottomSheet.setOnClickListener {
            ModalBottomSheetDialogFragment(onBottomSheetItemClickListener = { it: String ->
                binding.layoutResult.tvResult.text = it.plus(" Selected")
            }).show(myActivity.supportFragmentManager, TAG_MODAL_BOTTOM_SHEET)
        }

//        bottomSheetBehavior.saveFlags
//        bottomSheetBehavior.isGestureInsetBottomIgnored
//        bottomSheetBehavior.expandedOffset
//        bottomSheetBehavior.halfExpandedRatio
//        bottomSheetBehavior.isDraggable
//        bottomSheetBehavior.isHideable
//        bottomSheetBehavior.isFitToContents
//        bottomSheetBehavior.peekHeight
//        bottomSheetBehavior.skipCollapsed
//        bottomSheetBehavior.state
//        bottomSheetBehavior.disableShapeAnimations()
//        bottomSheetBehavior.removeBottomSheetCallback()
//        bottomSheetBehavior.setPeekHeight()
//        bottomSheetBehavior.setUpdateImportantForAccessibilityOnSiblings()
    }

    private fun setUpSheetsSide() {
        binding.apply {
            toolbar.title = SHEETS_SIDE.title
            layoutSheetsSide.root.visible()
            layoutResult.tvResult.text = "Side Sheets missing in Material library"
        }
    }

    private fun setUpSliders() {
        binding.apply {
            toolbar.title = SLIDERS.title
            layoutSliders.root.visible()
        }
        binding.layoutSliders.sliderContinuous.apply {
            valueFrom = 0.0F
            valueTo = 100.0F
            value = 10.0F   // starting value
            setLabelFormatter { value: Float -> "$value <<((" }
            addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: Slider) = Unit // Responds to when slider's touch event is being started
                override fun onStopTrackingTouch(slider: Slider) = Unit // Responds to when slider's touch event is being stopped
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
                override fun onStartTrackingTouch(slider: RangeSlider) = Unit // Responds to when slider's touch event is being started
                override fun onStopTrackingTouch(slider: RangeSlider) = Unit // Responds to when slider's touch event is being stopped
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
        // Setting coordinator layout as root view allows user to dismiss snackbar by swiping to the right
        binding.apply {
            toolbar.title = SNACKBARS.title
            layoutSnackbars.root.visible()
        }
        binding.layoutSnackbars.apply {
            btnSnackBarSimple.setOnClickListener { Snackbar.make(binding.coordinatorLayoutRoot, "You clicked me!", Snackbar.LENGTH_SHORT).show() }
            btnSnackBarAction.setOnClickListener {
                Snackbar.make(binding.coordinatorLayoutRoot, "Email deleted!", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") { binding.layoutResult.tvResult.text = "You undid email deletion!" }
                    .show()
            }
            btnSnackBarWithLongText.setOnClickListener {
                Snackbar.make(
                    binding.coordinatorLayoutRoot,
                    "Email must contain only @. as special characters. Otherwise hackers can bust your email. You will then cry like a baby. So be careful!",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("DON'T TELL ME AGAIN") { binding.layoutResult.tvResult.text = "\"DON'T TELL ME AGAIN\" Clicked!" }.show()
            }
            btnSnackBarCustom.setOnClickListener {
                Snackbar.make(binding.coordinatorLayoutRoot, "Email must contain only @. as special characters!", Snackbar.LENGTH_INDEFINITE)
                    .setDuration(10_000)    // Even if its indefinite sncakbar disappears after 10 sec
                    .setAction("OK") { binding.layoutResult.tvResult.text = "You got it!" }
                    .setBackgroundTint(ContextCompat.getColor(myContext, R.color.teal_100))
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .setTextColor(ContextCompat.getColor(myContext, R.color.teal_700))
                    .setActionTextColor(ContextCompat.getColor(myContext, R.color.teal_900))
                    .show()
            }
            btnSnackBarCustomPosition.setOnClickListener {
                Snackbar.make(binding.coordinatorLayoutRoot, "I am at a different place than usual. How strange!", Snackbar.LENGTH_LONG)
                    .setAnchorView(btnSnackBarSimple)
                    .show()
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun setUpSwitches() {
        binding.apply {
            toolbar.title = SWITCHES.title
            layoutSwitches.root.visible()
        }
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
                    thumbDrawable = ContextCompat.getDrawable(myContext, R.drawable.ic_baseline_stars_24)
                    thumbTintList = ContextCompat.getColorStateList(myContext, R.color.purple_700)
                    trackDrawable = ContextCompat.getDrawable(myContext, R.drawable.ic_baseline_dehaze_24)
                    trackTintList = ContextCompat.getColorStateList(myContext, R.color.teal_200)
                    text = "Custom Switch is On!"
                    setTextColor(ContextCompat.getColorStateList(myContext, R.color.purple_700))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) setTextAppearance(android.R.attr.textAppearanceLarge)
                } else {
                    thumbDrawable = ContextCompat.getDrawable(myContext, R.drawable.ic_baseline_stars_24)
                    thumbTintList = ContextCompat.getColorStateList(myContext, android.R.color.darker_gray)
                    trackDrawable = ContextCompat.getDrawable(myContext, R.drawable.ic_baseline_dehaze_24)
                    trackTintList = ContextCompat.getColorStateList(myContext, android.R.color.darker_gray)
                    text = "Custom Switch is Off!"
                    setTextColor(ContextCompat.getColorStateList(myContext, android.R.color.darker_gray))
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
        // Add new tab dynamically

        binding.apply {
            toolbar.title = TABS.title
            layoutTabs.root.visible()
        }

        val demoCollectionAdapter = DemoCollectionAdapter(this)
        binding.layoutTabs.viewPager.adapter = demoCollectionAdapter

        binding.layoutTabs.tabLayout1.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (null != tab?.badge) tab.removeBadge()
                binding.layoutResult.tvResult.text = "${tab?.text.toString().capFirstChar()} Selected"
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                binding.layoutResult.tvResult.text = "${tab?.text.toString().capFirstChar()} Reselected"
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                binding.layoutResult.tvResult.text = "${tab?.text.toString().capFirstChar()} Unselected"
            }
        })

        TabLayoutMediator(binding.layoutTabs.tabLayout1, binding.layoutTabs.viewPager) { tab, position ->
            for (i in 1..10) {
                tab.text = "Inbox ${position + 1}"
                tab.icon = ContextCompat.getDrawable(myContext, R.drawable.ic_baseline_email_24)
                if (position == 2 || position == 7) {
                    // Get badge from tab (or create one if none exists)
                    tab.orCreateBadge.apply {
                        number = SecureRandom().nextInt(1000)
                        setContentDescriptionNumberless("contentDescription")
                        setContentDescriptionQuantityStringsResource(R.plurals.tab_layout_1)
                        setContentDescriptionExceedsMaxBadgeNumberStringResource(R.string.app_name)
                    }
                }
            }
        }.attach()

        // Default tab selected
        val tabCount = binding.layoutTabs.tabLayout1.tabCount
        binding.layoutTabs.tabLayout1.getTabAt(if (tabCount > 3) 3 else 0)?.select()
        binding.layoutTabs.viewPager.currentItem = 3

        // How to add fragment?
        binding.layoutTabs.btnAddTab.setOnClickListener {
            val tab = binding.layoutTabs.tabLayout1.newTab().apply {
                text = "Inbox ${binding.layoutTabs.tabLayout1.tabCount + 1}"
                icon = ContextCompat.getDrawable(myContext, R.drawable.ic_baseline_email_24)
            }
            binding.layoutTabs.tabLayout1.apply {
                addTab(tab, binding.layoutTabs.tabLayout1.tabCount, true)
                getTabAt(binding.layoutTabs.tabLayout1.tabCount - 1)?.select()
            }
//            (binding.layoutTabs.viewPager.adapter as DemoCollectionAdapter).apply {
//                createFragment(binding.layoutTabs.tabLayout1.tabCount)
//                notifyItemInserted(binding.layoutTabs.tabLayout1.tabCount)
//            }
        }

        binding.layoutTabs.btnRemoveTab.setOnClickListener {
            if (binding.layoutTabs.tabLayout1.tabCount < 2) return@setOnClickListener

            binding.layoutTabs.tabLayout1.apply {
                removeTabAt(binding.layoutTabs.tabLayout1.tabCount - 1)
                getTabAt(binding.layoutTabs.tabLayout1.tabCount - 1)?.select()
            }
        }

        binding.layoutTabs.btnTabMode.setOnClickListener {
            if (binding.layoutTabs.tabLayout1.tabMode == TabLayout.MODE_SCROLLABLE) {
                binding.layoutTabs.tabLayout1.tabMode = TabLayout.MODE_FIXED
                binding.layoutResult.tvResult.text = "Tab Mode Fixed"
            } else {
                binding.layoutTabs.tabLayout1.tabMode = TabLayout.MODE_SCROLLABLE
                binding.layoutResult.tvResult.text = "Tab Mode Scrollable"
            }
        }

        binding.layoutTabs.btnInlineLabel.setOnClickListener {
            binding.layoutTabs.tabLayout1.isInlineLabel = !binding.layoutTabs.tabLayout1.isInlineLabel
        }

        binding.layoutTabs.btnShowHideIcon.setOnClickListener {
            if (binding.layoutTabs.tabLayout1.getTabAt(0)?.icon == null) {
                repeat(binding.layoutTabs.tabLayout1.tabCount) { position: Int ->
                    binding.layoutTabs.tabLayout1.getTabAt(position)?.icon = ContextCompat.getDrawable(myContext, R.drawable.ic_baseline_email_24)
                }
            } else {
                repeat(binding.layoutTabs.tabLayout1.tabCount) { position: Int ->
                    binding.layoutTabs.tabLayout1.getTabAt(position)?.icon = null
                }
            }
        }
    }

    private fun setUpTextFields() {
        binding.apply {
            toolbar.title = TEXT_FIELDS.title
            layoutTextFields.root.visible()
        }
        val professionAdapter = ArrayAdapter(myContext, android.R.layout.simple_list_item_1, Constants.professionList)
        val hobbyAdapter = ArrayAdapter(myContext, R.layout.list_item_custom_array_adapter, Constants.hobbyList)
        (binding.layoutTextFields.etProfession.editText as? AutoCompleteTextView)?.setAdapter(professionAdapter)
        (binding.layoutTextFields.etHobby.editText as? AutoCompleteTextView)?.setAdapter(hobbyAdapter)
        binding.layoutTextFields.etName.editText?.addTextChangedListener(afterTextChanged = { it: Editable? ->
            if (it?.length ?: 0 < 3) binding.layoutTextFields.etName.error = "Name must contain at least 3 characters!"
            else binding.layoutTextFields.etName.error = null
        })
        binding.layoutTextFields.etEmail.apply {
            setEndIconOnClickListener {
                Snackbar.make(binding.coordinatorLayoutRoot, "Email must contain only @. as special characters!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("GOT IT") { }
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

    private fun setUpTooltips() {
        binding.apply {
            toolbar.title = TOOLTIPS.title
            layoutTooltips.root.visible()
            layoutResult.tvResult.text = "Tooltips missing in Material library"
        }
    }

    private fun setUpTimePickers() {
        binding.apply {
            toolbar.title = TIME_PICKERS.title
            layoutTimePickers.root.visible()
        }
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
