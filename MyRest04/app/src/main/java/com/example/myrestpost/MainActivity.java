package com.example.myrestpost;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView tv_outPut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 위젯에 대한 참조.
        tv_outPut = (TextView) findViewById(R.id.tv_outPut);
    }

    public void onRequestPost(View v){
        // URL 설정.
        //String url = "http://hmkcode.appspot.com/jsonservlet";
        String url = "http://192.168.219.129:8081";

        // AsyncTask를 통해 HttpURLConnection 수행.
        new PostMethodDemo().execute(url);
    }


    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    public class PostMethodDemo extends AsyncTask<String , Void ,String> {
        String server_response;

        @Override
        protected String doInBackground(String... strings) {

            URL url;
            HttpURLConnection urlConnection = null;

            try {
                String str = "{"
                        + "		user_id:'claude',"
                        + "		msg:'안녕'"
                        + "}" ;
                JSONObject jo = new JSONObject(str);

                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.getOutputStream().write(jo.toString().getBytes("UTF-8")); // POST 호출

                int responseCode = urlConnection.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK){
                    server_response = readStream(urlConnection.getInputStream());
                    Log.v("CatalogClient", server_response);
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
        /*
// HTTP POST request
    private void sendPost(String targetUrl, String parameters) throws Exception {
            URL url = new URL(targetUrl);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("POST"); // HTTP POST 메소드 설정
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setDoOutput(true); // POST 파라미터 전달을 위한 설정
        // Send post request
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(parameters);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close(); // print result
            System.out.println("HTTP 응답 코드 : " + responseCode);
            System.out.println("HTTP body : " + response.toString());
        }

*/
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("Response", "" + server_response);
            tv_outPut.setText(server_response);


        }
    }
}
