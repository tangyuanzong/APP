package com.example.tyz;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends Activity {
	private Button b1;
	private Button b2;
    private EditText u1; 
    private EditText n1; 
    private EditText a1;
    private EditText t1;
    private EditText p1;
    private EditText p2;
    private TextView e1;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		b1  = (Button)findViewById(R.id.button1);
		b2  = (Button)findViewById(R.id.button2);
		u1  = (EditText)findViewById(R.id.username);
		n1  = (EditText)findViewById(R.id.name);
		a1  = (EditText)findViewById(R.id.age);
		t1  = (EditText)findViewById(R.id.teleno);
        p1  = (EditText)findViewById(R.id.password1);
        p2  = (EditText)findViewById(R.id.password2);
        e1  = (TextView)findViewById(R.id.mess);
        
        
	}
   
	public void check(View view){
		   String err    = e1.getText().toString();
		   String uname  = u1.getText().toString();
		   String name   = n1.getText().toString();
		   String age    = a1.getText().toString();
		   String teleno = t1.getText().toString();
		   String pass1  = p1.getText().toString();
		   String pass2  = p2.getText().toString();
		   
		   if(err.length()>0) e1.setVisibility(view.INVISIBLE);
		   
		   //检查username
		   String mess = "";
	       if(uname.length() < 5 || uname.length() > 10)
	            mess += " 用户名长度需5-10位 ";
	        String pattern1 = "^[a-zA-Z][a-zA-Z\\d_]*";
	        String pattern2 = ".*[A-Z].*";
	        boolean match1 = Pattern.matches(pattern1, uname);
	        boolean match2 = Pattern.matches(pattern2, uname);
	        if(!match1) {
	            mess += " 用户名需要以英文字母开头，由英文字母数字和_组成 ";
	        }
	        if(!match2) {
	            mess += " 用户名必须包含至少一个大写字母 ";
	        }
	        
	        if(!mess.isEmpty()) {
	        	e1.setText(mess);
			    e1.setVisibility(view.VISIBLE);
			    return ;
	        }
	        
		   //检查姓名
		   if(name.isEmpty()){
			    e1.setText(" 姓名为空 ");
			    e1.setVisibility(view.VISIBLE);
			    return ;
		   }
		   String pattern3 = "[a-zA-Z\\_]*";
		   boolean match3 = Pattern.matches(pattern3, name);
		   if(!match3){
			    e1.setText(" 姓名由字母和下划线组成 ");
			    e1.setVisibility(view.VISIBLE);
			    return ;
		   }
		   
		   //检查age
		   if(age.isEmpty()){
			    e1.setText(" 年龄为空 ");
			    e1.setVisibility(view.VISIBLE);
			    return ;
		   }
		   
		   String pattern4 = "^([1-9]\\d|\\d)$";
		   boolean match4 = Pattern.matches(pattern4, age);
		   if(!match4){
			    e1.setText(" 年龄只能为0-99的整数 ");
			    e1.setVisibility(view.VISIBLE);
			    return ;
		   }
		   
		   //检查电话
		   if(teleno.isEmpty()){
			    e1.setText(" 电话为空 ");
			    e1.setVisibility(view.VISIBLE);
			    return ;
		   }
		   String pattern5 = "(?:^(?:\\+86)?1(?:3[4-9]|4[7]|5[0-27-9]|7[8]|8[2-478])\\d{8}$)|(?:^(?:\\+86)?1705\\d{7}$)";
		   boolean match5 = Pattern.matches(pattern5, teleno);
		   if(!match5){
			    e1.setText(" 输入正确的电话号码 ");
			    e1.setVisibility(view.VISIBLE);
			    return ;
		   }
		   
		   //检查密码
		    mess = "";
	        if(pass1.length() < 6 || pass2.length() > 12 ||pass1.length()>12||pass2.length()<6)
	            mess += " 密码长度需6-12位 ";
	        if(!pass1.equals(pass2)){
	        	mess +=" 输入不一致 ";
	        }
	        String pattern6 = "[a-zA-Z\\d_]*";
	        boolean match6 = Pattern.matches(pattern6, pass1);
	        if(!match6) {
	            mess += " 密码应由英文字母数字和_组成 ";
	        }
	        if(!mess.isEmpty()) {
	        	e1.setText(mess);
			    e1.setVisibility(view.VISIBLE);
			    return ;
	        }
	        
		   else {
		       e1.setVisibility(view.INVISIBLE);
		       new SignUpProcess().execute(uname, pass1, name, age, teleno);
		   }
	}
	
	private class SignUpProcess extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            String name = params[2];
            String age = params[3];
            String teleno = params[4];
            String result = "";
            String s_url = "http://tyz.s1.natapp.cc/tyz_back/signup?username="+username+"&password="+password+"&name="+name+"&age="+age+"&telenum="+teleno;
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
                    e1.setText(error_code);
                    e1.setVisibility(View.VISIBLE);
                    p1.setText(null);
                    p2.setText(null);
                } else {
                    SignInSuccess(result_json);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
	
	
	private void SignInSuccess(JSONObject info) {
        Toast.makeText(this,"注册成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Welcome.class);
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
	
	public void back(View view){
		   Intent intent = new Intent();
		   intent.setClass(Register.this, MainActivity.class);
		   startActivity(intent);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

}
