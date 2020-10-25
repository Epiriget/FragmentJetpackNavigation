package com.example.fragmentjetpacknavigation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment

class ThirdFragment : Fragment() {
    private var calledFrom: String = ""

    companion object {
        const val CALLED_FROM_KEY = "ThirdFragment.CALLED_FROM_KEY"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        calledFrom = arguments?.getString(SecondFragment.CALLED_FROM_KEY) ?: "Missing"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_third, container, false)

        val textView: TextView = view.findViewById(R.id.thirdFragmentLabel)
        val text: String = textView.text.toString() + " called from: " + calledFrom;
        textView.text = text


        val button: Button = view.findViewById(R.id.button_to_first_fragment)
        button.setOnClickListener{ startFirstFragment() }
        return view
    }

    private fun startFirstFragment() {
        val navHostFragment =
            activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bundle = Bundle().apply { putString(CALLED_FROM_KEY, ThirdFragment::class.java.simpleName) }
        navController.navigate(R.id.action_thirdFragment_to_firstFragment, bundle)
    }

}
