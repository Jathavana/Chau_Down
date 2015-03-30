package com.example.yadu.chaudown;

import android.graphics.Bitmap;

/**
 * Created by Nav on 11/15/2014.
 *
 * Interface must be implemented by any Activities or Classes which wish to use the Mongo.java
 * database driver and should be passed as the first argument whenever calling one of
 * Mongo's static methods.
 */
public interface MongoAdapter2 {

    /**
     * Method should return the name of the database the caller wishes to access
     *
     * @return The name of the database to be accessed
     */
    public String dbName();

    /**
     * Method should return the API key to be used to connect to the MongoLab REST api.
     * The current API key for a user can be found on the user page of the MongoLab website.
     *
     * @return The API key to use to connect to the database.
     */
    public String apiKey();

    /**
     * Method to process the result given by a Mongo.get() call.
     * Other async HTTP requests can be modified to use the method as well.
     *
     * @param result The result string returned by the HTTP request.
     */
    public void processResult(Bitmap[] result);
}
