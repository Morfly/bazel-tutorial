package com.morfly.bazel

import android.content.Context


private const val VERSION = "0.1.0"


class AndroidLibrary(context: Context): Library {

    private val applicationContext = context.applicationContext

    override val about =
            "${applicationContext.packageName}: Android library. Version $VERSION"
}