package com.example.anger.readfromdatabase;

public class Asteroid {
    public String id, name, date;
    public int dia, distance, vel;

    public Asteroid() {
//        this.id = id;
//        this.name = name;
//        this.date = date;
//        this.dia = diameter;
//        this.distance = distance;
//        this.vel = velocity;
    }

//    public static String getId() {
//        return id;
//    }
//
//    public static String getName() {
//        return name;
//    }
//
//    public static String getDate() {
//        return date;
//    }
//
//    public static int getDiameter() {
//        return dia;
//    }
//
//    public static int getDistance() {
//        return distance;
//    }
//
//    public static int getVel() {
//        return vel;
//    }
//
    public String toString() {
        return id + " " + name + " " + String.valueOf(dia) + " " + String.valueOf(distance) + " " + String.valueOf(vel) + " " + date;
    }
}
