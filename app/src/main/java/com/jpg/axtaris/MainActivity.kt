package com.jpg.axtaris

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity

import android.view.Menu
import android.view.MenuItem
import android.view.View

import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.annotations.Marker

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

import android.content.Intent
import com.jpg.axtaris.auth.PhoneAuthActivity
//import sun.awt.windows.ThemeReader.getPosition

import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.support.annotation.NonNull
import android.widget.Toast






class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        MapboxMap.OnMapClickListener {

    private var mapView: MapView? = null
    private var mapboxMap: MapboxMap? = null
    private var marker: Marker? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, "pk.eyJ1IjoiY2hpbm1heWdhcmciLCJhIjoiY2podzRoZTZpMHQxczNrbGhqMXdub28zNCJ9.oU1rSDWuCq1U9sw8ZTy7qg")

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mapView = findViewById<View>(R.id.mapView) as MapView?
        mapView!!.onCreate(savedInstanceState)

        //for stationary marker over iitj
        /*mapView!!.getMapAsync(OnMapReadyCallback { mapboxMap ->
            mapboxMap.addMarker(MarkerOptions()
                    .position(LatLng(26.472885, 73.113890))
                    .title(getString(R.string.draw_marker_options_title))
                    .snippet(getString(R.string.draw_marker_options_snippet)))
        })*/

        //for sliding marker
        mapView!!.getMapAsync(this)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    public override fun onStart() {
        super.onStart()
        mapView!!.onStart()
    }

    public override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

    public override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

    public override fun onStop() {
        super.onStop()
        mapView!!.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView!!.onSaveInstanceState(outState!!)
    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
                startActivity(Intent(this@MainActivity, PhoneAuthActivity::class.java))
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this@MainActivity.mapboxMap = mapboxMap

        marker = mapboxMap.addMarker(MarkerOptions()
                .position(LatLng(26.472885, 73.113890)))

        Toast.makeText(
                this@MainActivity,
                getString(R.string.tap_on_map_instruction),
                Toast.LENGTH_LONG
        ).show()

        mapboxMap.addOnMapClickListener(this)
    }

    override fun onMapClick(point: LatLng) {
        // When the user clicks on the map, we want to animate the marker to that
        // location.
        val markerAnimator = ObjectAnimator.ofObject(marker, "position",
                LatLngEvaluator(), marker!!.position, point)
        markerAnimator.duration = 1000
        markerAnimator.start()
    }

    private class LatLngEvaluator : TypeEvaluator<LatLng> {
        // Method is used to interpolate the marker animation.

        private val latLng = LatLng()

        override fun evaluate(fraction: Float, startValue: LatLng, endValue: LatLng): LatLng {
            latLng.latitude = startValue.latitude + (endValue.latitude - startValue.latitude) * fraction
            latLng.longitude = startValue.longitude + (endValue.longitude - startValue.longitude) * fraction
            return latLng
        }
    }
}
