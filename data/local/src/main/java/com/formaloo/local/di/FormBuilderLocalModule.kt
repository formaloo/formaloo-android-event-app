package com.formaloo.local.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.formaloo.local.FormBuilderDB
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val formBuilderLocalModule = module {
    single() { FormBuilderDB.buildDatabase(androidContext()) }
    factory { (get() as FormBuilderDB).boardDao() }
    factory { (get() as FormBuilderDB).formDao() }
    factory { (get() as FormBuilderDB).blockDao() }
    factory { (get() as FormBuilderDB).newsDao() }
    factory { (get() as FormBuilderDB).faqDao() }
    factory { (get() as FormBuilderDB).faqKeysDao() }
    factory { (get() as FormBuilderDB).speakerDao() }
    factory { (get() as FormBuilderDB).sponsorDao() }
    factory { (get() as FormBuilderDB).sponsorKeysDao() }
    factory { (get() as FormBuilderDB).speakerKeysDao() }
    factory { (get() as FormBuilderDB).newsKeysDao() }
    factory { (get() as FormBuilderDB).timeLineDao() }
    factory { (get() as FormBuilderDB).timeLineKeysDao() }
    single {
        provideSharePreferences(androidApplication())
    }

}

private fun provideSharePreferences(app: Application): SharedPreferences =
    app.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
