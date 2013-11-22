package kr.mintech.bluetoothhearingaid.utils;

import java.io.BufferedReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Log;

public class UrlShortenUtil
{
   private static final String URL_GOOGL_SERVICE = "https://www.googleapis.com/urlshortener/v1/url";
   
   
   public static String shorten(String $longUrl)
   {
      DefaultHttpClient client = new DefaultHttpClient();
      String result = null;
      BufferedReader reader = null;
      
      try
      {
         HttpPost kRequest = new HttpPost(URL_GOOGL_SERVICE);
         kRequest.setHeader("Content-Type", "application/json");
         
         JSONObject longUrlJson = new JSONObject();
         longUrlJson.put("longUrl", $longUrl);
         
         StringEntity kStringEntity = new StringEntity(longUrlJson.toString(), HTTP.UTF_8);
         
         kRequest.setEntity(kStringEntity);
         
         HttpResponse kResponse = client.execute(kRequest);
         String jsonResult = EntityUtils.toString(kResponse.getEntity());
         
         result = JsonUtil.value(jsonResult, "id");
         Log.i("UrlShortenUtil.java | shorten", "|" + $longUrl + " => " + result + "|");
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      finally
      {
         if (reader != null)
         {
            try
            {
               reader.close();
            }
            catch (Exception e)
            {
               e.printStackTrace();
            }
         }
         
         client.getConnectionManager().closeExpiredConnections();
      }
      
      return result;
   }
}
