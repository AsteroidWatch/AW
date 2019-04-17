package com.aw.awnew;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
public class SearchActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private boolean startDateSet = false;
    private boolean endDateSet = false;
    private TextView sDateTextView;
    private TextView eDTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Variables
        final Button startDateButton = (Button) findViewById(R.id.startDateButton);
        Button endDateButton = (Button) findViewById(R.id.endDateButton);

        sDateTextView = (TextView) findViewById(R.id.startDateTextView);

        //Button Listeners
        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
                startDateSet = true;
            }
        });

        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
                endDateSet = true;
            }
        });

        if (savedInstanceState != null) {
            //sDateTextView = savedInstanceState.getString("start date");
            sDateTextView.setText(String.valueOf("start date"));
        }

    }


    @NonNull
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        if(startDateSet) {
            TextView textView = (TextView) findViewById(R.id.startDateTextView);
            textView.setText("Start date: " + currentDateString);
            sDateTextView = textView;
            startDateSet = false;
        }

        if(endDateSet) {
            TextView textView = (TextView) findViewById(R.id.endDateTextView);
            textView.setText("End date: " + currentDateString);
            endDateSet = false;

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){

        outState.putString("start_date", sDateTextView.getText().toString());
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){

        super.onRestoreInstanceState(savedInstanceState);

        sDateTextView.setText(savedInstanceState.getString("start date"));

    }

}
