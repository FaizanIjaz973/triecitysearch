package com.example.triecitysearch.view

import ClickListener
import Items
import RecyclerviewItemAdapter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.triecitysearch.R
import com.example.triecitysearch.model.JsonEntry
import com.example.triecitysearch.viewmodel.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() , OnMapReadyCallback{

    private val viewModel: MainViewModel by viewModels()
    private var job: Job? = null
    private lateinit var textView: TextView
    private lateinit var recyclerView:RecyclerView
    lateinit var editText:EditText
    private lateinit var progressBar:ProgressBar
    lateinit var dialog : BottomSheetDialog
    private var itemsList : MutableList<Items>? = mutableListOf()
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initEditTextView()
        initTextView()
        initRecyclerView()
        initProgressBar()
        initBottomSheet()

        val nameObserver = Observer<List<JsonEntry>> { newName ->
            // Update the UI, in this case, a TextView.
            textView.text = newName.size.toString()
            if(job != null && job!!.isActive)
                job!!.cancel()

            job = CoroutineScope(Dispatchers.Main).launch {
                yield()
                itemsList!!.clear()
                for (i in newName.indices) {
                    val items = Items(newName[i].name + ", " + newName[i].country,  i, newName[i].coord)
                    itemsList!!.add(i, items)
                }
                progressBar.visibility = View.INVISIBLE
                recyclerView.adapter!!.notifyDataSetChanged()
            }
        }

        val treeReadyObserver = Observer<String> { Ready ->
            // Update the UI, in this case, a TextView.
            if (Ready.equals("true")){
                editText.isEnabled = true
                progressBar.visibility = View.INVISIBLE
            }
        }

        //Observe the readiness of the trie
        viewModel.treeReadyObservableData.observe(this, treeReadyObserver)

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        viewModel.currentName.observe(this, nameObserver)
   }

    private fun initRecyclerView(){
        recyclerView = findViewById<View>(R.id.recycleView) as RecyclerView
        val recyclerviewItemAdapter = RecyclerviewItemAdapter(itemsList!!)
        recyclerView.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = recyclerviewItemAdapter

        recyclerviewItemAdapter.setOnItemClickListener(object : ClickListener<Items> {
            override fun onClick(view: View?, data: Items, position: Int) {
                mMap.clear()
                // Add a marker to the city and move the camera
                val city = LatLng(data.coord.lat, data.coord.lon)
                mMap.addMarker(MarkerOptions()
                    .position(city)
                    .title("Marker in ${data.name}"))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(city))
                dialog.show()
            }
        })
    }
    private fun initProgressBar(){
        progressBar = findViewById(R.id.progressBar)
    }
    private fun initEditTextView(){
        editText = findViewById(R.id.editText)
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(viewModel.isTreeReady()){
                    viewModel.searchCity(editText.text.toString().lowercase())
                }
            }
        })
    }
    private fun initTextView(){
        textView = findViewById(R.id.textView)
    }
    private fun initBottomSheet(){
        dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog_layout, null)
        dialog.setContentView(view)
        dialog.setCancelable(true)

        //To keep the bottom sheet fullscreen
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map1) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) { mMap = googleMap }
}