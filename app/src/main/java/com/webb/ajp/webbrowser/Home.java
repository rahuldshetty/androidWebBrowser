package com.webb.ajp.webbrowser;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {

    public GridView grid;

    static public ArrayList<WebsiteData> datas;

    View view;

    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_home, container, false);

        grid = view.findViewById(R.id.gridView);
        datas=new ArrayList<WebsiteData>();
        datas.add(new WebsiteData("https:m.facebook.com",R.drawable.icons8facebook500,"Facebook"));
        datas.add(new WebsiteData("https:www.google.com",R.drawable.icons8google480,"Google"));
        datas.add(new WebsiteData("https:www.youtube.com",R.drawable.icons8playbutto480,"Youtube"));
        datas.add(new WebsiteData("https:mobile.twitter.com",R.drawable.icons8twitter480,"Twitter"));


        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String url = (datas.get(i).getUrl());


            }
        });


        WebsiteAdapter adapter=new WebsiteAdapter(this.getContext(),datas);
        grid.setAdapter(adapter);



        return view;

    }




    public class WebsiteAdapter extends BaseAdapter{

        List<WebsiteData> websiteDataList;

        private Context mContext;

        public WebsiteAdapter( Context c , List<WebsiteData> data){
            mContext=c;
            websiteDataList=data;


        }

        @Override
        public int getCount() {
            return websiteDataList.size();
        }

        @Override
        public Object getItem(int i) {
            return websiteDataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View grid;
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (view == null) {

                grid = new View(mContext);
                grid = inflater.inflate(R.layout.grid_single, null);
                TextView textView = (TextView) grid.findViewById(R.id.siteTitle);
                ImageView imageView = (ImageView)grid.findViewById(R.id.siteIcon);
                textView.setText(websiteDataList.get(i).getTitle());
                textView.setContentDescription(websiteDataList.get(i).getUrl());
                imageView.setImageResource(websiteDataList.get(i).getImg());

            } else {
                grid = (View) view;
            }



            return grid;
        }
    }


}
