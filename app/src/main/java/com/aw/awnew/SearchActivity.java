package com.aw.awnew;

import android.app.DatePickerDialog;
import android.content.Context;
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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class SearchActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private boolean startDateSet = false;
    private boolean endDateSet = false;
    private TextView sDateTextView;
    private TextView eDTextView, check;
    private String startdatee = " ";
    private String enddatee = " ";
    ArrayList<Asteroid> asteroids = new ArrayList<>();
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
                check.setText("" + asteroids.size());
                if(asteroids.size() > 0) {
                    Toast.makeText(SearchActivity.this, "Got all asteroids", Toast.LENGTH_SHORT);
                }
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

        if(startDateSet) {
            TextView textView = (TextView) findViewById(R.id.startDateTextView);
            textView.setText("Start date: " + currentDateString);
            startDate = LocalDate.parse(sDate);
            startDateSet = false;
        }

        if(endDateSet) {
            TextView textView = (TextView) findViewById(R.id.endDateTextView);
            textView.setText("End date: " + currentDateString);
            endDate = LocalDate.parse(sDate);
            endDateSet = false;

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){

        outState.putString("start_date", sDateTextView.getText().toString());
        outState.putString("end_date", eDTextView.getText().toString());
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){

        super.onRestoreInstanceState(savedInstanceState);

        startdatee = savedInstanceState.getString("start_date", "Didnt work");
        enddatee = savedInstanceState.getString("end_date", "Didnt work");
    }

    public static ArrayList<Asteroid> readSpecificDate(Context context, LocalDate s, LocalDate e) {
        ArrayList<Asteroid> al = new ArrayList<>();

        while(!s.equals(e)) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open("asteroids.txt")));
                String st;

                while((st = br.readLine()) != null) {
                    if(st.equals("Approach Date: " + s.toString())) {
                        Asteroid asteroid = new Asteroid();
                        int lines = 6;
                        for(int i = 0; i <= lines; i++) {
                            if(i == 0) {
                                asteroid.setDate(st);
                            } else if (i == 1) {
                                asteroid.setName(st);
                            } else if(i == 2) {
                                asteroid.setID(st);
                            } else if(i == 3) {
                                asteroid.setPotentiallyHazardous(st);
                            } else if(i == 4) {
                                asteroid.setDiameter(st);
                            } else if(i == 5) {
                                asteroid.setMissDistance(st);
                            } else if(i == 6) {
                                asteroid.setVel(st);
                            }

                            //System.out.println(st);
                            st = br.readLine();
                        }

                        al.add(asteroid);
                    }
                }

                s = s.plusDays(1);
                br.close();
            } catch (FileNotFoundException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            } catch (IOException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }

            Collections.sort(al, new Comparator<Asteroid>() {
                @Override
                public int compare(Asteroid o1, Asteroid o2) {
                    String n1 = o1.getDiameter().replaceAll("\\D+","");
                    String n2 = o2.getDiameter().replaceAll("\\D+","");
                    return Math.round(Float.parseFloat(n1)) - Math.round(Float.parseFloat(n2));
                }
            });

        }

        return al;
    }
}


