package com.example.sportbookingapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

    private lateinit var adapter: SportsRecyclerviewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var sportsArrayList: ArrayList<Sports>

    lateinit var imageId: Array<Int>
    lateinit var sportName: Array<String>

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selectedPosition", adapter.getSelectedPosition())
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            adapter.setSelectedPosition(it.getInt("selectedPosition", -1))
            adapter.notifyDataSetChanged()
        }
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
    }

    private fun dataInit() {
        sportsArrayList = arrayListOf<Sports>()
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