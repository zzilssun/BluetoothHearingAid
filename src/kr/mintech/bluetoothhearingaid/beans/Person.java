package kr.mintech.bluetoothhearingaid.beans;

import kr.mintech.bluetoothhearingaid.utils.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

public class Person
{
   private static final String NAME = "name";
   private static final String PHONE = "phone";
   
   public String name = "";
   public String phone = "";
   public boolean selected = false;
   
   
   /**
    * 사람 한 명의 정보
    * 
    * @param $name
    *           이름
    * @param $phone
    *           연락처
    */
   public Person(String $name, String $phone)
   {
      super();
      name = $name;
      phone = $phone;
   }
   
   
   /**
    * 사람 한 명의 정보
    * 
    * @param $jsonStr
    *           json 문자열<br>
    *           {"phone":"1234","name":"asdf"}
    */
   public Person(String $jsonStr)
   {
      super();
      
      if (TextUtils.isEmpty($jsonStr))
         return;
      
      name = JsonUtil.value($jsonStr, NAME);
      phone = JsonUtil.value($jsonStr, PHONE);
   }
   
   
   /**
    * json object 받기
    * 
    * @return JSONObject
    */
   public JSONObject json()
   {
      JSONObject result = new JSONObject();
      try
      {
         result.put(NAME, name);
         result.put(PHONE, phone);
      }
      catch (JSONException e)
      {
         e.printStackTrace();
      }
      
      return result;
   }
   
   
   /**
    * json 문자열 받기
    * 
    * @return {"phone":"1234","name":"asdf"}
    */
   public String jsonStr()
   {
      return json().toString();
   }
}
