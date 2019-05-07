package com.aw.awnew;

public class Asteroid {

    public static String date, name, vel, id, missDistance, diameter, ph;

    public Asteroid() {}

    public static void setDate(String d) {
        date = d;
    }

    public static void setName(String n) {
        name = n;
    }

    public static void setVel(String v) {
        vel = v;
    }

    public static void setID(String setID) {
        id = setID;
    }

    public static void setMissDistance(String missDist) {
        missDistance = missDist;
    }

    public static void setDiameter(String dia) {
        diameter = dia;
    }

    public static void setPotentiallyHazardous(String potentiallyHaz) {
        ph = potentiallyHaz;
    }

    public static String getDate() {
        return date;
    }

    public static String getName() {
        return name;
    }

    public static String getVel() {
        return vel;
    }

    public static String getID() {
        return id;
    }

    public static String getMissDistance() {
        return missDistance;
    }

    public static String getDiameter() {
        return diameter;
    }

    public static String getPotHaz() {
        return ph;
    }
}
