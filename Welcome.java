package com.example.tyz;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Welcome extends Activity {

	private TextView wel;
	private Button b1;
	private TextView un1;
	private TextView n1;
	private TextView a1;
	private TextView t1;
	
	private String temp_username;
	private String temp_name;
	private String temp_age;
	private String temp_teleno;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		b1 = (Button)findViewById(R.id.button1);
		un1 = (TextView)findViewById(R.id.username);
		n1 = (TextView)findViewById(R.id.name);
		a1 = (TextView)findViewById(R.id.age);
		wel = (TextView)findViewById(R.id.TextView1);
		t1 = (TextView)findViewById(R.id.teleno);
		
		Intent intent = getIntent();
		String n2 = intent.getStringExtra("name");
		String u2 = intent.getStringExtra("username");
		int a2 = intent.getIntExtra("age", 0);
		String teleno = intent.getStringExtra("teleno");
		
		wel.setText("欢迎 "+u2);
		temp_username = u2;
		temp_name     = n2;
		temp_age      = ""+a2;
		temp_teleno   = teleno;
	}
	
	public void check(View view){
			 un1.setText(temp_username);
			 n1.setText(temp_name);
			 a1.setText(temp_age);
			 t1.setText(temp_teleno);
	  }
	
   public void back(View view){ //返回登录界面
	   Intent intent = new Intent();
	   intent.setClass(Welcome.this, MainActivity.class);
	   startActivity(intent);
   }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) { 
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}

}
