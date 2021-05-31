package com.morfly.bazel;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_v2);

        Button showDescriptionButton = findViewById(R.id.showDescriptionButton);
        TextView libraryDescriptionTextView = findViewById(R.id.libraryDescriptionTextView);

        Library library = new KotlinLibrary();

        showDescriptionButton.setOnClickListener(v ->
                libraryDescriptionTextView.setText(Utils.formattedLibraryDescription(library))
        );
    }
}
