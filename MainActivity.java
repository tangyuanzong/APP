package com.example.tyz;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.*;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {
    private EditText username;
    private EditText password;
    private TextView err;
    private Button button1;
    private Button button2;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        err    = (TextView)findViewById(R.id.message);
        button1  = (Button)findViewById(R.id.login);
        button2  = (Button)findViewById(R.id.reg);
        
    }

   public void sign(View view){    //登录按键的响应按键
	      String user = username.getText().toString();
	      String pass = password.getText().toString();
	      if(user.isEmpty()||pass.isEmpty()){
	    	  err.setText("用户名或密码为空");
	    	  err.setVisibility(View.VISIBLE);
	      }
	      else{
	    	  err.setText(null);
	    	  err.setVisibility(View.INVISIBLE);
	    	  Toast.makeText(this,"正在登录",Toast.LENGTH_SHORT).show();
	    	  new SignInProcess().execute(user,pass);
	      }
   }
   
   
   public void signUp(View view) {   //注册按键的响应按键
       Intent intent = new Intent(MainActivity.this, Register.class);
       startActivity(intent);
   }

   
   private class SignInProcess extends AsyncTask<String, String, String> {
       @Override
       protected String doInBackground(String... params) {
    	  
           String username = params[0];
           String password = params[1];
           String result = "";
           String s_url = "http://tyz.s1.natapp.cc/tyz_back/singin?username="+username+"&password="+password;
           try {
               URL url = new URL(s_url);
               HttpURLConnection conn = (HttpURLConnection) url.openConnection();
               conn.setRequestMethod("GET");
               conn.connect();
               InputStream is = conn.getInputStream();
               InputStreamReader reader = new InputStreamReader(is, "UTF-8");
               int temp;
               while((temp=reader.read()) != -1) {
                   result += (char)temp;
               }
           } catch(Exception e) {
               e.printStackTrace();
           }
           
           return result;
       }

	@Override
       protected void onPostExecute(String result) {
           try {
               JSONObject result_json = new JSONObject(result);
               if(result_json.has("error")) {
                   String error_code;
                   error_code = result_json.getString("error");
                   err.setText(error_code);
                   err.setVisibility(View.VISIBLE);
                   password.setText(null);
               } else {
                   SignInSuccess(result_json);
               }
           } catch (Exception e) {
               e.printStackTrace();
           }
       }

   }

   private void SignInSuccess(JSONObject info) {
       Intent intent = new Intent(this, Welcome.class);
       Toast.makeText(this,"登录成功",Toast.LENGTH_SHORT).show();
       try {
           intent.putExtra("username", info.getString("username"));
           intent.putExtra("name", info.getString("name"));
           intent.putExtra("age", info.getInt("age"));
           intent.putExtra("teleno", info.getString("teleno"));
       } catch (Exception e) {
           e.printStackTrace();
       }
       startActivity(intent);
   }

}
