package com.aw.awnew;

import android.os.AsyncTask;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

public class Asteroids{

    static String API_KEY = "uxickliHQnKSJqa7sl3gfdWt6Fw1Oct7rzzxDzHB";




    static String urlBaseString = "https://api.nasa.gov/neo/rest/v1/feed?";




    static ObjectMapper mapper = new ObjectMapper();

    public static LocalDate startDate;
    public static LocalDate endDate;

    public static String getAsteroidsRootJSON(String myURL) {
        String urlString = myURL;
        // urlBaseString + "start_date=" + start_date + "&" + "end_date=" + end_date + "&" + "api_key=" + API_KEY;
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setDoInput(true);
            //BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
            InputStream is = con.getInputStream();
            //String inputLine = readInputString(is);
            StringBuffer response = readInputString(is);
//            while ((inputLine != null)) {
//                response.append(inputLine);
//            }
            //rd.close();
            return response.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static StringBuffer readInputString(InputStream stream) throws IOException {
        int n = 0;
        InputStreamReader reader = new InputStreamReader(stream, "UTF8");
        BufferedReader br = new BufferedReader(reader);
        StringBuffer response = new StringBuffer();
        while(br.readLine() != null) {
            response.append(br.readLine());
        }
        br.close();
        return response;
    }

    public static JsonNode getDateFields(String jsonAsteroidObjectHierarchy) {
        try {
            return mapper.readTree(jsonAsteroidObjectHierarchy).get("near_earth_objects");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<JsonNode> getAsteroidsJSON(JsonNode nearEarthObjectsJSON) {
        ArrayList<JsonNode> jsonNodes = new ArrayList<JsonNode>();
        for (JsonNode dateField : nearEarthObjectsJSON) {
            for (JsonNode asteroidField : dateField) {
                jsonNodes.add(asteroidField);
            }
        }
        return jsonNodes;
    }

    public static ArrayList<AsteroidFetch> getAsteroids(ArrayList<JsonNode> jsonNodes) {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ArrayList<AsteroidFetch> asteroids = new ArrayList<AsteroidFetch>();
        for (JsonNode jsonNode : jsonNodes) {
            try {
                asteroids.add(mapper.readValue(jsonNode.toString(), AsteroidFetch.class));
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return asteroids;
    }

    public static void setStartDate(String sDate) {
        startDate = LocalDate.parse(sDate);

    }

    public static void setEndDate(String eDate) {
        endDate = LocalDate.parse(eDate);
    }

    public static LocalDate getStartDate() {
        return startDate;
    }

    public static LocalDate getEndDate() {
        return endDate;
    }

    public static ArrayList<AsteroidFetch> returnAsteroids(LocalDate sDate, LocalDate eDate)  {
        // TODO Auto-generated method stub
        Instant start = Instant.now();
        //System.out.println("Start time: " + Calendar.getInstance().getTime());
        LocalDate START_DATE = sDate;
        LocalDate END_DATE = eDate;
        //System.out.println(daysOverall(START_DATE, END_DATE));
        ArrayList<AsteroidFetch> asteroids = roids(START_DATE, END_DATE);
//		AsteroidFetch[] asteroidArr = asteroids.toArray(new AsteroidFetch[asteroids.size()]);
//		Arrays.sort(asteroidArr, new SortBySize());

        //System.out.println("A list of info of near-Earth Asteroids (NEAs) between " +  START_DATE + " and " + END_DATE);

//		for (Asteroid asteroid : asteroidArr) {
//			System.out.println(asteroid);
//		}

//		for(Asteroid asteroid : asteroids) {
//			System.out.println(asteroid);
//		}

        //Print out information regarding asteroids
        //System.out.println("Number of asteroids:" + asteroids.size());
        Instant end = Instant.now();
        //System.out.println("End time: " + Calendar.getInstance().getTime());
        long difference = Duration.between(start, end).toMillis();
        //System.out.println("Time taken: " + (difference/1000) / 60 + "min "  + (difference/1000) % 60 + " seconds");
        return asteroids;
    }

    public static ArrayList<AsteroidFetch> roids(LocalDate startDate, LocalDate endDate) {
        //Variables
        ArrayList<AsteroidFetch> ast = new ArrayList<>();
        int numberOfDays = daysOverall(startDate, endDate);
        int numberOfWeeks = numberOfDays / 7;
        int leftover = numberOfDays % 7;

        //System.out.println("Weeks: " + numberOfWeeks + " Leftover: " + leftover);

        if(numberOfWeeks == 0) {
            ast.addAll(getAsteroids(getAsteroidsJSON(getDateFields(getAsteroidsRootJSON(urlBaseString + "start_date=" + startDate + "&" + "end_date=" + endDate + "&" + "api_key=" + API_KEY)))));
        } else if(numberOfWeeks > 0) {
            if(leftover > 0) {
                LocalDate leftoverDays = endDate.minusDays(leftover);
                ast.addAll(getAsteroids(getAsteroidsJSON(getDateFields(getAsteroidsRootJSON(urlBaseString + "start_date=" + leftover + "&" + "end_date=" + endDate + "&" + "api_key=" + API_KEY)))));
            }
            LocalDate s = startDate;
            LocalDate e = startDate.plusDays(7);

            for(int i = 0; i < numberOfWeeks; i++) {
                ast.addAll(getAsteroids(getAsteroidsJSON(getDateFields(getAsteroidsRootJSON(urlBaseString + "start_date=" + s + "&" + "end_date=" + e + "&" + "api_key=" + API_KEY)))));
                s = s.plusDays(7);
                e = e.plusDays(7);
            }

        }

        return ast;
    }

    public static int daysOverall(LocalDate s, LocalDate e) {
        int days = 0;

        while(!s.isEqual(e)) {
            days++;
            s = s.plusDays(1);
        }

        return days;
    }
}

class SortBySize implements Comparator<AsteroidFetch> {

    public int compare(AsteroidFetch a, AsteroidFetch b) {
        return Math.round(a.estimatedDiameter) - Math.round(b.estimatedDiameter);
    }
}