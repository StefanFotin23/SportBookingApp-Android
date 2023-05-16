package com.example.sportbookingapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // RecyclerView
    private lateinit var adapter: SportsRecyclerviewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var sportsArrayList: ArrayList<Sports>
    lateinit var imageId: Array<Int>
    lateinit var sportName: Array<String>

    // CalendarView
    private lateinit var calendarView: CalendarView
    private lateinit var calendar: Calendar
    private lateinit var selectedDate: Date

    // SpinnerView - Starting & Ending hour
    private lateinit var startingHourSpinner: Spinner
    private lateinit var endingHourSpinner: Spinner
    private lateinit var endingHourText: TextView

    // Lists that populate the choices in the SpinnerView
    private lateinit var availableStartingHoursList: List<String>
    private lateinit var availableEndingHoursList: List<String>

    private var startingHour = 0
    private var endingHour = 0
    private var totalPrice = 0

    // hint used for spinners
    val hint = "Select an hour"

    private lateinit var priceTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onResume() {
        super.onResume()
        val sharedPref = requireActivity()
            .getSharedPreferences("HomeFragment", Context.MODE_PRIVATE)
        adapter.setSelectedPosition(sharedPref.getInt("selectedPosition", -1))

        // SpinnerView - Starting & Ending hour
        updateAvailableReservationHours()
        startingReservationHoursInit(availableStartingHoursList)
        //endingReservationHoursInit(availableEndingHoursList)
    }

    override fun onPause() {
        super.onPause()
        val sharedPref = requireActivity()
            .getSharedPreferences("HomeFragment", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("selectedPosition", adapter.getSelectedPosition())
            putLong("date", selectedDate.time) // date in ms
            commit()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataInit()
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView = view.findViewById(R.id.sportsRecyclerView)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = SportsRecyclerviewAdapter(sportsArrayList)
        recyclerView.adapter = adapter

        // Initialize CalendarView
        calendarView = view.findViewById(R.id.calendarView)
        calendar = Calendar.getInstance()
        // Set minimum date to today
        val minDate = Calendar.getInstance()
        minDate.set(Calendar.HOUR_OF_DAY, 0)
        minDate.set(Calendar.MINUTE, 0)
        minDate.set(Calendar.SECOND, 0)
        calendarView.minDate = minDate.timeInMillis
        // Set maximum date to four weeks from now
        val maxDate = Calendar.getInstance()
        maxDate.add(Calendar.WEEK_OF_YEAR, 4)
        maxDate.set(Calendar.HOUR_OF_DAY, 0)
        maxDate.set(Calendar.MINUTE, 0)
        maxDate.set(Calendar.SECOND, 0)
        calendarView.maxDate = maxDate.timeInMillis
        // init the selectedDate by default
        selectedDate = Date(calendarView.minDate)

        // Set a listener to handle when the selected date changes
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Do something with the selected date
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            selectedDate = calendar.time
        }
        startingHourSpinner = view.findViewById(R.id.startingHourSpinner)
        endingHourSpinner = view.findViewById(R.id.endingHourSpinner)
        endingHourText = view.findViewById(R.id.endingHourTextView)
        priceTextView = view.findViewById(R.id.priceText)
    }

    private fun updateAvailableReservationHours() {
        availableStartingHoursList = listOf("1", "2", "3", "4")
        availableEndingHoursList = listOf("5", "6", "7", "8")
    }

    private fun startingReservationHoursInit(availableStartingHoursList: List<String>) {
        // Add a hint or prompt to the availableStartingHoursList
        val dataWithHint = listOf(hint) + availableStartingHoursList
        val startingHourAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item,
            dataWithHint
        )
        startingHourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        startingHourSpinner.adapter = startingHourAdapter

        // onItemSelected Listener
        startingHourSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position)
                if (selectedItem is String && selectedItem != hint) {
                    startingHour = selectedItem.toInt()
                    endingReservationHoursInit(availableEndingHoursList)
                } else if (selectedItem is String && selectedItem == hint) {
                    endingHourSpinner.visibility = View.INVISIBLE
                    endingHourText.visibility = View.INVISIBLE
                    startingHour = -1
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                endingHourSpinner.visibility = View.INVISIBLE
                endingHourText.visibility = View.INVISIBLE
            }
        }
    }

    private fun endingReservationHoursInit(
        availableEndingHoursList: List<String>
    ) {
        val dataWithHint = listOf(hint) + availableEndingHoursList
        val endingHourAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item,
            dataWithHint
        )
        endingHourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        endingHourSpinner.adapter = endingHourAdapter
        endingHourSpinner.visibility = View.VISIBLE
        endingHourText.visibility = View.VISIBLE

        // onItemSelected Listener
        endingHourSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position)
                if (selectedItem is String && selectedItem != hint) {
                    endingHour = selectedItem.toInt()
                    totalPrice = calculateTotalPrice(50)
                    val priceString = getString(R.string.price_text, totalPrice.toString())
                    priceTextView.text = priceString
                    priceTextView.visibility = View.VISIBLE
                } else if (selectedItem is String && selectedItem == hint) {
                    endingHour = -1
                    priceTextView.visibility = View.INVISIBLE
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }

    private fun calculateTotalPrice(price: Int): Int {
        return price * (endingHour - startingHour)
    }

    private fun dataInit() {
        sportsArrayList = arrayListOf()
        imageId = arrayOf(
            R.drawable.soccer_field,
            R.drawable.basketball_field,
            R.drawable.tennis_field,
            R.drawable.volleyball_field
        )
        sportName = arrayOf(
            getString(R.string.football),
            getString(R.string.basketball),
            getString(R.string.tennis),
            getString(R.string.volleyball)
        )

        for (i in imageId.indices) {
            val sport = Sports(imageId[i], sportName[i])
            sportsArrayList.add(sport)
        }
    }
}
