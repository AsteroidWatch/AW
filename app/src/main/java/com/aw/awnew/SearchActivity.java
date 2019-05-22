package com.aw.awnew;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class SearchActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
        private boolean startDateSet = false;
        private boolean endDateSet = false;
        private TextView sDateTextView;
        private TextView eDTextView, check;
        private String startdatee = " ";
        private String enddatee = " ";
        static ArrayList<Asteroid> asteroids = new ArrayList<>();
        private LocalDate startDate, endDate;
        private FirebaseAuth mAuth;
        private FirebaseUser currentUser;
//    private static String API_KEY = "uxickliHQnKSJqa7sl3gfdWt6Fw1Oct7rzzxDzHB";
//    private static String urlBaseString = "https://api.nasa.gov/neo/rest/v1/feed?";
//    private String url = urlBaseString + "start_date=" + startDate + "&" + "end_date=" + endDate + "&" + "api_key=" + API_KEY;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_search);

            mAuth = FirebaseAuth.getInstance();
            currentUser = mAuth.getCurrentUser();

            //Variables
            final Button startDateButton = (Button) findViewById(R.id.startDateButton);
            Button endDateButton = (Button) findViewById(R.id.endDateButton);
            final Button searchButton = (Button) findViewById(R.id.button);
            sDateTextView = (TextView) findViewById(R.id.startDateTextView);
            eDTextView = (TextView) findViewById(R.id.endDateTextView);

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


                sDateTextView.setText(savedInstanceState.getString("start_date"));
                eDTextView.setText(savedInstanceState.getString("end_date"));
            }

            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    asteroids = readSpecificDate(getApplicationContext(), startDate, endDate);
                    //check.setText("" + asteroids.size());
                    if (asteroids.size() > 0) {
                        Toast toast = Toast.makeText(SearchActivity.this, "Got all asteroids", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    Intent intent = new Intent(SearchActivity.this, ComparisonActivity.class);
                    startActivity(intent);

                }
            });


        }


        @NonNull
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
            String m, d;

            if (month < 10) {
                m = "0" + month;
            } else {
                m = "" + month;
            }

            if (dayOfMonth < 10) {
                d = "0" + dayOfMonth;
            } else {
                d = "" + dayOfMonth;
            }
            String sDate = year + "-" + m + "-" + d;

            if (startDateSet) {
                TextView textView = (TextView) findViewById(R.id.startDateTextView);
                textView.setText("Start date: " + currentDateString);
                startDate = LocalDate.parse(sDate);
                startDateSet = false;
            }

            if (endDateSet) {
                TextView textView = (TextView) findViewById(R.id.endDateTextView);
                textView.setText("End date: " + currentDateString);
                endDate = LocalDate.parse(sDate);
                endDateSet = false;

            }
        }

        @Override
        protected void onSaveInstanceState(Bundle outState) {

            outState.putString("start_date", sDateTextView.getText().toString());
            outState.putString("end_date", eDTextView.getText().toString());
            super.onSaveInstanceState(outState);

        }

        @Override
        protected void onRestoreInstanceState(Bundle savedInstanceState) {

            super.onRestoreInstanceState(savedInstanceState);

            startdatee = savedInstanceState.getString("start_date", "Didnt work");
            enddatee = savedInstanceState.getString("end_date", "Didnt work");
        }

        public ArrayList<Asteroid> readSpecificDate(Context context, LocalDate s, LocalDate e) {
            DataAdapter mDbHelper = new DataAdapter(context);
            mDbHelper.createDatabse();
            mDbHelper.open();
            ArrayList<Asteroid> al = mDbHelper.getAsteroids(s, e);
            mDbHelper.close();

            return al;
        }
    }
