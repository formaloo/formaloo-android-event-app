package com.formaloo.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.formaloo.local.convertor.FormConverters
import com.formaloo.local.dao.*
import com.formaloo.model.boards.block.Block
import com.formaloo.model.boards.board.Board
import com.formaloo.model.form.Form
import com.formaloo.model.local.*


@Database(
    entities = [Board::class, Form::class, Block::class, Speaker::class, Sponsor::class, News::class, NewsKeys::class, TimeLine::class, TimeLineKeys::class, FAQ::class, FAQKeys::class, SponsorsKeys::class, SpeakersKeys::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(FormConverters::class)
abstract class FormBuilderDB : RoomDatabase() {
    // DAO
    abstract fun boardDao(): BoardDao
    abstract fun formDao(): FormDao
    abstract fun blockDao(): BlockDao
    abstract fun speakerDao(): SpeakerDao
    abstract fun sponsorDao(): SponsorDao
    abstract fun newsDao(): NewsDao
    abstract fun faqDao(): FAQDao
    abstract fun faqKeysDao(): FAQKeysDao
    abstract fun newsKeysDao(): NewsKeysDao
    abstract fun sponsorKeysDao(): SponsorKeysDao
    abstract fun speakerKeysDao(): SpeakerKeysDao
    abstract fun timeLineDao(): TimeLineDao
    abstract fun timeLineKeysDao(): TimeLineKeysDao


    companion object {

        fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                FormBuilderDB::class.java,
                "AppUI.db"
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}
