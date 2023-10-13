package com.example.marvelcharacters

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException
import com.bumptech.glide.request.RequestOptions
import java.net.URLEncoder


class MainActivity : AppCompatActivity() {
    private var offset = 0 // Add an offset variable to fetch new characters
    private lateinit var imageView: ImageView
    private lateinit var characterNameTextView: TextView
    private lateinit var characterIdTextView: TextView
    private lateinit var comicsNameTextView: TextView
    private lateinit var characterDescriptionTextView: TextView
    private lateinit var searchInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.marvelcharacterimage)
        characterNameTextView = findViewById(R.id.charactername)
        characterIdTextView = findViewById(R.id.characterid)
        comicsNameTextView = findViewById(R.id.comicsname)
        characterDescriptionTextView = findViewById(R.id.characterdescription)
        searchInput = findViewById(R.id.searchinput)
        comicsNameTextView.setOnClickListener {
            if (comicsNameTextView.maxLines == 2) {
                comicsNameTextView.maxLines = Integer.MAX_VALUE // or some large number you prefer
            } else {
                comicsNameTextView.maxLines = 2
            }

        }

        val characterButton: Button = findViewById(R.id.characterbutton)
        val searchButton: Button = findViewById(R.id.searchbutton)
        searchButton.setOnClickListener {
            val query = searchInput.text.toString().trim()
            if (query.isNotEmpty()) {
                getCharacterData(query)
            } else {
                Toast.makeText(this, "Please enter a character name", Toast.LENGTH_SHORT).show()
            }
        }

        characterButton.setOnClickListener {
            // Increment offset for the next character
            offset++
            getCharacterData() // This will fetch a new character each time the button is clicked

        }

        getCharacterData() // Fetch the initial character data when the app starts
    }



    private fun getCharacterData(query: String = "" ){
        val encodedQuery = URLEncoder.encode(query, "UTF-8")
        val timestamp = System.currentTimeMillis()
        val url = if (query.isNotEmpty()){
            "https://gateway.marvel.com/v1/public/characters?nameStartsWith=$encodedQuery&limit=1&ts=$timestamp&apikey=0bff0d5f2a8aef9c2fa3c961be718c51&hash=715dd55a201345eff232ce852580d415"
        } else  {
            "https://gateway.marvel.com/v1/public/characters?limit=1&offset=$offset&ts=$timestamp&apikey=0bff0d5f2a8aef9c2fa3c961be718c51&hash=715dd55a201345eff232ce852580d415"
        }
        Log.d("GeneratedURL", "Requesting URL: $url")

        val client = AsyncHttpClient()
        client.get(url, object : JsonHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Headers,
                    response: JsonHttpResponseHandler.JSON
                ) {
                    // Called when response HTTP status is "200 OK"

                    try {
                        // Parsing JSON data
                        val jsonResponse = response.jsonObject
                        val data = jsonResponse.getJSONObject("data")
                        val results = data.getJSONArray("results")
                        if (results.length() == 0) {
                            Toast.makeText(
                                this@MainActivity,
                                "No characters found.",
                                Toast.LENGTH_LONG
                            ).show()
                            return
                        }
                        val character = results.getJSONObject(0)

                        if (character.has("thumbnail") && !character.isNull("thumbnail")) { // Check if the thumbnail JSONObject exists
                            val thumbnail = character.getJSONObject("thumbnail")
                            val imagePath =
                                thumbnail.getString("path") + "." + thumbnail.getString("extension")

                            // Set the character image
                            Glide.with(this@MainActivity).load(imagePath).into(imageView)

                            // Make sure the ImageView is visible if it was previously hidden
                            imageView.visibility = View.VISIBLE
                        } else {
                            // Hide the ImageView if there is no thumbnail
                            imageView.visibility = View.GONE
                        }


                        // Set the character information
                        val characterName = character.getString("name")
                        val characterId = character.getString("id")
                        val characterDescription = character.getString("description")
                        val comics = character.getJSONObject("comics")
                        val comicsItems = comics.getJSONArray("items")
                        val comicsName = StringBuilder()
                        for (i in 0 until comicsItems.length()) {
                            comicsName.append(comicsItems.getJSONObject(i).getString("name"))
                                .append("\n")
                        }

                        characterNameTextView.text = characterName
                        characterIdTextView.text = characterId
                        configureComicsNameTextView(comicsName?.toString())
                        characterDescriptionTextView.text = characterDescription
                        if (query.isEmpty()) {
                            offset++ // only increment offset if it's not a search query
                        }

                        if (data.getInt("total") == 0) {
                            runOnUiThread {
                                Toast.makeText(
                                    this@MainActivity,
                                    "No character found with the name: $query",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    response: String?,
                    throwable: Throwable?
                ) {
                    when (statusCode) {
                        409 -> {
                            // Handle missing parameters
                            val errorMessage = when {
                                response?.contains("Missing API Key") == true -> "API Key is missing. Please check your request."
                                response?.contains("Missing Hash") == true -> "Hash is missing. Please check your request."
                                response?.contains("Missing Timestamp") == true -> "Timestamp is missing. Please check your request."
                                else -> "There was an error in your request. Please check your parameters."
                            }
                            runOnUiThread {
                                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG)
                                    .show()
                            }
                        }

                        401 -> {
                            // Handle invalid parameters or unauthorized access
                            val errorMessage = when {
                                response?.contains("Invalid Referer") == true -> "Invalid referer. Please check your request headers."
                                response?.contains("Invalid Hash") == true -> "Invalid hash. Please ensure your hash value is correct."
                                else -> "Unauthorized request. Please check your authentication parameters."
                            }
                            runOnUiThread {
                                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG)
                                    .show()
                            }
                        }

                        405 -> {
                            runOnUiThread {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Method Not Allowed. Please check if the HTTP method is correct for this endpoint.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                        403 -> {
                            runOnUiThread {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Forbidden. You do not have access to this endpoint.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                        else -> {
                            // General error message
                            runOnUiThread {
                                Toast.makeText(
                                    this@MainActivity,
                                    "An error occurred: $response",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                    // Log for debugging purposes
                    Log.d("API Error", "Error response: $response StatusCode: $statusCode")
                    throwable?.printStackTrace()
                    Log.d("API Error", "Error message: ${throwable?.message}")
                    }
                  })
                }



    private fun configureComicsNameTextView(comicsName: String?) {
        val textToShow = if (comicsName.isNullOrEmpty()) {
            "No comics available."  // Placeholder text
        } else {
            comicsName
        }

        comicsNameTextView.apply {
            text = textToShow
            maxLines = 2
            ellipsize = TextUtils.TruncateAt.END
        }
    }




    override fun onDestroy() {
        super.onDestroy()
        // Clearing Glide requests to prevent memory leaks
        Glide.with(this).clear(imageView)
    }
}


