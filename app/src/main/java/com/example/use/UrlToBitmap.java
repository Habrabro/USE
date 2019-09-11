package com.example.use;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static androidx.constraintlayout.widget.Constraints.TAG;

class UrlToBitmap extends AsyncTask<String, Void, Bitmap>
{
    private OnBitmapTaskExecutedListener listener;

    public UrlToBitmap(@NonNull OnBitmapTaskExecutedListener listener)
    {
        this.listener = listener;
    }

    @Override
    protected Bitmap doInBackground(String... params)
    {
        try
        {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        }
        catch (Exception e)
        {
            Log.d(TAG,e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result)
    {
        listener.onBitmapTaskExecuted(result);
    }
}
