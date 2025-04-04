package com.kanoyatech.snapdex

import android.app.Application
import com.kanoyatech.snapdex.di.authModule
import com.kanoyatech.snapdex.di.dataLocalModule
import com.kanoyatech.snapdex.di.dataModule
import com.kanoyatech.snapdex.di.dataRemoteModule
import com.kanoyatech.snapdex.di.domainModule
import com.kanoyatech.snapdex.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class SnapdexApp: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@SnapdexApp)
            modules(
                dataLocalModule,
                dataRemoteModule,
                dataModule,
                uiModule,
                authModule,
                domainModule
            )
        }
    }
}