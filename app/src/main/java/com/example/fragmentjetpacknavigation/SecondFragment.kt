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

class SecondFragment : Fragment() {
    private var calledFrom: String = ""

    companion object {
        const val CALLED_FROM_KEY = "SecondFragment.CALLED_FROM_KEY"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        calledFrom = arguments?.getString(FirstFragment.CALLED_FROM_KEY) ?: "Missing"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_second, container, false)

        val textView: TextView = view.findViewById(R.id.secondFragmentLabel)
        val text: String = textView.text.toString() + " called from: " + calledFrom;
        textView.text = text

        val button: Button = view.findViewById(R.id.button_to_third_fragment)
        button.setOnClickListener {startThirdFragment()}

        return view
    }

    private fun startThirdFragment() {
        val navHostFragment =
            activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bundle = Bundle().apply { putString(CALLED_FROM_KEY, SecondFragment::class.java.simpleName) }
        navController.navigate(R.id.action_secondFragment_to_thirdFragment, bundle)
    }


}
