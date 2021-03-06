package com.webb.ajp.webbrowser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Picture;
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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.StackView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class WebTabs extends AppCompatActivity {
   // public static ListView tabList;
    public static StackView tabList;
    ImageView homeBtn;

    public static StackAdapter adapter;


    public static boolean goToMain=false;

    public boolean isRemovedTab = false;
    public int removedTab=-1;

    public static ArrayList<TabData> tabss=new ArrayList<TabData>();;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_tabs);

        homeBtn=findViewById(R.id.homeBtn);

        tabList=findViewById(R.id.stackView);



        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               loadMain();


            }
        });

        adapter=new StackAdapter(this,tabss);;



        tabList.setAdapter(adapter);

        tabList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent mc=new Intent(WebTabs.this,MainActivity.class);

                TabData dt = (TabData) adapterView.getAdapter().getItem(i);

                mc.putExtra("URLfromTAB",dt.getDesc());
                mc.putExtra("SELECTEDFRAG",i);
                mc.putExtra("TOADD",false);
                MainActivity.curWebFragmentIsNewPage=true;



                startActivity(mc);


            }
        });

        loadTabs();

    }

    public static void loadTabs(){
        {
            if(adapter==null)return;


            tabss.clear();

            for(WebFragment w:CacheClass.getWebFragments())
            {

                TabData obj=null;



                obj = new TabData(w.webView.getFavicon(),w.webView.getTitle(),w.webView.getUrl());

                if(obj!=null)
                    tabss.add(obj);


            }
            adapter.notifyDataSetChanged();
        }
    }


    public void loadMain(){
        Intent homeB=new Intent(WebTabs.this,MainActivity.class);
        homeB.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        CacheClass.cureWebFragment=-1;
        startActivity(homeB);
        finish();
    }


    public class StackAdapter extends BaseAdapter{

        ArrayList<TabData> datas=null;
        private Context mContext;

        StackAdapter( Context c , ArrayList<TabData> data){
            mContext=c;
            datas=data;
        }



        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public TabData getItem(int i) {
            return datas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            View grid;
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(goToMain)
            {

                goToMain=false;
                loadMain();
            }


            if (view == null ) {

                grid = new View(mContext);
                grid = inflater.inflate(R.layout.tabstacksingle, null);

                if(tabss.size()==0)
                {
                    grid.setVisibility(View.INVISIBLE);
                    grid.setEnabled(false);
                    return grid ;

                }

                TabData obj=datas.get(position);

                TextView title,desc;
                ImageView img,cls;

                title=grid.findViewById(R.id.tabTitle);
                desc=grid.findViewById(R.id.tabdesc);
                img=grid.findViewById(R.id.tabPic);
                cls=grid.findViewById(R.id.tabClose);




                title.setText(obj.getTitle());
                desc.setText(obj.getDesc());
                img.setImageBitmap(obj.getImage());


                cls.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tabss.remove(position);
                        CacheClass.removeFragment(position);


                        isRemovedTab=true;
                        removedTab=position;
                        MainActivity.refreshTabCount();
                        adapter.notifyDataSetChanged();

                    }
                });


            } else {
                grid = (View) view;
            }



            return grid;
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
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if(convertView==null)
            {
                LayoutInflater inflater=((Activity)mContext).getLayoutInflater();
                convertView=inflater.inflate(layoutResourceId,parent,false);
            }

            TabData obj=data.get(position);

            TextView title,desc;
            ImageView img,cls;

            title=convertView.findViewById(R.id.tabTitle);
            desc=convertView.findViewById(R.id.tabdesc);
            img=convertView.findViewById(R.id.tabPic);
            cls=convertView.findViewById(R.id.tabClose);




            title.setText(obj.getTitle());
            desc.setText(obj.getDesc());
            img.setImageBitmap(obj.getImage());


            cls.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tabss.remove(position);
                    CacheClass.removeFragment(position);
                    adapter.notifyDataSetChanged();
                    isRemovedTab=true;
                    removedTab=position;
                    MainActivity.refreshTabCount();

                }
            });


            return convertView;

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(isRemovedTab && CacheClass.cureWebFragment==-1)
        {
            loadMain();
        }
        else if(isRemovedTab && removedTab==CacheClass.cureWebFragment)
        {
            loadMain();
        }
    }
}
