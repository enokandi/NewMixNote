package com.oniktech.newmixnote.fragments

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation

import com.oniktech.newmixnote.R
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val DEFAULT_MODE = 0
private const val RECORDING_MODE = 1
private const val CAMERA_MODE = 2
private const val NOTE_MODE = 3

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HostFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HostFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HostFragment : Fragment() {
    public var thisNoteDirectoryName: String = ""

    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    lateinit var bigBottun: ImageButton
    lateinit var rightButton : ImageButton
    lateinit var leftButton : ImageButton
    lateinit var activity: Activity

    lateinit var hostFragment : ComplexCamera
    lateinit var frameLayout: FrameLayout

    private var camearaFragment: ComplexCamera? = null

    var currentMode: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        currentMode = DEFAULT_MODE

        thisNoteDirectoryName = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(Date())
        Toast.makeText(context , thisNoteDirectoryName , Toast.LENGTH_LONG).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_host, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // frameLayout = view.findViewById(R.id.complex_host_frame_layout)
       // hostFragment = ComplexCamera()
        //fragmentManager?.beginTransaction()?.add(R.id.complex_host_frame_layout , hostFragment)?.commit()
       // hostFragment.test(view.context)

        /*
        bigBottun = view.findViewById(R.id.camera_complexStartRecordButton)
        val navController = Navigation.findNavController(activity , R.id.host_fragment)
        navController.currentDestination
        bigBottun.setOnClickListener(){
             navController.navigate(R.id.action_complexNote_to_complexRecordingFragment)
            currentMode = RECORDING_MODE
            bigBottun.background = resources.getDrawable(R.drawable.pause_complex)
        }

        rightButton = view.findViewById(R.id.camera_complex_host_right_button)
        rightButton.setOnClickListener(){
            navController.navigate(R.id.action_complexNote_to_complexCamera)
            if (currentMode != CAMERA_MODE){
                currentMode = CAMERA_MODE
                rightButton.background = resources.getDrawable(R.drawable.back_pic)
                leftButton.background = resources.getDrawable(R.drawable.capturepic)
            } else{

            }

        }

        leftButton = view.findViewById(R.id.camera_complex_host_left_button)
        leftButton.setOnClickListener(){
            if (currentMode == CAMERA_MODE){
                takePic()
            }
        }   */
    }

    private fun takePic() {

    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

 /*   override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }  */

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HostFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(activity : Activity) =
            HostFragment().apply {
               this.activity = activity
            }
    }
}
