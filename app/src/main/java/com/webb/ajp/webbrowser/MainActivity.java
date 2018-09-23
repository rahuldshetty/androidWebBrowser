package com.webb.ajp.webbrowser;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Toolbar mToolbar;

    FrameLayout frameLayout;

    public static EditText urlSource;

    public static TextView menuCount;
    public static ImageView backBtn,fwdBtn,menuOptions,circleImg;

    public static int curWebFragment=-1;

    public ArrayList<WebFragment> webFragments;

    GridView gdview;

    String urlTemp=null;
    int selectFrag;
    boolean toadd;

    public static Home home;

    public static Activity activity ;
    static FragmentManager  fragmentManager;
    static FragmentTransaction fragmentTransaction;


    public SQLiteDatabase mydatabase ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mydatabase= openOrCreateDatabase("BROWESERDB",MODE_PRIVATE,null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS BOOKMARKS(title  varchar(50),url VARCHAR(10000),id int);");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS HISTORY(title  varchar(50),url VARCHAR(10000),times DATETIME DEFAULT CURRENT_TIMESTAMP);");

        if(getIntent().getExtras()!=null) {
            urlTemp = getIntent().getExtras().getString("URLfromTAB");
            selectFrag=getIntent().getExtras().getInt("SELECTEDFRAG");
            toadd=getIntent().getExtras().getBoolean("TOADD");
        }

        fragmentManager=getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();



        mToolbar=findViewById(R.id.main_toolbar);
        frameLayout=findViewById(R.id.frameLayout);

        activity=this;

        backBtn=findViewById(R.id.buttonBackward);
        fwdBtn=findViewById(R.id.buttonForward);

        menuCount=findViewById(R.id.menu_count);
        urlSource=findViewById(R.id.urlSource);

        circleImg=findViewById(R.id.circleimg);



        webFragments=CacheClass.getWebFragments();

        menuCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToTabs();
            }
        });



        circleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToTabs();
            }
        });


        urlSource.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if(i== EditorInfo.IME_ACTION_SEND)
                {
                    if(curWebFragment==-1)
                        loadWebFragment(urlSource.getText().toString());
                    else{
                        WebFragment webFragment=CacheClass.getWebFragments().get(curWebFragment);
                        webFragment.webView.loadUrl(urlSource.getText().toString());
                        webFragment.setUrl(urlSource.getText().toString());
                    }
                    handled=true;
                }

                return handled;
            }
        });

        fwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curWebFragment==-1)
                {
                    return;
                }
                WebFragment frag=webFragments.get(curWebFragment);
                if(frag.webView.canGoForward())
                {
                    frag.webView.goForward();
                }

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curWebFragment==-1) {
                    finish();
                    return;
                }
                WebFragment frag=webFragments.get(curWebFragment);

                if(frag.webView.canGoBack()){
                    frag.webView.goBack();
                }
                else
                {
                    CacheClass.removeFragment(curWebFragment);
                    webFragments=CacheClass.getWebFragments();
                    loadFragment(home);
                    urlSource.setText("");
                    curWebFragment=-1;
                }


            }
        });


        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        if(savedInstanceState==null) {
            home =new Home();
            loadFragment(home);
            curWebFragment=-1;

        }
        curWebFragment=-1;


        if(urlTemp!=null)
        {
            loadWebview(urlTemp,toadd);
            urlTemp=null;
            curWebFragment=selectFrag;
        }

    }



    void goToTabs(){
        Intent tab=new Intent(MainActivity.this,WebTabs.class);
        startActivity(tab);
        WebTabs.loadTabs();

    }

    public void loadWebFragment(String url)
    {
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        WebFragment frag = new WebFragment();
        Bundle args = new Bundle();
        args.putString("URL",url);
        frag.setArguments(args);
        webFragments.add(frag);
        ft.replace(R.id.frameLayout, frag);
        ft.commit();
        curWebFragment++;
        refreshTabCount();

    }

    public static void loadWebview(String url ,boolean add){
        WebFragment frag = new WebFragment();
        Bundle args = new Bundle();
        args.putString("URL",url);
        frag.setArguments(args);
        fragmentTransaction.replace(R.id.frameLayout, frag);
        fragmentTransaction.commit();

        if(add)
        {
            CacheClass.addToWebFragment(frag);
        }
        refreshTabCount();
    }


    public void loadFragment(Fragment frag){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameLayout, frag);
        ft.commit();
        refreshTabCount();
    }


    @Override
    public void onBackPressed() {

        if(curWebFragment==-1) {
            finish();
            return;
        }
        WebFragment frag=webFragments.get(curWebFragment);
        if(frag.webView.canGoBack()){
            frag.webView.goBack();
        }
        else
        {
            CacheClass.removeFragment(curWebFragment);
            webFragments=CacheClass.getWebFragments();
            loadFragment(home);
            urlSource.setText("");
            curWebFragment=-1;
        }
        refreshTabCount();

    }


    public static void refreshTabCount(){
        menuCount.setText(CacheClass.getSize()+"");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.opitonExit:
                finish();
                break;

            case R.id.optionTab:
                loadWebFragment("");
                break;

            case R.id.optionBookmarks:
                Intent bookmark=new Intent(MainActivity.this,Bookmarks.class);
                startActivity(bookmark);
                break;

            case R.id.optionHistory:
                Intent history=new Intent(MainActivity.this,History.class);
                startActivity(history);
                break;



        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.webBookmark:
                Cursor resultSet = mydatabase.rawQuery("Select * from BOOKMARKS",null);
                int count=resultSet.getCount();
                String titl = webFragments.get(curWebFragment).webtitle;
                String curURL = urlSource.getText().toString();
                mydatabase.execSQL("INSERT INTO BOOKMARKS VALUES(" +"'"+ titl  +"'"  + ",'"+curURL+"',"+count+");");
                resultSet.close();
                break;


        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionmenu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.webviewmenu, menu);
    }
}

