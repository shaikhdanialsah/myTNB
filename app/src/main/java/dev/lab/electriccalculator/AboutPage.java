package dev.lab.electriccalculator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.time.*;




public class AboutPage extends AppCompatActivity {

    TextView copyRighttext;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_page);
        copyRighttext = findViewById(R.id.copyRightText);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("About Developer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int copyrightSymbolCodePoint = 169 ;
        String s = Character.toString((char) copyrightSymbolCodePoint);

        Year thisYear = Year.now();
        copyRighttext.setText(s+thisYear+" "+"MyTNB. All Rights Reserved");



    }
}
