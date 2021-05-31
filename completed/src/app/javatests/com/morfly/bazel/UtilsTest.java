package com.morfly.bazel;


import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class UtilsTest {

    @Test
    public void formatLibraryDescriptionTest() {
        // Given
        String aboutTestLibrary = "Test library. Version 0.0.0";
        Library library = () -> aboutTestLibrary;

        // When
        String result = Utils.formattedLibraryDescription(library);

        // Then
        String expectedResult = "About library:\n" + aboutTestLibrary;
        assertEquals(expectedResult, result);
    }
}