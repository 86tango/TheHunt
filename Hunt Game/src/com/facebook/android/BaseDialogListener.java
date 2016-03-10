package com.facebook.android;

import android.content.Context;

import com.facebook.android.Facebook.DialogListener;

/**
 * Skeleton base class for RequestListeners, providing default error 
 * handling. Applications should handle these error conditions.
 *
 */
public abstract class BaseDialogListener implements DialogListener {
	Context con;
    public void onFacebookError(FacebookError e) {
        e.printStackTrace();
    }

    public void onError(DialogError e) {
        e.printStackTrace();        
    }

    public void onCancel() {    
    //	Toast.makeText(con, "hasds", Toast.LENGTH_LONG).show();
    }
    
}
