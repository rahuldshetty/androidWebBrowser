package com.webb.ajp.webbrowser;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    Button histrory ;

    public SQLiteDatabase mydatabase ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        histrory=findViewById(R.id.settingsClearHistory);

        mydatabase= openOrCreateDatabase("BROWESERDB",MODE_PRIVATE,null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS BOOKMARKS(title  varchar(50),url VARCHAR(10000),id int);");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS HISTORY(title  varchar(50),url VARCHAR(10000),times DATETIME DEFAULT CURRENT_TIMESTAMP);");

        histrory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mydatabase.execSQL("Delete from HISTORY;");
                Toast.makeText(view.getContext(),"All history cleared...",Toast.LENGTH_LONG).show();
            }
        });
    }

}
