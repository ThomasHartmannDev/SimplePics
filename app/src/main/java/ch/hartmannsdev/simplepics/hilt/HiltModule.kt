package ch.hartmannsdev.simplepics.hilt

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Dagger Hilt module to provide Firebase dependencies.
 *
 * This module is installed in the ViewModelComponent, which means the provided dependencies
 * will be available for injection in ViewModel classes.
 */
@Module
@InstallIn(ViewModelComponent::class)
class HiltModule {

    /**
     * Provides an instance of FirebaseAuth.
     *
     * @return An instance of FirebaseAuth.
     */
    @Provides
    fun provideAuthentication(): FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Provides an instance of FirebaseFirestore.
     *
     * @return An instance of FirebaseFirestore.
     */
    @Provides
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    /**
     * Provides an instance of FirebaseStorage.
     *
     * @return An instance of FirebaseStorage.
     */
    @Provides
    fun provideStorage(): FirebaseStorage = FirebaseStorage.getInstance()
}
