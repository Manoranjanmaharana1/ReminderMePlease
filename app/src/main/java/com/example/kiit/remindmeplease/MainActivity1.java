package com.example.kiit.remindmeplease;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TreeSet;

public class MainActivity1 extends AppCompatActivity {

    private FloatingActionButton fb;
    private DatabaseHandler dbhandler;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        dbhandler = new DatabaseHandler(this,null,null,1);
        printList();
    }

    protected void printList() {
        TreeSet<String> events1 = new TreeSet<>();
        Log.e("Hello",Integer.toString(events1.size()));
        if (!events1.isEmpty())
        Log.e("Hello",events1.first());
        TreeSet<String> events2 = new TreeSet<>();
        TreeSet<String> events3 = new TreeSet<>();
        updateTreeset(events1,events2,events3);
        if (!events1.isEmpty())
        Log.e("Hello",events1.first());
        List<String> list1 = new ArrayList<>(events1);
        List<String> list2 = new ArrayList<>(events2);
        List<String> list3 = new ArrayList<>(events3);
        ListAdapter listAdapter1 = new CustomAdapter(this,list1);
        ListAdapter listAdapter2 = new CustomAdapter(this,list2);
        ListAdapter listAdapter3 = new CustomAdapter(this,list3);
        ListView a,b,c;
        a = findViewById(R.id.list);
        b = findViewById(R.id.list1);
        c = findViewById(R.id.list2);
        a.setAdapter(listAdapter1);
        b.setAdapter(listAdapter2);
        c.setAdapter(listAdapter3);
        setDynamicHeight(a);
        setDynamicHeight(b);
        setDynamicHeight(c);
        fb = findViewById(R.id.fb);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeInput();
            }
        });

    }

    public void updateTreeset(TreeSet<String> e1,TreeSet<String> e2,TreeSet<String> e3){
        TreeSet<String> events1 = e1;
        TreeSet<String> events2 = e2;
        TreeSet<String> events3 = e3;
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
        Log.e("Hello",Integer.toString(e1.size())+"there");
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

    @Override
    protected void onResume() {
        super.onResume();
        printList();
    }

    public void takeInput(){
        Intent intent = new Intent(MainActivity1.this,MainActivity.class);
        startActivity(intent);
    }
    public static void setDynamicHeight(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        //check adapter if null
        if (adapter == null) {
            return;
        }
        int height = 0;
        int count = adapter.getCount();
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < count; i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            height += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        layoutParams.height = height + (listView.getDividerHeight() * (count));
        listView.setLayoutParams(layoutParams);
        listView.requestLayout();
    }
}
