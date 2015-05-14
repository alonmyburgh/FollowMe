package android.project.followme;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationAddress 
{
    

    public String  getAddressFromLocation(final double latitude, final double longitude,
                                              final Context context)
    {
        
            
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = "";
                try {
                    List<Address> addressList = geocoder.getFromLocation(
                            latitude, longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                            sb.append(address.getAddressLine(i)).append("\n");
                        }
                        sb.append(address.getLocality()).append("\n");
                        sb.append(address.getCountryName());
                        result = sb.toString();
                    }
                    
                    
                } 
                catch (IOException e) 
                {
                    e.printStackTrace();
                } 
                finally 
                {
                    
                }
                return result;
            }
       
       
    }

