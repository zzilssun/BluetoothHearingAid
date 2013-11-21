package kr.mintech.bluetoothhearingaid.utils;

import java.io.IOException;
import java.util.Arrays;

import kr.mintech.bluetoothhearingaid.R;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;
import com.google.api.services.drive.model.Permission;

public class UploadHelper
{
   private Drive _service;
   private OnUserRecoverableAuthIOExceptionCallback _authExceptionCallback;
   private Context _context;
   private OnUploadEndCallback _uploadCallback;
   
   
   public UploadHelper(Context $context, GoogleAccountCredential $credential, OnUserRecoverableAuthIOExceptionCallback $callback)
   {
      super();
      _context = $context;
      _service = getDriveService($credential);
      _authExceptionCallback = $callback;
   }
   
   
   public void upload(final String $filePath, final OnUploadEndCallback $uploadCallback)
   {
      _uploadCallback = $uploadCallback;
      uploadTask.execute($filePath);
//      Thread t = new Thread(new Runnable()
//      {
//         @Override
//         public void run()
//         {
      
//         }
//      });
//      t.start();
   }
   
   
   private String uploadToDrive(String $filePath)
   {
      String result = null;
      try
      {
         boolean hasFolder = false;
         File targetFolder = null;
         
         // 폴더 목록 검색
         Files.List request = _service.files().list().setQ("mimeType='application/vnd.google-apps.folder' and trashed=false");
         FileList files = request.execute();
         
         for (File folder : files.getItems())
         {
            // 원하는 폴더 있는지 검사
            if (folder.getTitle().equals(_context.getString(R.string.app_name)))
            {
               hasFolder = true;
               targetFolder = folder;
               break;
            }
         }
         
         // 원하는 폴더 없으면 새로 만들기
         if (!hasFolder)
         {
            File folderBody = new File();
            folderBody.setTitle(_context.getString(R.string.app_name));
            folderBody.setMimeType("application/vnd.google-apps.folder");
            targetFolder = _service.files().insert(folderBody).execute();
         }
         
         // 파일 정보 세팅
         java.io.File fileContent = new java.io.File($filePath);
         FileContent mediaContent = new FileContent("audio/mp4", fileContent);
         
         // 위에서 정한 폴더에 파일 넣기
         File body = new File();
         body.setTitle(fileContent.getName());
         body.setMimeType("audio/mp4");
         body.setShared(true);
         body.setParents(Arrays.asList(new ParentReference().setId(targetFolder.getId())));
         
         File file = _service.files().insert(body, mediaContent).execute();
         if (file != null)
         {
            // 파일을 전체 공개로 변경하기
            Permission permission = new Permission();
            permission.setType("anyone");
            permission.setRole("reader");
            permission.setValue("anyone");
            _service.permissions().insert(file.getId(), permission).execute();
            Log.i("MainActivity.java | run", "|" + file.getWebContentLink() + "|" + UrlShortenUtil.shorten(file.getWebContentLink()));
            result = UrlShortenUtil.shorten(file.getWebContentLink());
//            _uploadCallback.onUploaded(UrlShortenUtil.shorten(file.getWebContentLink()));
         }
//         else
//         {
//            _uploadCallback.onUploadFail();
//         }
      }
      catch (UserRecoverableAuthIOException e)
      {
         _authExceptionCallback.onUserRecoverableAuthIOException(e);
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
      return result;
   }
   
   
   private Drive getDriveService(GoogleAccountCredential credential)
   {
      return new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential).build();
   }
   
   private AsyncTask<String, Integer, String> uploadTask = new AsyncTask<String, Integer, String>()
   {
      @Override
      protected String doInBackground(String... params)
      {
         return uploadToDrive(params[0]);
      }
      
      
      protected void onPostExecute(String result)
      {
         _uploadCallback.onUploaded(result);
      };
      
   };
   
   public interface OnUserRecoverableAuthIOExceptionCallback
   {
      public void onUserRecoverableAuthIOException(UserRecoverableAuthIOException e);
   }
   
   public interface OnUploadEndCallback
   {
      public void onUploaded(String $url);
      
      
      public void onUploadFail();
   }
}