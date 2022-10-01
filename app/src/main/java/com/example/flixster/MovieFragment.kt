package com.example.flixster

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import org.json.JSONArray
import org.json.JSONObject

private const val API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed"

class MovieFragment : Fragment(), OnListFragmentInteractionListener {
    //Constructing the view
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        val view = inflater.inflate(R.layout.activity_main, container,false)
        val recyclerView = view.findViewById<View>(R.id.list) as RecyclerView
        val context = view.context
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        updateAdapter(recyclerView)
        return view
    }

    /*
     * Updates the RecyclerView adapter with new data.  This is where the
     * networking magic happens!
     */
    private fun updateAdapter(recyclerView: RecyclerView){
        Log.d("UGH", "in update")
        //Create and set up AsyncHTTPClient()
        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api-key"] = API_KEY
        // Using the client, perform the HTTP request
        client[
                "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&language=en-US&page=1",
                params,
                object: JsonHttpResponseHandler()
        {
            /*
          * The onSuccess function gets called when
          * HTTP response status is "200 OK"
          */

            override fun onSuccess(

                statusCode: Int,
                headers: Headers,
                json: JsonHttpResponseHandler.JSON
            ) {
                Log.d("UGH", "yo")
                //TODO - Parse JSON into Models
                val resultsJSON : JSONArray = json.jsonArray.get(2) as JSONArray
//                val resultsJSON : JSONArray("result")
                Log.d("UGH", resultsJSON.toString())
                val resultsRawJSON : String = resultsJSON.toString()
                Log.d("UGH", resultsRawJSON)
                val gson = Gson()
                val arrayMovieType = object : TypeToken<List<Movie>>() {}.type


                val models : List<Movie> = gson.fromJson(resultsRawJSON,Array<Movie>::class.java).toList()
                recyclerView.adapter = MovieRecyclerViewAdapter(models, this@MovieFragment)

                // Look for this in Logcat:
                Log.d("BestSellerBooksFragment", "response successful")
            }

            /*
             * The onFailure function gets called when
             * HTTP response status is "4XX" (eg. 401, 403, 404)
             */
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                t: Throwable?
            ) {

                // If the error is not null, log it!
                t?.message?.let {
                    Log.e("MovieFragment", errorResponse)
                }
            }
        }]
    }

    /*
     * What happens when a particular book is clicked.
     */
    override fun onItemClick(item: Movie) {
        Toast.makeText(context, "test: " + item.title, Toast.LENGTH_LONG).show()
    }
}
