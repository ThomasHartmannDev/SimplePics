

plugins {
    id("com.android.application") version "8.3.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.android.library") version "8.3.2" apply false
    kotlin("plugin.serialization") version "1.8.10" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
    alias(libs.plugins.google.gms.google.services) apply false
}