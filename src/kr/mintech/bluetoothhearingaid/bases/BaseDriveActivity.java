package kr.mintech.bluetoothhearingaid.bases;

import java.util.Arrays;

import kr.mintech.bluetoothhearingaid.utils.ContextUtil;
import kr.mintech.bluetoothhearingaid.utils.PreferenceUtil;
import kr.mintech.bluetoothhearingaid.utils.UploadHelper.OnUserRecoverableAuthIOExceptionCallback;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.services.drive.DriveScopes;

public class BaseDriveActivity extends FragmentActivity
{
   
   private static final int REQUEST_ACCOUNT_PICKER = 10;
   private static final int REQUEST_AUTHORIZATION = 20;
   
   protected GoogleAccountCredential credential;
   
   
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      
      ContextUtil.CONTEXT = getApplicationContext();
      
      credential = GoogleAccountCredential.usingOAuth2(this, Arrays.asList(DriveScopes.DRIVE));
      
      String accountName = PreferenceUtil.googleAccount();
      if (TextUtils.isEmpty(accountName))
         startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
      else
         onDriveService(accountName);
   }
   
   
   @Override
   protected void onActivityResult(final int requestCode, final int resultCode, final Intent data)
   {
      switch (requestCode)
      {
         case REQUEST_ACCOUNT_PICKER:
            if (resultCode == RESULT_OK && data != null && data.getExtras() != null)
            {
               String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
               if (accountName != null)
                  onDriveService(accountName);
            }
            break;
         case REQUEST_AUTHORIZATION:
            Log.w("MainActivity.java | onActivityResult", "|" + "===================" + "|" + resultCode + "|" + Activity.RESULT_OK + "|"
                  + credential.getSelectedAccount());
            if (resultCode == Activity.RESULT_OK)
            {
               uploadToGoogleDrive();
            }
            else
            {
               startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
            }
            break;
      }
   }
   
   
   private void onDriveService(String $account)
   {
      credential.setSelectedAccountName($account);
      PreferenceUtil.putGoogleAccount($account);
   }
   
   
   protected void uploadToGoogleDrive()
   {
      
   }
   
   protected OnUserRecoverableAuthIOExceptionCallback authIOExceptionCallback = new OnUserRecoverableAuthIOExceptionCallback()
   {
      @Override
      public void onUserRecoverableAuthIOException(UserRecoverableAuthIOException e)
      {
         startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);
      }
   };
}
