package com.ridhamsharma.crudfirebase

interface NotesInterface {
    fun OnUpdate(notesDataClass: NotesDataClass,position : Int)
    fun OnDelete(notesDataClass: NotesDataClass,position: Int)
}