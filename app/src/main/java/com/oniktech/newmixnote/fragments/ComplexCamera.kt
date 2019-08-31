package com.oniktech.newmixnote.fragments


import androidx.camera.core.CameraX
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysisConfig
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureConfig
import androidx.camera.core.Preview
import androidx.camera.core.PreviewConfig

import android.content.Context
import android.content.Intent
import android.hardware.Camera
import android.hardware.display.DisplayManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.*
import android.provider.Telephony.Mms.Part.FILENAME
import android.util.DisplayMetrics
import android.util.Log
import android.util.Rational
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ImageButton
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.util.*

import com.oniktech.newmixnote.R
import com.oniktech.newmixnote.utils.AutoFitPreviewBuilder
import java.io.File
import java.nio.file.Files.createFile
import java.text.SimpleDateFormat

typealias LumaListener = (luma: Double) -> Unit


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val TAG = "mytag"


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ComplexCamera.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ComplexCamera.newInstance] factory method to
 * create an instance of this fragment.
 */
class ComplexCamera : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    lateinit var textureView: TextureView
    lateinit var pauseButton: ImageButton
    lateinit var backButton: ImageButton
    lateinit var captureButton: ImageButton

    private lateinit var outputDirectory: File
    private lateinit var broadcastManager: LocalBroadcastManager
    private lateinit var thisNoteDirectoryName: String
    private lateinit var container: ConstraintLayout

    private var displayId = -1
    private var lensFacing = CameraX.LensFacing.BACK
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var imageAnalyzer: ImageAnalysis? = null

    private lateinit var displayManager: DisplayManager

    /**
     * We need a display listener for orientation changes that do not trigger a configuration
     * change, for example if we choose to override config change in manifest or for 180-degree
     * orientation changes.
     */
    private val displayListener = object : DisplayManager.DisplayListener {
        override fun onDisplayAdded(displayId: Int) = Unit
        override fun onDisplayRemoved(displayId: Int) = Unit
        override fun onDisplayChanged(displayId: Int) = view?.let { view ->
            if (displayId == this@ComplexCamera.displayId) {
                Log.d(TAG, "Rotation changed: ${view.display.rotation}")

                preview?.setTargetRotation(view.display.rotation)
                imageCapture?.setTargetRotation(view.display.rotation)
                imageAnalyzer?.setTargetRotation(view.display.rotation)
                activity?.windowManager?.defaultDisplay?.rotation?.let { preview?.setTargetRotation(it) }
                activity?.windowManager?.defaultDisplay?.rotation?.let { preview?.setTargetRotation(it) }
                activity?.windowManager?.defaultDisplay?.rotation?.let { preview?.setTargetRotation(it) }

            }
        } ?: Unit
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      /*  arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        } */
        retainInstance = true

    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Unregister the broadcast receivers and listeners
        displayManager.unregisterDisplayListener(displayListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_complex_camera, container, false)
    }



 /*   private val imageSavedListener = object : ImageCapture.OnImageSavedListener {
        override fun onError(
            error: ImageCapture.UseCaseError, message: String, exc: Throwable?) {
            Log.e(TAG, "Photo capture failed: $message")
            exc?.printStackTrace()
        }

        override fun onImageSaved(photoFile: File) {
            Log.d(TAG, "Photo capture succeeded: ${photoFile.absolutePath}")


            // We can only change the foreground Drawable using API level 23+ API
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                // Update the gallery thumbnail with latest picture taken
                setGalleryThumbnail(photoFile)
            }

            // Implicit broadcasts will be ignored for devices running API
            // level >= 24, so if you only target 24+ you can remove this statement
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                requireActivity().sendBroadcast(
                    Intent(Camera.ACTION_NEW_PICTURE, Uri.fromFile(photoFile))
                )
            }

            // If the folder selected is an external media directory, this is unnecessary
            // but otherwise other apps will not be able to access our images unless we
            // scan them using [MediaScannerConnection]
            val mimeType = MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension(photoFile.extension)
            MediaScannerConnection.scanFile(
                context, arrayOf(photoFile.absolutePath), arrayOf(mimeType), null)
        }
    }

  */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        thisNoteDirectoryName = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(Date())

        container = view as ConstraintLayout
        textureView = view.findViewById(R.id.complexCameraPreviewTextureView)
        pauseButton = view.findViewById(R.id.camera_complexStartRecordButton)
        backButton = view.findViewById(R.id.camera_complex_host_right_button)
        captureButton = view.findViewById(R.id.camera_complex_host_left_button)

        // broadcastManager = LocalBroadcastManager.getInstance(view.context)

        // Every time the orientation of device changes, recompute layout
        displayManager = textureView.context
            .getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        displayManager.registerDisplayListener(displayListener, null)

        FolderCreater.createMainFolder()
        outputDirectory = File(Environment.getExternalStorageDirectory().absolutePath + "/mixnote")

        // Wait for the views to be properly laid out
        textureView.post {
            // Keep track of the display in which this view is attached
            displayId = textureView.display.displayId

            // Build UI controls and bind all camera use cases
            updateCameraUi()
            bindCameraUseCases()

            onClick()

            // In the background, load latest photo taken (if any) for gallery thumbnail
            /*  lifecycleScope.launch(Dispatchers.IO) {
                outputDirectory.listFiles { file ->
                    EXTENSION_WHITELIST.contains(file?.extension?.toUpperCase())
                }.sorted().reversed().firstOrNull()?.let { setGalleryThumbnail(it) } */

        }
    }

    private fun onClick() {
        captureButton.setOnClickListener(){

            val file = File(Environment.getExternalStorageDirectory().absolutePath +"/jafar")

                if(file.mkdirs())
                    Toast.makeText(context , "shod" , Toast.LENGTH_LONG)


            val photoFile: File = createFile(outputDirectory, FILENAME, PHOTO_EXTENSION , "ali")

            val metadata = ImageCapture.Metadata().apply {
                // Mirror image when using the front camera
                isReversedHorizontal = lensFacing == CameraX.LensFacing.FRONT
            }

            imageCapture?.takePicture(photoFile, imageSavedListener , metadata
                )
        }

        backButton.setOnClickListener(){

        }

        pauseButton.setOnClickListener(){

        }
    }

    private fun bindCameraUseCases() {

        // Get screen metrics used to setup camera for full screen resolution
        val metrics = DisplayMetrics().also {
            textureView.display.getRealMetrics(it) }

        Toast.makeText(context , "w: ${metrics.widthPixels} , H: ${metrics.heightPixels}" , Toast.LENGTH_LONG).show()

        metrics.widthPixels = 1200
        val screenAspectRatio = Rational(metrics.widthPixels, metrics.heightPixels)
        Log.d(TAG, "Screen metrics: ${metrics.widthPixels} x ${metrics.heightPixels}")

        // Set up the view finder use case to display camera preview
        val viewFinderConfig = PreviewConfig.Builder().apply {
            setLensFacing(lensFacing)
            // We request aspect ratio but no resolution to let CameraX optimize our use cases
            setTargetAspectRatio(screenAspectRatio)
            // Set initial target rotation, we will have to call this again if rotation changes
            // during the lifecycle of this use case
            setTargetRotation(textureView.display.rotation)
        }.build()

        // Use the auto-fit preview builder to automatically handle size and orientation changes
        preview = AutoFitPreviewBuilder.build(viewFinderConfig, textureView)


        // Set up the capture use case to allow users to take photos
        val imageCaptureConfig = ImageCaptureConfig.Builder().apply {
            setLensFacing(lensFacing)
            setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
            // We request aspect ratio but no resolution to match preview config but letting
            // CameraX optimize for whatever specific resolution best fits requested capture mode
            setTargetAspectRatio(screenAspectRatio)
            // Set initial target rotation, we will have to call this again if rotation changes
            // during the lifecycle of this use case
            setTargetRotation(textureView.display.rotation)
        }.build()

        imageCapture = ImageCapture(imageCaptureConfig)

        // Setup image analysis pipeline that computes average pixel luminance in real time
        val analyzerConfig = ImageAnalysisConfig.Builder().apply {
            setLensFacing(lensFacing)
            // Use a worker thread for image analysis to prevent preview glitches
            val analyzerThread = HandlerThread("LuminosityAnalysis").apply { start() }
            setCallbackHandler(Handler(analyzerThread.looper))
            // In our analysis, we care more about the latest image than analyzing *every* image
            setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
            // Set initial target rotation, we will have to call this again if rotation changes
            // during the lifecycle of this use case
            setTargetRotation(textureView.display.rotation)
        }.build()

     /*   imageAnalyzer = ImageAnalysis(analyzerConfig).apply {
            analyzer = LuminosityAnalyzer { luma ->
                // Values returned from our analyzer are passed to the attached listener
                // We log image analysis results here -- you should do something useful instead!
                val fps = (analyzer as LuminosityAnalyzer).framesPerSecond
                Log.d(TAG, "Average luminosity: $luma. " +
                        "Frames per second: ${"%.01f".format(fps)}")
            }
        } */
        imageAnalyzer = ImageAnalysis(analyzerConfig)

        // Apply declared configs to CameraX using the same lifecycle owner
        CameraX.bindToLifecycle(
            viewLifecycleOwner, preview, imageCapture, imageAnalyzer)
    }

    private fun updateCameraUi() {

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
    }
*/
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

    private val imageSavedListener = object : ImageCapture.OnImageSavedListener {
        override fun onError(
            error: ImageCapture.UseCaseError, message: String, exc: Throwable?) {
            Log.e(TAG, "Photo capture failed: $message")
            exc?.printStackTrace()
            Toast.makeText(context , "ridi"  , Toast.LENGTH_LONG).show()
        }

        override fun onImageSaved(photoFile: File) {
            Log.d(TAG, "Photo capture succeeded: ${photoFile.absolutePath}")


            // We can only change the foreground Drawable using API level 23+ API
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                // Update the gallery thumbnail with latest picture taken
            }

            // Implicit broadcasts will be ignored for devices running API
            // level >= 24, so if you only target 24+ you can remove this statement
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                requireActivity().sendBroadcast(
                    Intent(Camera.ACTION_NEW_PICTURE, Uri.fromFile(photoFile))
                )
            }

            // If the folder selected is an external media directory, this is unnecessary
            // but otherwise other apps will not be able to access our images unless we
            // scan them using [MediaScannerConnection]
            val mimeType = MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension(photoFile.extension)
            MediaScannerConnection.scanFile(
                context, arrayOf(photoFile.absolutePath), arrayOf(mimeType), null)
        }
    }

    companion object {

        private const val TAG = "CameraXBasic"
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"

        /** Helper function used to create a timestamped file */
        private fun createFile(
            baseFolder: File,
            format: String,
            extension: String,
            thisNoteDirectoryName: String
        ): File {
            val thisNoteFolder:File = File(baseFolder.absolutePath + "/$thisNoteDirectoryName" )

            //making specific folder for each note
            if (!thisNoteFolder.exists())
                thisNoteFolder.mkdirs()

            return File(thisNoteFolder, SimpleDateFormat(format, Locale.US)
                .format(System.currentTimeMillis()) + extension)
        }

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ComplexCamera().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
