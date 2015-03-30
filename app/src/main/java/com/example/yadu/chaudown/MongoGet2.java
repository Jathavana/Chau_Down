package com.example.yadu.chaudown;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Nav Bhatti on 11/11/2014.
 *
 * Code is based on Lola Priego's Android app + MongoDB + MongoLab [cloud] blog post
 * modified using AsyncTasks to comply with Android's enforcement of non-blocking network calls
 * Original post can be found @ http://lolapriego.com/blog/android-app-mongodb-mongolab-cloud/
 *
 * All class methods can be used statically to make HTTP Requests to a MongoLab database
 * hosted in the cloud using the MongoDB REST API documented @ http://docs.mongolab.com/restapi/.
 * Class methods do not have mechanism for all the optional parameters illustrated in the
 * documentation, but can be easily modified to suit the needs of the application.
 *
 * Any Activities or Classes wishing to use this class must implement the MongoAdapter Interface
 * and pass themselves as the first parameter in each of the methods.
 *
 * @author Nav Bhatti
 * @author Lola Priego
 */
public class MongoGet2 {

    private static final String BASE_URL = "https://api.mongolab.com/api/1/databases/";


    /**
     * Execute an HTTP GET request as a separate thread using an AsyncTask
     * GET requests are used to retrieve documents
     *
     * @param context MongoAdapter Interface to be used as context for the request
     * @param collection Name of the collection to retrieve from
     * @param query Limit the returned documents to those matching the properties of query
     */
    public static void get( MongoAdapter2 context, String collection, JSONObject query, GridView gridview, Activity activity )
    {
        String url = BASE_URL
                + context.dbName()
                + "/collections/" + collection + "?";

        if ( query != null )
        {
            try{
                url += "q=" + URLEncoder.encode( query.toString(), "UTF-8" ) + "&";
            }catch ( UnsupportedEncodingException e )
            {
                Log.d( "URLEncoder", e.getLocalizedMessage() );
            }
        }

        url += "apiKey=" + context.apiKey();
        Log.d( "URL", url );
        new GetTask( context, gridview, activity ).execute(url);
    }

    /**
     * Converts the passed InputStream into a String
     *
     * @param is InputStream to convert
     * @return Converted InputStream as a String
     * @throws java.io.IOException
     */
    private static String convertStreamToString(final InputStream is)
            throws IOException {
        InputStreamReader isr;
        BufferedReader reader;
        final StringBuilder builder;
        String line;

        isr = new InputStreamReader(is);
        reader = new BufferedReader(isr);
        builder = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        return builder.toString();
    }

    /**
     * A new instance of the classes below will be created and executed Asynchronously
     * for the corresponding HTTP request verbs. In the case of a GetTask, a result String will
     * be passed to the processResult method of the given context via the onPostExecute() method.
     *
     * The other 3 tasks have empty onPostExecute() methods, but these can be modified to return
     * status strings or other debugging information.
     */
    private static class GetTask
            extends AsyncTask<String, Void, Bitmap[]>   // params, progress, result
    {
        private final MongoAdapter2 context;
        GridView newGridView;
        Activity newActivity;

        public GetTask(final MongoAdapter2 c, GridView gridview, Activity activity)
        {
            context = c;
            this.newGridView = gridview;
            this.newActivity = activity;
        }

        @Override
        protected Bitmap[] doInBackground(final String... params)
        {
            InputStream inputStream;
            String      result;
            Bitmap[] bm = new Bitmap[4];

            if(params.length != 1)
            {
                throw new IllegalArgumentException("You must provide one uri only");
            }

            inputStream = null;

            try
            {
                final HttpClient httpclient;
                final HttpGet httpGet;
                final HttpResponse httpResponse;

                httpclient   = new DefaultHttpClient();
                httpGet      = new HttpGet( new URI(params[0]) );
                httpResponse = httpclient.execute(httpGet);
                inputStream  = httpResponse.getEntity().getContent();

                if(inputStream != null)
                {
                    result = convertStreamToString(inputStream);
                    try{
                        JSONArray jsonArray = new JSONArray(result);
                        JSONObject jsonObject;

                        bm = new Bitmap[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++){
                            jsonObject = jsonArray.getJSONObject(i);
                            String imageLocation = jsonObject.getString("BannerURL");
                            Bitmap image = getDamnFUCKINGIMAGES(imageLocation);
                            bm[i] = image;
                        }
                        Log.d("BM ARRAY", bm.toString());

                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
                else
                {
                    result = null;
                }
            }
            catch(final ClientProtocolException ex)
            {
                Log.d("InputStream", ex.getLocalizedMessage());
            }
            catch(final IOException ex)
            {
                Log.d("InputStream", ex.getLocalizedMessage());
            }
            catch ( URISyntaxException ex )
            {
                Log.d("InputStream", ex.getLocalizedMessage());
            }
            return (bm);
        }

        public Bitmap getDamnFUCKINGIMAGES(String imageLocation){
            Bitmap image;
            try {
                URL url = new URL(imageLocation);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();

                image = BitmapFactory.decodeStream(input);

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("getBmpFromUrl error: ", e.getMessage());
                return null;
            }


            return image;
        }

        @Override
        protected void onPostExecute(Bitmap[] result)
        {
            if (result != null)
            {
                newGridView.setAdapter(new ImageAdapter(newActivity, result));

                Log.d("HHHHHHHHHHHHHHHI", "RESULT");
                //context.processResult(result);

            }
        }

    }

}
