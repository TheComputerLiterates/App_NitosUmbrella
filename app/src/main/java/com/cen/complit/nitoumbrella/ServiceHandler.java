package com.cen.complit.nitoumbrella;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Andrew on 4/10/2015.
 */
public class ServiceHandler {
    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;
    public final static String APIKEY = "ZTPDcCdzezkcdrMhbWPuaBJYqKKMDy63K9pJrfHpQCsnRG7vbc82skUYvcFq343XypfGTZ9eVzvUhBWCP4RWTmBwKuGG2WW8wc2T7Zwfv3AYHZbaxRb56whsRVJGpR6re845Jkht5W36A9CpzzF3UTJMXtNEPKQ4aq5vTpW3Cw2GF45nS97hVu34x4dTwGFBnK5sGXVVWZDEcsJ4XAw4KyFAryVwUFLRE8ub7Lnp2PgAa7Zqd54aC59kkkc6SZKE";
    public final static String URL = "http://fsuhvz.com/api/";
    public ServiceHandler(){

    }

    //Make service call
    public String makeServiceCall(String url, int method){
        return this.makeServiceCall(url, method, null);
    }

    public String makeServiceCall(String url, int method, List<NameValuePair> params){
        try{
            //http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            //Checking http request method type
            if(method == POST){
                HttpPost httpPost = new HttpPost(url);
                //adding post params
                if(params != null){
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }

                httpResponse = httpClient.execute(httpPost);
            }else if(method == GET){
                //appending params to url
                if(params != null){
                    String paramString = URLEncodedUtils.format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);
                httpResponse = httpClient.execute(httpGet);
            }
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
        } catch(UnsupportedEncodingException e){
            e.printStackTrace();
        } catch(ClientProtocolException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }

        return response;
    }
}
