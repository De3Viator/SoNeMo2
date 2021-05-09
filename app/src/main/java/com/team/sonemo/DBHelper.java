package com.team.sonemo;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DBHelper {
    private DatabaseReference dbReference;
    private static DBHelper instance;

    public static DBHelper getInstance() {
        if(instance == null){
            instance = new DBHelper();
        }
        return instance;
    }

    private DBHelper(){
        dbReference = FirebaseDatabase.getInstance().getReference();
    }

    public DatabaseReference getChildReference(String childName){
        return dbReference.child(childName);
    }
}
