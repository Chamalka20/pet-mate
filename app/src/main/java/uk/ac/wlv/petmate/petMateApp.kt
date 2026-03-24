package uk.ac.wlv.petmate

import android.app.Application
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.osmdroid.config.Configuration
import uk.ac.wlv.petmate.di.viewModelModule

class PetMateApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        Configuration.getInstance().load(
            this,
            getSharedPreferences("osmdroid", MODE_PRIVATE)
        )
        startKoin {
            androidContext(this@PetMateApp)
            modules(viewModelModule)
        }
    }
}