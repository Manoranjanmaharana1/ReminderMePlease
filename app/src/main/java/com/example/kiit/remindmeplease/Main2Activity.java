package com.example.kiit.remindmeplease;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;
import java.util.TreeSet;

public class Main2Activity extends AppCompatActivity {

    private TextView desc;
    private FloatingActionButton btn;
    private TextToSpeech tts;
    private DatabaseHandler dbhandler;
    String s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //Bundle b = getIntent().getExtras();

        final Window win= getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        s = getIntent().getStringExtra("desc");
        dbhandler = new DatabaseHandler(this,null,null,1);
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
        desc = findViewById(R.id.desc);
        desc.setText(s);
        btn = findViewById(R.id.fab);
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    int res = tts.setLanguage(Locale.ENGLISH);
                    if (res == TextToSpeech.LANG_MISSING_DATA ||
                            res == TextToSpeech.LANG_NOT_SUPPORTED){}
                            else{
                        tts.speak("Here is the important Reminder for you. "+s,TextToSpeech.QUEUE_FLUSH,null);
                        updateTreeset();
                    }
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mclose();
    }

    void mclose(){
        if(tts!=null){
            tts.stop();
            tts.shutdown();
        }
        System.exit(0);
    }

    public void updateTreeset(){
        TreeSet<String> events1 = new TreeSet<>();
        TreeSet<String> events2 = new TreeSet<>();
        TreeSet<String> events3 = new TreeSet<>();;
        Log.e("Hello",Integer.toString(events1.size())+"here");
        String eventsdata = dbhandler.getData();
        //Log.d("DATA",data);
        int index=0;
        String info = "";
        int j;
        if(eventsdata.compareTo("")!=0)
        {
            for(int i=0;i<eventsdata.length();i++)
                if(eventsdata.charAt(i)=='\n'){
                    info = eventsdata.substring(index,i-1);
                    j = compareDates(info);
                    Log.d("Data",info+"  "+j);
                    if(j==0)
                        events1.add(info);
                    else if(j<0)
                        events2.add(info);
                    else if(j==1) events3.add(info);
                    index = i+1;
                }
        }
        if(!events1.isEmpty())
            startAlarmListener(events1.first());
        else if(!events2.isEmpty())
            startAlarmListener(events2.first());
        else if(!events3.isEmpty())
            startAlarmListener(events3.first());
        //Log.e("Hello",Integer.toString(e1.size())+"there");
    }

    private void startAlarmListener(String first) {
        String []details = first.split("/");
        int Day = Integer.parseInt(details[0]);
        int Month = Integer.parseInt(details[1]);
        int Year = Integer.parseInt(details[2].substring(0,4));
        Month-=1;
        int Hour = Integer.parseInt(details[2].substring(5,7));
        int Minute = Integer.parseInt((details[2].substring(8,10)));
        String AM_PM = details[2].substring(10,12);
        if(AM_PM.compareTo("PM")==0)
            Hour = Hour + 12;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Year,Month,Day,Hour,Minute,0);
        Intent intent = new Intent(getApplicationContext(),Notify.class);
        intent.putExtra("Event",details[2].substring(13,details[2].length())+"#"+details[2].substring(5,7)+":"+details[2].substring(8,10)+AM_PM);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),4488,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),1000*60*2,pendingIntent);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis()-300*1000,pendingIntent);

    }

    public int compareDates(String date){
        int day = Integer.parseInt(date.substring(0,2));
        int month = Integer.parseInt(date.substring(3,5));
        int year = Integer.parseInt(date.substring(6,10));
        Log.d("Data",day+" "+month+" "+year);
        Calendar c = Calendar.getInstance();
        int d = c.get(Calendar.DAY_OF_MONTH);
        int m = c.get(Calendar.MONTH);
        m+=1;
        int y = c.get(Calendar.YEAR);
        Log.d("Data",d+" "+m+" "+y);
        if(day == d && m == month && y == year)
            return 0;
        else if(day-d==1 && m == month && y == year)
            return -1;
        else if(day-d>1 && (m == month || m<month) && (y == year || y<year))
            return 1;
        return 2323; //unique code
    }
}
