import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.eurogas.api.ApiService
import com.example.eurogas.api.GasStationApiConfig
import com.example.eurogas.repositories.GasStationRepository

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AppContainer(context: Context) {

    private val GasStationApiService = GasStationApiConfig.provideRetrofit().create(ApiService::class.java)

    val GasStationRepository: GasStationRepository = GasStationRepository(GasStationApiService)

}