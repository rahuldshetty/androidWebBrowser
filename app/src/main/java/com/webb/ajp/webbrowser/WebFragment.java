package com.webb.ajp.webbrowser;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class WebFragment extends Fragment {


    ProgressBar superProgressBar;
    ImageView superImage;
    TextView wtitle;
    public WebView webView;

    String url;

    String webtitle;


    public WebFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_web, container, false);



        superImage=view.findViewById(R.id.siteIcon);
        superProgressBar=view.findViewById(R.id.progressBar);
        webView = view.findViewById(R.id.myWebView);

        wtitle=view.findViewById(R.id.webTitle);

        superProgressBar.setMax(100);

        superProgressBar.setVisibility(View.INVISIBLE);

        url= getArguments().getString("URL");

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
                }
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);

                superImage.setImageBitmap(icon);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                wtitle.setText(title);
            }

        });

        return view;

    }



}
