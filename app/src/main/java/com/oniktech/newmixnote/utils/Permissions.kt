package Permissions

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.content.ContextCompat

private const val PERMISSIONS_REQUEST_CODE = 10
private val PERMISSIONS_REQUIRED = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.RECORD_AUDIO,
    Manifest.permission.WRITE_CONTACTS,
    Manifest.permission.READ_EXTERNAL_STORAGE )

@TargetApi(Build.VERSION_CODES.M)
fun requestPermissions(activity: Activity):Boolean{
    if (!isPermissiosGranted(activity.applicationContext)) {
        activity.requestPermissions(PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST_CODE)
        Toast.makeText(activity.applicationContext , "permission Granted" , Toast.LENGTH_LONG).show()
        return true
    }else{
        Toast.makeText(activity.applicationContext , "permission not Granted" , Toast.LENGTH_LONG).show()
        return false
    }

}

fun isPermissiosGranted(context: Context) = PERMISSIONS_REQUIRED.all{
    ContextCompat.checkSelfPermission(context , it) == PackageManager.PERMISSION_GRANTED
}


