package com.example.notepad

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.myfirstapp.db.MyDbManager
import com.example.notepad.databinding.EditActivityBinding
import com.example.notepad.db.MyIntentConstants

class EditActivity : AppCompatActivity() {

    val myDbManager = MyDbManager(this)

    lateinit var binding : EditActivityBinding
    val imageRequestCode = 10
    var tempImageUri = "empty"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getMyIntents()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == imageRequestCode){
            binding.imageView.setImageURI(data?.data)
            tempImageUri = data?.data.toString()
            contentResolver.takePersistableUriPermission(data?.data!!,Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
    }
    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }



    fun onClickAddImage(view: View) = with(binding){
        twoLayout.visibility = View.VISIBLE
        fbAddImage.visibility=View.GONE
    }

    fun onClickDeleteImage(view: View) = with(binding){
        twoLayout.visibility=View.GONE
        fbAddImage.visibility=View.VISIBLE
    }

    fun onClickChooseImage(view: View) = with(binding){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent,imageRequestCode)
    }
    fun onClickSave(view: View) = with(binding){
        val myTitle = edTitle.text.toString()
        val myDesc = edDesc.text.toString()

        if(myTitle!="" && myDesc!=""){
            myDbManager.insertToDb(myTitle,myDesc,tempImageUri)
        }
        finish()
    }


    fun getMyIntents()=with(binding){
        val i = intent
        if(i!=null){
            if(i.getStringExtra(MyIntentConstants.I_TITLE_KEY)!=null){
                fbAddImage.visibility=View.GONE
                edTitle.setText(i.getStringExtra(MyIntentConstants.I_TITLE_KEY))
                edDesc.setText(i.getStringExtra(MyIntentConstants.I_DESC_KEY))
                if(i.getStringExtra(MyIntentConstants.I_URI_KEY)!="empty"){
                    twoLayout.visibility = View.VISIBLE
                    imageView.setImageURI(Uri.parse(i.getStringExtra(MyIntentConstants.I_URI_KEY)))
                    imButtonEditImage.visibility = View.GONE
                    inButtonDeleteImage.visibility = View.GONE
                }
            }
        }
    }
}