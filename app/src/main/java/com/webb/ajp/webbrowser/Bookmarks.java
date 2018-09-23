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

import java.util.ArrayList;
import java.util.List;

public class Bookmarks extends AppCompatActivity {

    public static ListView bookmarklistview;
    public static BookmarkAdapter Badapter;
    public ArrayList<BookmarkData> datas;
    public SQLiteDatabase mydatabase ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);
        mydatabase= openOrCreateDatabase("BROWESERDB",MODE_PRIVATE,null);
        bookmarklistview=findViewById(R.id.bookmarkList);

        datas=new ArrayList<BookmarkData>();


        Badapter=new BookmarkAdapter(this,R.layout.bookmark_single,datas);

        bookmarklistview.setAdapter(Badapter);

        Cursor resultSet = mydatabase.rawQuery("Select * from BOOKMARKS;",null);



        Badapter.clear();
        datas.clear();
        try{
            while(resultSet.moveToNext())
            {

            BookmarkData data = new BookmarkData( resultSet.getString(1),resultSet.getString(0) );
            datas.add(data);


            }
        }
        catch(Exception e)
        {
            resultSet.close();
        }
        Badapter.notifyDataSetChanged();

        bookmarklistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent mc=new Intent(Bookmarks.this,MainActivity.class);

                BookmarkData dt = (BookmarkData) adapterView.getAdapter().getItem(i);

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

    public class BookmarkAdapter extends ArrayAdapter<BookmarkData> {

        Context mContext;

        int layoutResourceId;

        ArrayList<BookmarkData> data = null;

        public BookmarkAdapter(@NonNull Context context, int resource, @NonNull ArrayList<BookmarkData> objects) {
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

            BookmarkData obj=data.get(position);

            TextView title=convertView.findViewById(R.id.bookmarkTitle);
            TextView desc=convertView.findViewById(R.id.bookmarkDesc);

            title.setText(obj.getTitle());
            desc.setText(obj.getUrl());




            return convertView;
        }
    }


}
