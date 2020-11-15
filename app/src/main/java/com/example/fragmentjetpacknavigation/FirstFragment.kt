package com.example.fragmentjetpacknavigation

import android.content.*
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.NavHostFragment

//Todo(question: Где лучше всего размещать управление навигации фрагментами?)
//Todo(question: Использует ли action из nav_graph вызов вида Navigation.createNavigateOnClickListener?)
//Todo(question: Что обозначает конструкция вида :View? { в вызове onCreateView)
//Todo(question: Можно ли использовать getInstance в случае с navigation action?)

class FirstFragment : Fragment() {
    private var calledFrom: String = ""
    private lateinit var mServiceBroadcastReceiver: BroadcastReceiver
    private var isServiceBroadcastReceiverCreated: Boolean = false


    companion object {
        const val CALLED_FROM_KEY = "FirstFragment.CALLED_FROM_KEY"
        public const val SERVICE_RESULT = "SERVICE_RESULT"
        public const val SERVICE_START_ARGS_KEY = "SERVICE_ARGS"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        calledFrom = arguments?.getString(ThirdFragment.CALLED_FROM_KEY) ?: "Missing"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first, container, false)

        val textView: TextView = view.findViewById(R.id.firstFragmentLabel)
        val text: String = textView.text.toString() + " called from: " + calledFrom;
        textView.text = text

        val buttonToSecondFragment: Button = view.findViewById(R.id.button_to_second_fragment)
        val serviceButton:Button = view.findViewById(R.id.service_button)
        val buttonToContactFragment: Button = view.findViewById(R.id.button_to_contact_fragment)


        //        button.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.secondFragment, null))
        buttonToSecondFragment.setOnClickListener {startSecondFragment()}
        buttonToContactFragment.setOnClickListener {startContactProviderFragment()}
        serviceButton.setOnClickListener {
            val inputField: EditText = view.findViewById(R.id.input_fib_field)
            val inputValue = inputField.text.toString().toLongOrNull()
            startFibonacciService(inputValue ?: 0L)
        }


        return view
    }

    private fun startFibonacciService(value:Long) {
        Intent(context, FibonacciService::class.java).apply {
                putExtra(SERVICE_START_ARGS_KEY, value)
                context?.startService(this)
        }
        if(!isServiceBroadcastReceiverCreated) {
            mServiceBroadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    Log.d("BroadcastReceiver", "Invoked")
                    val res: Long = intent?.getLongExtra(SERVICE_RESULT, 0L) ?: 0L
                    val resTextView: TextView? = activity?.findViewById(R.id.result_textview)
                    resTextView?.text = getString(R.string.result_message, res);
                }
            }
            LocalBroadcastManager.getInstance(requireContext())
                .registerReceiver(mServiceBroadcastReceiver, IntentFilter(FibonacciService::class.java.simpleName))
            isServiceBroadcastReceiverCreated = true
        }
    }

    private fun startSecondFragment() {
        val navHostFragment =
            activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bundle = Bundle().apply { putString(CALLED_FROM_KEY, FirstFragment::class.java.simpleName) }
        navController.navigate(R.id.action_firstFragment_to_secondFragment, bundle)
    }

    private fun startContactProviderFragment() {
        val navHostFragment =
            activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navController.navigate(R.id.action_firstFragment_to_contactProviderFragment)
    }

    override fun onStop() {
        super.onStop()
        if(isServiceBroadcastReceiverCreated) {
            context?.unregisterReceiver(mServiceBroadcastReceiver)
            isServiceBroadcastReceiverCreated = false
        }
    }

}
