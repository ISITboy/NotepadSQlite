package com.example.notepad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.db.MyDbManager
import com.example.notepad.databinding.ActivityMainBinding
import com.example.notepad.databinding.EditActivityBinding
import com.example.notepad.db.MyAdapter

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding

    val myDbManager = MyDbManager(this)
    val myAdapter = MyAdapter(ArrayList(),this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initSearchView()
    }




    fun onClickNew(view : View){
        val i = Intent(this,EditActivity::class.java)
        startActivity(i)

    }
    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
        fillAdapter()
    }
    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    fun initSearchView(){
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                val list = myDbManager.readDbDate(p0!!)
                myAdapter.updateAdapter(list)
               return true
            }
        })
    }

    fun init() = with(binding){
        rcView.layoutManager = LinearLayoutManager(this@MainActivity)
        val swapHelper = getSwapMg()
        swapHelper.attachToRecyclerView(rcView)
        rcView.adapter=myAdapter

    }
    fun fillAdapter(){

        val list = myDbManager.readDbDate("")
        myAdapter.updateAdapter(list)
        if(list.size>0) {
            binding.tvNotElements.visibility = View.GONE
        }else
            binding.tvNotElements.visibility = View.VISIBLE
    }

    private fun getSwapMg():ItemTouchHelper{
        return ItemTouchHelper(object :ItemTouchHelper.
        SimpleCallback(0,ItemTouchHelper.RIGHT /*or ItemTouchHelper.LEFT*/ ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                myAdapter.removeItem(viewHolder.adapterPosition,myDbManager)
            }

        })
    }

}