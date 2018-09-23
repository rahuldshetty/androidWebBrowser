package com.webb.ajp.webbrowser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class WebTabs extends AppCompatActivity {
    public static ListView tabList;
    ImageView homeBtn;

    public static TabAdapter adapter;

    public static ArrayList<TabData> tabss=new ArrayList<TabData>();;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_tabs);

        homeBtn=findViewById(R.id.homeBtn);

        tabList=findViewById(R.id.tabList);



        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeB=new Intent(WebTabs.this,MainActivity.class);
                homeB.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                MainActivity.curWebFragment=-1;
                startActivity(homeB);
                finish();


            }
        });

        adapter=new TabAdapter(this,R.layout.tab_single,tabss);;



        tabList.setAdapter(adapter);

        tabList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent mc=new Intent(WebTabs.this,MainActivity.class);

                TabData dt = (TabData) adapterView.getAdapter().getItem(i);


                startActivity(mc);
            }
        });


    }

    public static void loadTabs(){
        {
            if(adapter==null)return;

            adapter.clear();
            tabss.clear();
            for(WebFragment w:CacheClass.getWebFragments())
            {

                TabData obj=null;

                obj = new TabData(w.img,w.webtitle,w.url);

                if(obj!=null)
                    tabss.add(obj);


            }
            adapter.notifyDataSetChanged();
        }
    }




    public class TabAdapter extends ArrayAdapter<TabData> {

        Context mContext;

        int layoutResourceId;

        ArrayList<TabData> data = null;

        public TabAdapter(@NonNull Context context, int resource, ArrayList<TabData> objects) {
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

            TabData obj=data.get(position);

            TextView title,desc;
            ImageView img;

            title=convertView.findViewById(R.id.tabTitle);
            desc=convertView.findViewById(R.id.tabdesc);
            img=convertView.findViewById(R.id.tabPic);

            title.setText(obj.getTitle());
            desc.setText(obj.getDesc());
            img.setImageBitmap(obj.getImage());



            return convertView;

        }
    }






}
