package com.example.runnablehandler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    int number;
    Runnable runnable;
    Handler handler;
    Button buttonStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonStart = findViewById(R.id.buttonStart);

        textView = findViewById(R.id.textView);
        number = 0;

    }

    public void start(View view){//bir sayaç yapmaya çalışıyoruz bu sayaçta 1er saniye bkelemesi için thread.sleep() fonksiyonu uyguladık
                                 //uygulayınca bir hata ile karılaştık çünübiz arayüz threadini kilitlemiş olduk ve bunu yapuınca proghram tamamen kilitlendi
                                 //ve aarayüz çalışmamaya başladı ve hata mesajları aldık. Böyle bir işlemde buluncağımız zaman sayacı aryüzü kilitlemeyecek
                                 //şekilde farklı bir thread içerisinden kullanmamız gerek
        /*while(number < 100){
            textView.setText("Time :" + number);
            number++;
            textView.setText("Time :" + number);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }*/
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                textView.setText("Time :" + number);
                number++;
                textView.setText("Time :" + number);

                handler.postDelayed(runnable,1000);
            }

        };

        handler.post(runnable);//bu sefer de buton her tıklandığında tekrar
                                //runnable çalışıyor ve sayacı hızlandırıyor
                                //gereksiz bir şekilde
        buttonStart.setEnabled(false);//tıklandıktan sonra tıklamayı devre dışı bıraktık



    }

    public void stop(View view){
        buttonStart.setEnabled(true);

        handler.removeCallbacks(runnable);

        number = 0;

        textView.setText("Time : "+number);

        Toast.makeText(this, "Timer reset", Toast.LENGTH_SHORT).show();


    }
}