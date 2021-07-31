package com.singularitycoder.materiallooksxml

object Constants {
    const val REPEAT_DURATION_IN_MILLIS = 1_000L // 1 sec
    const val MAX_DURATION_IN_MILLIS = 100_000L // 100 sec
    const val TAG_MODAL_BOTTOM_SHEET = "MODAL_BOTTOM_SHEET"

    val professionArray = listOf("Android Developer", "Data Scientist", "Otaku", "Rocket Scientist", "Physicist", "Mathematician")
    val hobbyArray = listOf("Meditation", "Yoga", "Computer Programming", "Hacking", "3D Animation", "Story Telling", "Graphic Design", "Inventing")
    val materialComponentList = ArrayList<MaterialComponent>()

    init {
        MaterialComponents.values().forEach { it: MaterialComponents ->
            materialComponentList.add(MaterialComponent(image = it.image, title = it.title, subtitle = it.subTitle, link = it.link))
        }
    }
}

enum class Route {
    NONE,
    TAB,
    NAV_RAIL_RECENT,
    NAV_RAIL_PHOTOS,
    NAV_RAIL_VIDEOS,
    NAV_RAIL_SELFIES
}

enum class MaterialComponents(val image: Int, val title: String, val subTitle: String, val link: String) {
    APP_BAR_BOTTOM(
        image = R.drawable.app_bar_bottom,
        title = "App bars: bottom",
        subTitle = "A bottom app bar displays navigation and key actions at the bottom of mobile screens.",
        link = "https://material.io/components/app-bars-bottom/android"
    ),
    APP_BAR_TOP(
        image = R.drawable.app_bar_top,
        title = "App bars: top",
        subTitle = "The top app bar displays information and actions relating to the current screen.",
        link = "https://material.io/components/app-bars-top/android"
    ),
    BOTTOM_NAVIGATION(
        image = R.drawable.bottom_navigation,
        title = "Bottom navigation",
        subTitle = "Bottom navigation bars allow movement between primary destinations in an app.",
        link = "https://material.io/components/bottom-navigation/android"
    ),
    BUTTONS(
        image = R.drawable.buttons,
        title = "Buttons",
        subTitle = "Buttons allow users to take actions, and make choices, with a single tap.",
        link = "https://material.io/components/buttons/android"
    ),
    FLOATING_ACTION_BUTTON(
        image = R.drawable.buttons_floating_action_button,
        title = "Buttons: floating action button",
        subTitle = "A floating action button (FAB) represents the primary action of a screen.",
        link = "https://material.io/components/buttons-floating-action-button/android"
    ),
    CARDS(
        image = R.drawable.cards,
        title = "Cards",
        subTitle = "Cards contain content and actions about a single subject.",
        link = "https://material.io/components/cards/android"
    ),
    CHECK_BOXES(
        image = R.drawable.checkboxes,
        title = "Checkboxes",
        subTitle = "Checkboxes allow users to select one or more items from a set. Checkboxes can turn an option on or off.",
        link = "https://material.io/components/checkboxes/android"
    ),
    CHIPS(
        image = R.drawable.chips,
        title = "Chips",
        subTitle = "Chips are compact elements that represent an input, attribute, or action.",
        link = "https://material.io/components/chips/android#using-chips"
    ),
    DATE_PICKERS(
        image = R.drawable.date_pickers,
        title = "Date pickers",
        subTitle = "Date pickers let users select a date, or a range of dates.",
        link = "https://material.io/components/date-pickers/android#using-date-pickers"
    ),
    DIALOGS(
        image = R.drawable.dialogs,
        title = "Dialogs",
        subTitle = "Dialogs inform users about a task and can contain critical information, require decisions, or involve multiple tasks.",
        link = "https://material.io/components/dialogs/android"
    ),
    DIVIDERS(
        image = R.drawable.dividers,
        title = "Dividers",
        subTitle = "A divider is a thin line that groups content in lists and layouts.",
        link = "https://material.io/components/dividers/android"
    ),
    MENUS(
        image = R.drawable.menus,
        title = "Menus",
        subTitle = "Menus display a list of choices on temporary surfaces.",
        link = "https://material.io/components/menus/android"
    ),
    NAVIGATION_DRAWER(
        image = R.drawable.navigation_drawer,
        title = "Navigation drawer",
        subTitle = "Navigation drawers provide access to destinations in your app.",
        link = "https://material.io/components/navigation-drawer/android"
    ),
    NAVIGATION_RAIL(
        image = R.drawable.navigation_rail,
        title = "Navigation rail",
        subTitle = "Navigation rails provide ergonomic movement between primary destinations in apps.",
        link = "https://material.io/components/navigation-rail/android"
    ),
    PROGRESS_INDICATORS(
        image = R.drawable.progress_indicators,
        title = "Progress indicators",
        subTitle = "Progress indicators express an unspecified wait time or display the length of a process.",
        link = "https://material.io/components/progress-indicators/android"
    ),
    RADIO_BUTTONS(
        image = R.drawable.radio_buttons,
        title = "Radio buttons",
        subTitle = "Radio buttons allow users to select one option from a set.",
        link = "https://material.io/components/radio-buttons/android"
    ),
    SHEETS_BOTTOM(
        image = R.drawable.sheets_bottom,
        title = "Sheets: bottom",
        subTitle = "Bottom sheets are surfaces containing supplementary content that are anchored to the bottom of the screen.",
        link = "https://material.io/components/sheets-bottom/android"
    ),
    SLIDERS(
        image = R.drawable.sliders,
        title = "Sliders",
        subTitle = "Sliders allow users to make selections from a range of values.",
        link = "https://material.io/components/sliders/android"
    ),
    SNACKBARS(
        image = R.drawable.snackbars,
        title = "Snackbars",
        subTitle = "Snackbars provide brief messages about app processes at the bottom of the screen.",
        link = "https://material.io/components/snackbars/android"
    ),
    SWITCHES(
        image = R.drawable.switches,
        title = "Switches",
        subTitle = "Switches toggle the state of a single item on or off.",
        link = "https://material.io/components/switches/android"
    ),
    TABS(
        image = R.drawable.tabs,
        title = "Tabs",
        subTitle = "Tabs organize content across different screens, data sets, and other interactions.",
        link = "https://material.io/components/tabs/android"
    ),
    TEXT_FIELDS(
        image = R.drawable.text_fields,
        title = "Text fields",
        subTitle = "Text fields let users enter and edit text.",
        link = "https://material.io/components/text-fields/android"
    ),
    TIME_PICKERS(
        image = R.drawable.time_pickers,
        title = "Time pickers",
        subTitle = "Time pickers help users select and set a specific time.",
        link = "https://material.io/components/time-pickers/android"
    )
}