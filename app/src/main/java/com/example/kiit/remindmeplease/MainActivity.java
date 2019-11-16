package com.example.kiit.remindmeplease;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private EditText event;
    private TextView time,date;
    private Calendar calendar;
    int day,month1,year1,hh,mm;
    private ImageButton cal,clock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        event = findViewById(R.id.event);
        time = findViewById(R.id.time);
        date = findViewById(R.id.date);
        cal = findViewById(R.id.cal);
        clock = findViewById(R.id.clock);
        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month1 = calendar.get(Calendar.MONTH);
        year1 = calendar.get(Calendar.YEAR);
        hh = calendar.get(Calendar.HOUR_OF_DAY);
        mm = calendar.get(Calendar.MINUTE);
        month1+=1;
        date.setText((day<10?"0"+day:day)+"/"+(month1<10?"0"+month1:month1)+"/"+year1);
        time.setText((hh>12?(hh-12<10?"0" + (hh-12) : hh-12) :(hh<10?"0" + (hh) : hh))+":"+ (mm<10?"0" + mm : mm)+""+(calendar.get(Calendar.AM_PM)==0?"AM":"PM") );
        cal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DatePickerDialog dp = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month+=1;
                        day = dayOfMonth;
                        month1 = month;
                        year1 = year;
                        date.setText((dayOfMonth<10?"0"+dayOfMonth:dayOfMonth)+"/"+(month<10?"0"+month:month)+"/"+year);
                    }
                },year1,month1-1,day);
                dp.show();
            }
        });
        clock.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TimePickerDialog tp = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String AM_PM = hourOfDay>12?"PM":"AM";
                        hh = hourOfDay;
                        if(hourOfDay>12)
                            hh-=12;

                        mm = minute;
                        time.setText((hh<10?"0"+hh:hh)+":"+ (minute<10?"0" + minute : minute)+""+AM_PM);
                    }
                },hh,mm,false);
                tp.show();
            }
        });
        final DatabaseHandler dbhandler = new DatabaseHandler(this,null,null,1);
        Button set = findViewById(R.id.set);
        set.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(event.getText().toString().compareTo("")==0)
                    Toast.makeText(MainActivity.this,"Please Set an Event!!",Toast.LENGTH_SHORT).show();
                else {
                    dbhandler.addEvents(event.getText().toString(),date.getText().toString(),time.getText().toString());
                    Intent i = new Intent(MainActivity.this,MainActivity1.class);
                    //startAlarmListener(day,month1,year1,hh,mm);
                    startActivity(i);
                    finishAffinity();

                }

            }
        });
    }
    //private void startAlarmListener(int Day,int Month,int Year,int Hour,int Minute){


}
