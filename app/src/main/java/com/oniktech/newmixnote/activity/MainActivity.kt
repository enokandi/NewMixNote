package com.oniktech.newmixnote.activity

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.oniktech.newmixnote.R
import com.oniktech.newmixnote.fragments.ComplexNote
import com.oniktech.newmixnote.fragments.HostFragment
import com.oniktech.newmixnote.fragments.SimpleNote
import com.oniktech.newmixnote.utils.MyViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!Permissions.requestPermissions(this)){ }
        val adapter: MyViewPagerAdapter = MyViewPagerAdapter(supportFragmentManager)
        val simpleNote = SimpleNote()
        val complexNote = HostFragment.newInstance(this)
        adapter.addFragment(simpleNote, "simpleNote")
        adapter.addFragment(complexNote , "complexNote")
        viewPager.adapter = adapter
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Permissions.PERMISSIONS_REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){

            } else{
                finish()
            }
        }
    }
}
