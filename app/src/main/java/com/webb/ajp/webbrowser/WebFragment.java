package com.webb.ajp.webbrowser;


import android.app.DownloadManager;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class WebFragment extends Fragment {


    ProgressBar superProgressBar;
    ImageView superImage;
    TextView wtitle;
    public WebView webView;

    public String url;

    public String webtitle;
    public Bitmap img;

    SQLiteDatabase mydatabase ;

    public void setUrl(String ur)
    {
        url=ur;
    }

    public WebFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_web, container, false);

        mydatabase = getActivity().openOrCreateDatabase("BROWESERDB",MODE_PRIVATE,null);

        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS HISTORY(title  varchar(50),url VARCHAR(10000),times DATETIME DEFAULT CURRENT_TIMESTAMP);");

        superImage=view.findViewById(R.id.siteIcon);
        superProgressBar=view.findViewById(R.id.progressBar);
        webView = view.findViewById(R.id.myWebView);

        wtitle=view.findViewById(R.id.webTitle);

        superProgressBar.setMax(100);

        superProgressBar.setVisibility(View.INVISIBLE);

        if(getArguments()!=null)
            url= getArguments().getString("URL");
        else
            url="https://www.google.com";

        registerForContextMenu(webView);




        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient(){



            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                if(newProgress!=100)
                {
                    superProgressBar.setVisibility(View.VISIBLE);
                }

                if(webView.getUrl()!=null)
                    MainActivity.urlSource.setText(webView.getUrl());
                superProgressBar.setProgress(newProgress);
                if(newProgress==100)
                {
                    superProgressBar.setVisibility(View.INVISIBLE);
                    if(url!="")
                        mydatabase.execSQL("INSERT INTO HISTORY VALUES(" +"'"+ webtitle  +"'"  + ",'"+url+"'" +",datetime('now','localtime')"+");" );




                }
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
                superImage.setImageBitmap(icon);
                img=icon;
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                wtitle.setText(title);
                webtitle=title;
            }

        });

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                request.setMimeType(mimeType);
                //------------------------COOKIE!!------------------------
                String cookies = CookieManager.getInstance().getCookie(url);
                request.addRequestHeader("cookie", cookies);
                //------------------------COOKIE!!------------------------
                request.addRequestHeader("User-Agent", userAgent);
                request.setDescription("Downloading file...");
                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimeType));
                DownloadManager dm = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getContext(), "Downloading File", Toast.LENGTH_LONG).show();


            }
        });

        return view;

    }



}
