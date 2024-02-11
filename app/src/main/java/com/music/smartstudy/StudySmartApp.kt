package com.music.smartstudy

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp  // we are telling hilt to generate required code for dependency injection
class StudySmartApp:Application()