package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            SQLiteDatabase db = this.openOrCreateDatabase("Musicians",MODE_PRIVATE,null);
            db.execSQL("create table if not exists musicians (id INTEGER PRIMARY KEY,name VARCHAR,age INTEGER)");//id primary key olarak belirlerken int yazarsan çalışmaz
            //db.execSQL("insert into musicians (name,age) values ('Jake',19)");
            //db.execSQL("insert into musicians (name,age) values ('Lars',60)");
            //db.execSQL("insert into musicians (name,age) values ('Kirk',15)");
            //create table if not exists mytable (id INT PRIMARY KEY, name VARCHAR, age INT);// Bu tablo oluşup da içine veri eklenince veriye
            // id verilmese bile id otomatik artar primary key olduğu için

            Cursor cursor = db.rawQuery("select * from musicians",null);//selection args kısmı filtre için kullanuılıyormuş
            int idIx = cursor.getColumnIndex("id");
            int nameIx =  cursor.getColumnIndex("name");
            int ageIx = cursor.getColumnIndex("age");

            while (cursor.moveToNext()){
                System.out.println("id : "+ cursor.getInt(idIx) );
                System.out.println("Name : "+ cursor.getString(nameIx) );
                System.out.println("Age : "+ cursor.getInt(ageIx) );
            }
            cursor.close();

        }catch (Exception e){
            e.printStackTrace();

        }
    }
}