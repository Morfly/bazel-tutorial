package com.morfly.bazel


class KotlinLibrary : Library {

    override val about = "Kotlin library. Version $VERSION"


    companion object {
        private const val VERSION = "0.0.1"
    }
}