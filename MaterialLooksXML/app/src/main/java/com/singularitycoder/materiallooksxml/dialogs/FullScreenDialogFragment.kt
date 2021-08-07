package com.singularitycoder.materiallooksxml.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.singularitycoder.materiallooksxml.Constants
import com.singularitycoder.materiallooksxml.MainActivity
import com.singularitycoder.materiallooksxml.R
import com.singularitycoder.materiallooksxml.convertLongToTime
import com.singularitycoder.materiallooksxml.databinding.FragmentFullScreenDialogBinding

// https://developer.android.com/guide/topics/ui/dialogs#FullscreenDialog
class FullScreenDialogFragment(val listener: (person: Person) -> Unit = {}) : DialogFragment() {

    private lateinit var myContext: Context
    private lateinit var myActivity: MainActivity
    private lateinit var binding: FragmentFullScreenDialogBinding

    // Fragments must have public no-arg constructor
    constructor() : this({})

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myContext = context
        myActivity = context as MainActivity
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Modify dialog specific attributes here
        return super.onCreateDialog(savedInstanceState).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFullScreenDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.setOnClickListener { }
        setUpToolbar()
        setUpDatePicker()
        setUpProfessionDropDown()
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            title = "Full Screen Dialog"
            setNavigationOnClickListener { dismiss() }
            inflateMenu(R.menu.full_screen_dialog_menu)
            setOnMenuItemClickListener {
                val person = Person(
                    binding.etName.editText?.text.toString(),
                    binding.etDob.editText?.text.toString(),
                    binding.etProfession.editText?.text.toString()
                )
                listener.invoke(person)
                dismiss()
                true
            }
        }
    }

    private fun setUpDatePicker() {
        binding.etDob.editText?.apply {
            isFocusable = false
            isClickable = true
            isLongClickable = false
            setOnClickListener {
                val datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()
                datePicker.show(myActivity.supportFragmentManager, "TAG_DATE_PICKER")
                datePicker.apply {
                    addOnPositiveButtonClickListener { it: Long? ->
                        binding.etDob.editText?.setText(convertLongToTime(time = it ?: return@addOnPositiveButtonClickListener, type = 3u))
                    }
                    addOnNegativeButtonClickListener { }
                }
            }
        }
    }

    private fun setUpProfessionDropDown() {
        val professionAdapter = ArrayAdapter(myContext, android.R.layout.simple_list_item_1, Constants.professionList)
        (binding.etProfession.editText as? AutoCompleteTextView)?.setAdapter(professionAdapter)
    }
}

data class Person(
    val name: String,
    val dob: String,
    val profession: String
)