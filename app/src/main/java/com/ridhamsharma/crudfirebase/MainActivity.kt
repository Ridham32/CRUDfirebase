package com.ridhamsharma.crudfirebase

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.ridhamsharma.crudfirebase.databinding.ActivityMainBinding
import com.ridhamsharma.crudfirebase.databinding.CustomfabBinding

class MainActivity : AppCompatActivity(), NotesInterface {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: RecyclerViewAdapter
    lateinit var layoutManager: LinearLayoutManager
    var notesList = arrayListOf<NotesDataClass>()
    var firestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        layoutManager = LinearLayoutManager(this)
        binding.recycler.layoutManager = layoutManager
        adapter = RecyclerViewAdapter(notesList, this)
        binding.recycler.adapter = adapter
        getCollectionData()
        binding.fab.setOnClickListener {
            var dialog = Dialog(this)
            var dialogBinding = CustomfabBinding.inflate(layoutInflater)
            dialog.setContentView(dialogBinding.root)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialogBinding.btnAdd.setOnClickListener {
                if (dialogBinding.etCustomtitle.text.toString().isNullOrEmpty()) {
                    dialogBinding.etCustomtitle.error = "Enter the Title"
                } else if (dialogBinding.etCustomDescription.text.toString().isNullOrEmpty()) {
                    dialogBinding.etCustomDescription.error = "Enter the Description"
                } else {
                    /*notesList.add(
                    NotesDataClass(
                        title = dialogBinding.etCustomtitle.text.toString(),
                        description = dialogBinding.etCustomDescription.text.toString()
                    )
                )*/
                    firestore.collection("mynotes").add(
                        NotesDataClass(
                            title = dialogBinding.etCustomtitle.text.toString(),
                            description = dialogBinding.etCustomDescription.text.toString()
                        )
                    )
                        .addOnSuccessListener {
                            getCollectionData()
                            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
                        }
                        .addOnCanceledListener {
                            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
                        }
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                }
            }
            dialog.show()
        }
    }

    override fun OnUpdate(notesDataClass: NotesDataClass, position: Int) {
        var dialog = Dialog(this)
        var dialogBinding = CustomfabBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialogBinding.btnAdd.setOnClickListener {
            if (dialogBinding.etCustomtitle.text.toString().isNullOrEmpty()) {
                dialogBinding.etCustomtitle.error = "Enter the Title"
            } else if (dialogBinding.etCustomDescription.text.toString().isNullOrEmpty()) {
                dialogBinding.etCustomDescription.error = "Enter the Description"
            } else {
               /* notesList.set(
                    position,
                    NotesDataClass(
                        title = dialogBinding.etCustomtitle.text.toString(),
                        description = dialogBinding.etCustomDescription.text.toString()
                    )
                )*/
                firestore.collection("mynotes").document(notesDataClass.id ?: " ")
                    .set( NotesDataClass(
                        title = dialogBinding.etCustomtitle.text.toString(),
                        description = dialogBinding.etCustomDescription.text.toString()
                    ))
                    .addOnSuccessListener {
                        getCollectionData()
                        Toast.makeText(this,"SuccessUpdate",Toast.LENGTH_SHORT).show()
                     }
                    .addOnFailureListener{
                        Toast.makeText(this,"FailuresUpdate",Toast.LENGTH_SHORT).show()
                    }
                    .addOnCanceledListener {
                        Toast.makeText(this,"CancelledUpdate",Toast.LENGTH_SHORT).show()
                    }

                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }
        }
        dialog.show()


    }

    override fun OnDelete(notesDataClass: NotesDataClass, position: Int) {
        firestore.collection("mynotes").document(notesDataClass.id ?: " ")
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this,"Success Delete",Toast.LENGTH_SHORT).show()
                getCollectionData()
            }
            .addOnFailureListener{
                Toast.makeText(this,"Failure Delete",Toast.LENGTH_SHORT).show()
            }
            .addOnCanceledListener {
                Toast.makeText(this,"Cancelled Delete",Toast.LENGTH_SHORT).show()
            }


    }

    fun getCollectionData() {
        notesList.clear()
        firestore.collection("mynotes").get()
            .addOnSuccessListener {
                for (items in it.documents) {
                    var firestoreClass =
                        items.toObject(NotesDataClass::class.java) ?: NotesDataClass()
                    firestoreClass.id = items.id
                    notesList.add(firestoreClass)


                }
                adapter.notifyDataSetChanged()

            }
    }
}