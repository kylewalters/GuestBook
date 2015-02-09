package com.kylethetechguy.guestbook;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;

public class Parse_AppAPIKeys extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    // Initialization code
    Parse.initialize(this, "", "");
    ParseACL defaultACL = new ParseACL();
   
    // If you would like all objects to be private by default, remove this line.
    defaultACL.setPublicReadAccess(true); 
    ParseACL.setDefaultACL(defaultACL, true);
  }
}
