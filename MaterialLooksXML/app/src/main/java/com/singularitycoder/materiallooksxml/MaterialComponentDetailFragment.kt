package com.singularitycoder.materiallooksxml

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.format.DateFormat.is24HourFormat
import android.view.*
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.children
import androidx.core.view.get
import androidx.core.view.size
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
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
import com.singularitycoder.materiallooksxml.sheetsbottom.ModalBottomSheetDialogFragment
import com.singularitycoder.materiallooksxml.tabs.DemoCollectionAdapter
import com.singularitycoder.materiallooksxml.tabs.DummyFragment
import java.text.NumberFormat
import java.util.*

class MaterialComponentDetailFragment(val component: MaterialComponent) : Fragment() {

    private lateinit var demoCollectionAdapter: DemoCollectionAdapter
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMaterialComponentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        setUpDefaults()
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
            layoutNavigationDrawers.root.visibility = View.GONE
            layoutNavigationRail.root.visibility = View.GONE
            layoutProgressIndicators.root.visibility = View.GONE
            layoutRadioButtons.root.visibility = View.GONE
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
            DIVIDERS.title -> setUpDividers()
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
        binding.toolbar.title = APP_BAR_BOTTOM.title
    }

    private fun setUpAppBarTop() {
        binding.toolbar.title = APP_BAR_TOP.title
    }

    private fun setUpBottomNavigation() {
        binding.toolbar.title = BOTTOM_NAVIGATION.title
    }

    private fun setUpButtons() {
        binding.toolbar.title = BUTTONS.title
    }

    private fun setUpFloatingActionButton() {
        binding.toolbar.title = FLOATING_ACTION_BUTTON.title
    }

    private fun setUpCards() {
        binding.toolbar.title = CARDS.title
    }

    private fun setUpCheckBoxes() {
        binding.toolbar.title = CHECK_BOXES.title
    }

    private fun setUpChips() {
        binding.toolbar.title = CHIPS.title
    }

    private fun setUpDatePickers() {
        binding.toolbar.title = DATE_PICKERS.title
    }

    private fun setUpDialogs() {
        binding.toolbar.title = DIALOGS.title
    }

    private fun setUpDividers() {
        binding.toolbar.title = DIVIDERS.title
    }

    private fun setUpMenus() {
        binding.toolbar.title = MENUS.title
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
            layoutNavigationDrawers.root.visibility = View.VISIBLE
            navigationDrawerModal.visibility = View.VISIBLE
            bottomAppBar.visibility = View.VISIBLE
            navigationDrawerBottom.visibility = View.VISIBLE
            flScrim.visibility = View.GONE
        }

        fun onMenuItemClick(menuItem: MenuItem, isChecked: Boolean = true): Boolean {
            binding.layoutResult.tvResult.text = "${menuItem.title} Selected"
            menuItem.isChecked = isChecked
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return true
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
            binding.layoutResult.tvResult.text = "${menuItem.title} Selected"
            menuItem.isChecked = true
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            binding.flScrim.visibility = View.GONE
            true
        }

        binding.flScrim.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            binding.flScrim.visibility = View.GONE
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
                    binding.flScrim.visibility = View.GONE
                }
            }
        })

        fun showHideBottomNavigationDrawer(bottomSheetBehavior: BottomSheetBehavior<NavigationView>) {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                binding.flScrim.visibility = View.GONE
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                binding.flScrim.visibility = View.VISIBLE
            }
        }

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

        binding.toolbar.title = NAVIGATION_RAIL.title
        binding.layoutNavigationRail.root.visibility = View.VISIBLE

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

        binding.toolbar.title = PROGRESS_INDICATORS.title
        binding.layoutProgressIndicators.root.visibility = View.VISIBLE
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
                visibility = View.GONE
            }
        }

        fun hideCircularIndicators() {
            binding.layoutProgressIndicators.circularProgressIndicator.apply {
                hideAnimationBehavior = BaseProgressIndicator.HIDE_INWARD
                visibility = View.GONE
            }
        }

        fun showLinearIndicators() {
            binding.layoutProgressIndicators.linearProgressIndicator.apply {
                showAnimationBehavior = BaseProgressIndicator.SHOW_OUTWARD
                visibility = View.VISIBLE
            }
        }

        fun showCircularIndicators() {
            binding.layoutProgressIndicators.circularProgressIndicator.apply {
                showAnimationBehavior = BaseProgressIndicator.SHOW_OUTWARD
                visibility = View.VISIBLE
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
                    showSnackBar(view = binding.root, message = R.string.finished_downloading, duration = Snackbar.LENGTH_SHORT)
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
        binding.toolbar.title = RADIO_BUTTONS.title
        binding.layoutRadioButtons.root.visibility = View.VISIBLE

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
                    showSnackBar(view = binding.layoutRadioButtons.root, message = R.string.custom_radio_message, duration = Snackbar.LENGTH_SHORT)
                    setTextColor(ContextCompat.getColorStateList(myContext, R.color.purple_500))
                    buttonTintList = ContextCompat.getColorStateList(myContext, R.color.purple_500)
                } else {
                    setTextColor(Color.DKGRAY)
                    buttonTintList = ContextCompat.getColorStateList(myContext, R.color.title_color)
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

        binding.toolbar.title = SHEETS_BOTTOM.title
        binding.layoutSheetsBottom.root.visibility = View.VISIBLE
        binding.layoutPersistentBottomSheet.root.visibility = View.VISIBLE

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

    private fun setUpSliders() {
        binding.toolbar.title = SLIDERS.title
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
        binding.toolbar.title = SNACKBARS.title
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
                    .setBackgroundTint(ContextCompat.getColor(myContext, R.color.teal_100))
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .setTextColor(ContextCompat.getColor(myContext, R.color.teal_700))
                    .setActionTextColor(ContextCompat.getColor(myContext, R.color.teal_900))
                    .show()
            }
            btnSnackBarCustomPosition.setOnClickListener {
                Snackbar.make(binding.root, "I am at a different place than usual. How strange!", Snackbar.LENGTH_LONG)
                    .setAnchorView(btnSnackBarSimple)
                    .show()
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun setUpSwitches() {
        binding.toolbar.title = SWITCHES.title
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

        binding.toolbar.title = TABS.title
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
            for (i in 1..10) {
                tab.text = "tab ${position + 1}"
                tab.icon = ContextCompat.getDrawable(myContext, R.drawable.ic_baseline_alternate_email_24)
            }
        }.attach()
    }

    private fun setUpTextFields() {
        binding.toolbar.title = TEXT_FIELDS.title
        binding.layoutTextFields.root.visibility = View.VISIBLE
        val professionAdapter = ArrayAdapter(myContext, android.R.layout.simple_list_item_1, Constants.professionArray)
        val hobbyAdapter = ArrayAdapter(myContext, R.layout.list_item_custom_array_adapter, Constants.hobbyArray)
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
                    .setBackgroundTint(ContextCompat.getColor(myContext, R.color.black))
                    .setTextColor(ContextCompat.getColor(myContext, R.color.white))
                    .setActionTextColor(ContextCompat.getColor(myContext, R.color.purple_200))
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
        binding.toolbar.title = TIME_PICKERS.title
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
