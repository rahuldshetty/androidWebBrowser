package com.webb.ajp.webbrowser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class History extends AppCompatActivity {

    ListView historyList;

    ArrayList<HistoryDate> datas;

    HistoryAdapter adapter;

    SQLiteDatabase mydatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyList=findViewById(R.id.historyList);

        datas=new ArrayList<HistoryDate>();

        adapter=new HistoryAdapter(this,R.layout.bookmark_single,datas);

        historyList.setAdapter(adapter);

        mydatabase= openOrCreateDatabase("BROWESERDB",MODE_PRIVATE,null);

        Cursor resultSet = mydatabase.rawQuery("Select  distinct title, url , times  from HISTORY order by times desc ;",null);

        adapter.clear();
        datas.clear();
        try{
            while(resultSet.moveToNext())
            {

                String  temp =resultSet.getString(2);

                Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(temp);
                String day = new SimpleDateFormat("EEEE").format(d) +" " + new SimpleDateFormat("HH:mm").format(d);

                HistoryDate data = new HistoryDate(day,resultSet.getString(1),resultSet.getString(0));


                datas.add(data);


            }
        }
        catch(Exception e)
        {
            resultSet.close();
        }
        adapter.notifyDataSetChanged();

        historyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent mc=new Intent(History.this,MainActivity.class);

                HistoryDate dt = (HistoryDate) adapterView.getAdapter().getItem(i);

                mc.putExtra("URLfromTAB",dt.getUrl());
                int totalFrags = CacheClass.getSize();
                mc.putExtra("SELECTEDFRAG",totalFrags);
                mc.putExtra("TOADD",true);


                startActivity(mc);
                MainActivity.refreshTabCount();
                finish();


            }
        });


    }

    public class HistoryAdapter extends ArrayAdapter<HistoryDate> {

        Context mContext;

        int layoutResourceId;

        ArrayList<HistoryDate> data = null;

        public HistoryAdapter(@NonNull Context context, int resource, @NonNull ArrayList<HistoryDate> objects) {
            super(context, resource, objects);
            mContext=context;
            layoutResourceId=resource;
            data=objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView==null)
            {
                LayoutInflater inflater=((Activity)mContext).getLayoutInflater();
                convertView=inflater.inflate(layoutResourceId,parent,false);
            }

            HistoryDate obj=data.get(position);

            TextView title=convertView.findViewById(R.id.bookmarkTitle);
            TextView desc=convertView.findViewById(R.id.bookmarkDesc);
            TextView timestamp=convertView.findViewById(R.id.timestamp);

            timestamp.setVisibility(View.VISIBLE);
            title.setText(obj.getTitle());
            desc.setText(obj.getUrl());
            timestamp.setText(obj.getTimestamp());




            return convertView;
        }
    }





}
