package kr.mintech.bluetoothhearingaid.utils;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;

public class LocationManager
{
   private LocationClient mLocationClient;
   private Context _context;
   private OnLocationChangedListener _locationChangedListener;
   
   
   public LocationManager(Context $context, OnLocationChangedListener $listener)
   {
      super();
      _context = $context;
      _locationChangedListener = $listener;
      mLocationClient = new LocationClient($context, connectionCallbacks, connectionFailedListener);
   }
   
   
   public void startLocationFind()
   {
      mLocationClient.connect();
   }
   
   
   public void disconnect()
   {
      if (mLocationClient.isConnected())
         mLocationClient.disconnect();
   }
   
   
   private void getLocation()
   {
      Location location = null;
      
      if (servicesConnected())
      {
         location = mLocationClient.getLastLocation();
      }
      
      _locationChangedListener.onLocationChanged(location);
   }
   
   
   private boolean servicesConnected()
   {
      int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(_context);
      
      if (ConnectionResult.SUCCESS == resultCode)
      {
         return true;
      }
      else
      {
         Log.w("MainActivity.java | servicesConnected", "|" + "error" + "|");
         return false;
      }
   }
   
   private ConnectionCallbacks connectionCallbacks = new ConnectionCallbacks()
   {
      @Override
      public void onDisconnected()
      {
         
      }
      
      
      @Override
      public void onConnected(Bundle connectionHint)
      {
         getLocation();
      }
   };
   
   private OnConnectionFailedListener connectionFailedListener = new OnConnectionFailedListener()
   {
      @Override
      public void onConnectionFailed(ConnectionResult result)
      {
         
      }
   };
}
