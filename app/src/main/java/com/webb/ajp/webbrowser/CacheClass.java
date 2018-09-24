package com.webb.ajp.webbrowser;

import java.util.ArrayList;

public class CacheClass {

    public static int cureWebFragment; // -1 == HOME

    public static ArrayList<WebFragment> webFragments=new ArrayList<WebFragment>();;

    public CacheClass(){
        cureWebFragment=-1;
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

    public static void removeFragment(int i){ webFragments.remove(i);  }

    public static void replaceWebFragment(WebFragment newFrag,int pos){
        webFragments.remove(pos);
        webFragments.add(pos,newFrag);
    }

}
