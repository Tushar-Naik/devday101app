package com.example.tusharnaik.devday101app;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class PostMessage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_message);
        final EditText name = (EditText) findViewById(R.id.editTextName);
        final EditText id = (EditText) findViewById(R.id.editTextId);
        final EditText[] message = {(EditText) findViewById(R.id.editTextMessage)};
        Button post = (Button) findViewById(R.id.buttonPost);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String authorName = name.getText().toString();
                final String authorId = id.getText().toString();
                final String postMessage = message[0].getText().toString();

                final boolean[] success = new boolean[1];
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String url = "http://webapp101.azurewebsites.net/Home/CreateAuthor";
                        HttpPost httpPost = new HttpPost(url);
                        try {
                            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                            pairs.add(new BasicNameValuePair("Id", authorId));
                            pairs.add(new BasicNameValuePair("Name", authorName));
                            pairs.add(new BasicNameValuePair("Message", postMessage));
                            httpPost.setEntity(new UrlEncodedFormEntity(pairs));

                            DefaultHttpClient httpClient = new DefaultHttpClient();

                            HttpResponse response = httpClient.execute(httpPost);
                            HttpEntity entity = response.getEntity();

                            if (entity != null && (response.getStatusLine().getStatusCode() == 201 || response.getStatusLine().getStatusCode() == 200)) {
                                success[0] = true;
                            } else {
                                success[0] = false;
                            }
                        } catch (ClientProtocolException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(success[0]){
                    Toast toast = Toast.makeText(PostMessage.this, "Succussfully posted", Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                }
                else{
                    Toast toast = Toast.makeText(PostMessage.this, "Error hitting the endpoint: status", Toast.LENGTH_SHORT);
                    toast.show();
                    finish();

                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
