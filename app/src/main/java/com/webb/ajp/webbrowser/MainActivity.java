package com.webb.ajp.webbrowser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.provider.DocumentsContract;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Toolbar mToolbar;

    FrameLayout frameLayout;

    public static EditText urlSource;

    public static TextView menuCount;
    public static ImageView backBtn,fwdBtn,circleImg;

    public static boolean curWebFragmentIsNewPage=false;


    public ArrayList<WebFragment> webFragments;



    String urlTemp=null;
   static int selectFrag;
   static boolean toadd;

    public static Home home;

    public static Activity activity ;
    static FragmentManager  fragmentManager;
    static FragmentTransaction fragmentTransaction;


    public SQLiteDatabase mydatabase ;


    public static String defaultURL = "https://www.google.com/search?q=";


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
                    if(CacheClass.cureWebFragment==-1)
                        loadWebFragment(urlSource.getText().toString());
                    else{

                        String url =urlSource.getText().toString();
                        if(!isValid(url))
                        {
                            url=defaultURL+url;
                        }
                        WebFragment webFragment=CacheClass.getWebFragments().get(CacheClass.cureWebFragment);
                        webFragment.webView.loadUrl(url);
                        webFragment.setUrl(url);
                    }
                    handled=true;
                }

                return handled;
            }
        });

        fwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CacheClass.cureWebFragment==-1)
                {
                    return;
                }
                if(curWebFragmentIsNewPage)
                {
                    return;
                }

                WebFragment frag=webFragments.get(CacheClass.cureWebFragment);
                if(frag.webView.canGoForward())
                {
                    frag.webView.goForward();
                }

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CacheClass.cureWebFragment==-1) {
                    finish();
                    return;
                }

                if(curWebFragmentIsNewPage)
                {
                   return;
                }

                WebFragment frag=webFragments.get(CacheClass.cureWebFragment);

                if(frag.webView.canGoBack() ){
                    frag.webView.goBack();

                }
                else
                {
                    CacheClass.removeFragment(CacheClass.cureWebFragment);
                    webFragments=CacheClass.getWebFragments();
                    loadFragment(home);
                    urlSource.setText("");
                    CacheClass.cureWebFragment=-1;
                    curWebFragmentIsNewPage=false;
                }


            }
        });


        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        if(savedInstanceState==null) {
            home =new Home();
            loadFragment(home);
            CacheClass.cureWebFragment=-1;

        }



        if(urlTemp!=null)
        {
            loadWebview(urlTemp,toadd);
            urlTemp=null;
            CacheClass.cureWebFragment=CacheClass.getSize();
            webFragments=CacheClass.getWebFragments();
        }

    }


    public static boolean isValid(String url)
    {
        try {
            new URL(url).toURI();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }


    void goToTabs(){
        Intent tab=new Intent(MainActivity.this,WebTabs.class);
        startActivity(tab);

    }

    public void loadWebFragment(String url)
    {

        if(!isValid(url))
        {
            url=defaultURL+url;
        }

        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        WebFragment frag = new WebFragment();
        Bundle args = new Bundle();
        args.putString("URL",url);
        frag.setArguments(args);
        webFragments.add(frag);
        ft.replace(R.id.frameLayout, frag);
        ft.commit();

        CacheClass.cureWebFragment++;

        refreshTabCount();

    }

    public static void loadWebview(String url ,boolean add){
        WebFragment frag = new WebFragment();
        Bundle args = new Bundle();
        args.putString("URL",url);
        frag.setArguments(args);
        fragmentTransaction.replace(R.id.frameLayout, frag);
        fragmentTransaction.commit();


        if(!add)
        {
            CacheClass.webFragments.set(selectFrag,frag);
        }
        else{
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

        if(CacheClass.cureWebFragment==-1) {
            finish();
            return;
        }
        if(curWebFragmentIsNewPage)
        {
            return;
        }

        WebFragment frag=webFragments.get(CacheClass.cureWebFragment);
        if(frag.webView.canGoBack()){
            frag.webView.goBack();
        }
        else
        {
            CacheClass.removeFragment(CacheClass.cureWebFragment);
            webFragments=CacheClass.getWebFragments();
            loadFragment(home);
            urlSource.setText("");
            CacheClass.cureWebFragment=-1;
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

            case R.id.optionSettings:
                Intent settings=new Intent(MainActivity.this,SettingActivity.class);
                startActivity(settings);
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
                String titl = webFragments.get(CacheClass.cureWebFragment).webtitle;
                String curURL = urlSource.getText().toString();
                mydatabase.execSQL("INSERT INTO BOOKMARKS VALUES(" +"'"+ titl  +"'"  + ",'"+curURL+"',"+count+");");
                resultSet.close();
                break;


            case R.id.webSaveAsPDF:

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {

                    PrintManager printManager = (PrintManager) this
                            .getSystemService(Context.PRINT_SERVICE);

                    PrintDocumentAdapter printAdapter =
                            webFragments.get(CacheClass.cureWebFragment).webView.createPrintDocumentAdapter();

                    String jobName = getString(R.string.app_name) + " Print Test";

                    if (printManager != null) {
                        printManager.print(jobName, printAdapter,
                                new PrintAttributes.Builder().build());
                    }

                } else {



                }




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

