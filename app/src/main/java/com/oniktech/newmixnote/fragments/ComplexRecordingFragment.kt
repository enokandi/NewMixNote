package com.oniktech.newmixnote.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.oniktech.newmixnote.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ComplexRecordingFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ComplexRecordingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ComplexRecordingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

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
        return inflater.inflate(R.layout.fragment_complex_recording, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
     /*   bigBottun = view.findViewById(R.id.camera_complexStartRecordButton)
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
        } */
    }

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
         * @return A new instance of fragment ComplexRecordingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ComplexRecordingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
