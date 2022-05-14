package com.example.liberary.View.Activities

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity;
import com.example.liberary.R
import com.example.liberary.constants.Constants
import com.github.barteksc.pdfviewer.PDFView;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class PDFActivity : AppCompatActivity(), Connector {
    private lateinit  var pdfView: PDFView
    private lateinit var pdfurl:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdfactivity)
        pdfurl = intent.getStringExtra(Constants.reference)!!
        pdfView = findViewById(R.id.idPDFView);
        val asyncTask = RetrivePDFfromUrl(pdfurl)
        asyncTask.execute(pdfurl)
        asyncTask.delagate = this

    }
    class RetrivePDFfromUrl(private var pdfurl:String): AsyncTask<String, Void, InputStream>() {
        lateinit var delagate:Connector
        private  var inputStream: InputStream? = null
        override fun doInBackground(vararg p0: String?): InputStream? {

            try {
                val url = URL(pdfurl)
                // below is the step where we are
                // creating our connection.
                val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
                if (urlConnection.responseCode == 200) {

                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = BufferedInputStream(urlConnection.inputStream)
                }
                Log.d("error message",urlConnection.responseMessage)
                //Toast.makeText(this@PDFActivity,urlConnection.responseMessage,Toast.LENGTH_LONG).show()
                //

            } catch (e: IOException) {
                // this is the method
                // to handle errors.
                e.printStackTrace()
                return null
            }
            return inputStream
        }

        override fun onPostExecute(result: InputStream?) {
            super.onPostExecute(result)
            delagate.returnInputStream(inputStream)
        }


    }

    override fun returnInputStream(result: InputStream?) {
        if(result != null){
            pdfView.fromStream(result).load()
        }

    }

}
interface Connector{
    fun returnInputStream(result: InputStream?)
}