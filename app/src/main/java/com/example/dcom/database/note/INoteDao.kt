package com.example.dcom.database.note

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface INoteDao {
    @Query("SELECT * FROM note")
    fun getAll(): List<Note>

    @Query("SELECT * FROM note WHERE id IN (:noteIds)")
    fun loadAllByIds(noteIds: IntArray): List<Note>

    @Insert
    fun insert(vararg users: Note)

    @Query("UPDATE note SET title = :title, content = :content WHERE id = :id")
    fun update(title: String, content: String, id: Int)

    // get note by id
    @Query("SELECT * FROM note WHERE id = :id")
    fun get(id: Int): Note

    @Query("SELECT * FROM note ORDER BY id DESC LIMIT 1")
    fun getLatestNote(): Note

    @Query("SELECT id FROM note ORDER BY id DESC LIMIT 1")
    fun getLatestNoteId(): Int

    @Delete
    fun delete(user: Note)

    @Query("DELETE FROM note WHERE id = :id")
    fun deleteById(id: Int)

    // delete all note
    @Query("DELETE FROM note")
    fun deleteAll()

}
