package com.morfly.bazel

import org.junit.Assert
import org.junit.Test


class KotlinLibraryTest {

    @Test
    fun aboutTest() {
        // Given
        val kotlinLibrary = KotlinLibrary()

        // When
        val result = kotlinLibrary.about

        // Then
        val expectedResult = "Kotlin library. Version 0.0.1"
        Assert.assertEquals(expectedResult, result)
    }
}