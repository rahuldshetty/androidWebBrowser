package com.webb.ajp.webbrowser;

import java.util.ArrayList;

public class CacheClass {
    public static ArrayList<WebFragment> webFragments=new ArrayList<WebFragment>();;

    public CacheClass(){

    }

    public static ArrayList<WebFragment> getWebFragments(){
        return webFragments;
    }

    public static void addToWebFragment(WebFragment frag){
        webFragments.add(frag);
    }

    public static int getSize(){
        return webFragments.size();
    }


}
