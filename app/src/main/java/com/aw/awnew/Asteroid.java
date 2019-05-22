package com.aw.awnew;

import android.os.Parcel;
import android.os.Parcelable;

public class Asteroid implements Parcelable {
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

    protected Asteroid(Parcel in) {
        id = in.readString();
        name = in.readString();
        date = in.readString();
        dia = in.readInt();
        distance = in.readInt();
        vel = in.readInt();
    }

    public static final Creator<Asteroid> CREATOR = new Creator<Asteroid>() {
        @Override
        public Asteroid createFromParcel(Parcel in) {
            return new Asteroid(in);
        }

        @Override
        public Asteroid[] newArray(int size) {
            return new Asteroid[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(date);
        dest.writeInt(dia);
        dest.writeInt(distance);
        dest.writeInt(vel);
    }
}
