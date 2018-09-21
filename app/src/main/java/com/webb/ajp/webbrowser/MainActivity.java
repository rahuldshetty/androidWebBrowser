package com.webb.ajp.webbrowser;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
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

    EditText urlSource;

    TextView menuCount;
    ImageView backBtn,fwdBtn,menuOptions;

    ArrayList<WebFragment> webFragments;

    GridView gdview;

    public static Activity activity ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar=findViewById(R.id.main_toolbar);
        frameLayout=findViewById(R.id.frameLayout);

        activity=this;

        backBtn=findViewById(R.id.buttonBackward);
        fwdBtn=findViewById(R.id.buttonForward);
        menuOptions=findViewById(R.id.menu_options);

        menuCount=findViewById(R.id.menu_count);
        urlSource=findViewById(R.id.urlSource);

        webFragments=new ArrayList<WebFragment>();


        urlSource.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if(i== EditorInfo.IME_ACTION_SEND)
                {
                    loadWebFragment(urlSource.getText().toString());
                    handled=true;
                }

                return handled;
            }
        });





        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        if(savedInstanceState==null) {
            Home frag=new Home();
            loadFragment(frag);

        }

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
    }



    public void loadFragment(Fragment frag){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameLayout, frag);
        ft.commit();

    }
}
