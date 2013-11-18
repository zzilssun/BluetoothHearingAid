package kr.mintech.bluetoothhearingaid.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtil
{
   /**
    * $json에서 $key에 해당하는 value 가져오기.
    * 
    * @param $json
    *           에서
    * @param $key
    *           의 값 가져오기
    * @return
    */
   public static String value(JSONObject $json, String $key)
   {
      return value($json.toString(), $key);
   }
   
   
   /**
    * {로 시작하는 $json에서 $key에 해당하는 value 가져오기.
    * 
    * @param $json
    *           에서
    * @param $key
    *           의 값 가져오기
    * @return
    */
   public static String value(String $json, String $key)
   {
      String result = null;
      
      try
      {
         result = new JSONObject($json).getString($key);
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      
      return result;
   }
   
   
   /**
    * [로 시작하는 json String의 {}들을 ArrayList<String>으로 가져오기.
    * 
    * @param $json
    *           의
    * @param $key
    *           값을
    * @return ArrayList 가져오기.
    */
   public static ArrayList<String> array(JSONObject $json, String $key)
   {
      return array(value($json, $key));
   }
   
   
   /**
    * [로 시작하는 json String의 {}들을 ArrayList<String>으로 가져오기.
    * 
    * @param $json
    *           의
    * @param $key
    *           값을
    * @return ArrayList 가져오기.
    */
   public static ArrayList<String> array(String $json, String $key)
   {
      return array(value($json, $key));
   }
   
   
   /**
    * [로 시작하는 json String의 {}들을 ArrayList<String>으로 가져오기.
    * 
    * @param $json
    * @return
    */
   public static ArrayList<String> array(String $json)
   {
      ArrayList<String> result = new ArrayList<String>();
      
      try
      {
         JSONArray array = new JSONArray($json);
         
         for (int i = 0; i < array.length(); i++)
            result.add(array.getString(i));
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      
      return result;
   }
}
