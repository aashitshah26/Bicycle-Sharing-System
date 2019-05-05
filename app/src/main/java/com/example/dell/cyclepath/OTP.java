package com.example.dell.cyclepath;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OTP extends AppCompatActivity
{

    EditText ed1,ed2,ed3,ed4,ed5;
    Button btn;
    String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        final String otp = getIntent().getStringExtra("otp");
        final String num = getIntent().getStringExtra("number");

        ed1 = (EditText) findViewById(R.id.ed_1);
        ed2 = (EditText) findViewById(R.id.ed_2);
        ed3 = (EditText) findViewById(R.id.ed_3);
        ed4 = (EditText) findViewById(R.id.ed_4);
        ed5 = (EditText) findViewById(R.id.ed_5);

        ed1.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(ed1.getText().toString().length()==1)
                {
                    ed2.requestFocus();

                }

            }


            @Override
            public void afterTextChanged(Editable editable) {
                if(ed1.getText().toString().length()==1)
                {
                    ed2.requestFocus();

                }
            }
        });


        ed2.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(ed2.getText().toString().length()==1)
                {
                    if(ed1.getText().toString().length()==0)
                    {
                        ed2.setText("");
                        ed1.requestFocus();
                    }
                    else
                        ed3.requestFocus();

                }
                else
                    ed1.requestFocus();




            }


            @Override
            public void afterTextChanged(Editable editable) {
                if(ed2.getText().toString().length()==0)
                {
                    if(ed1.getText().toString().length()==0)
                    {
                        ed1.requestFocus();
                    }
                    ed1.requestFocus();

                }


            }
        });

        ed3.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(ed1.getText().toString().length()==0)
                {
                    ed1.requestFocus();
                }
                else if(ed2.getText().toString().length()==0)
                {
                    ed2.requestFocus();
                }
            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(ed3.getText().toString().length()==1)
                {
                    if(ed1.getText().toString().length()==0)
                    {
                        ed1.requestFocus();
                    }
                    else if(ed2.getText().toString().length()==0)
                    {
                        ed1.requestFocus();
                    }
                    else
                        ed4.requestFocus();

                }

            }


            @Override
            public void afterTextChanged(Editable editable) {

                if(ed3.getText().toString().length()==0)
                {
                    ed2.requestFocus();

                }

            }
        });

        ed4.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(ed1.getText().toString().length()==0)
                {
                    ed1.requestFocus();
                }
                else if(ed2.getText().toString().length()==0)
                {
                    ed2.requestFocus();
                }
                else if(ed3.getText().toString().length()==0)
                {
                    ed3.requestFocus();
                }
            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(ed4.getText().toString().length()==1)
                {
                    if(ed1.getText().toString().length()==0)
                    {
                        ed1.requestFocus();
                    }
                    else if(ed2.getText().toString().length()==0)
                    {
                        ed1.requestFocus();
                    }
                    else if(ed3.getText().toString().length()==0)
                    {
                        ed3.requestFocus();
                    }
                    else
                        ed5.requestFocus();

                }


            }


            @Override
            public void afterTextChanged(Editable editable) {
                if(ed4.getText().toString().length()==0)
                {

                    ed3.requestFocus();

                }

            }
        });

        ed5.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(ed1.getText().toString().length()==0)
                {
                    ed1.requestFocus();
                }
                else if(ed2.getText().toString().length()==0)
                {
                    ed2.requestFocus();
                }
                else if(ed3.getText().toString().length()==0)
                {
                    ed3.requestFocus();
                }
                else if(ed4.getText().toString().length()==0)
                {
                    ed4.requestFocus();
                }
            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


            @Override
            public void afterTextChanged(Editable editable) {
                if(ed5.getText().toString().length()==0)
                {

                    if(ed1.getText().toString().length()==0)
                    {
                        ed1.requestFocus();
                    }
                    else if(ed2.getText().toString().length()==0)
                    {
                        ed1.requestFocus();
                    }
                    else if(ed3.getText().toString().length()==0)
                    {
                        ed3.requestFocus();
                    }
                    ed4.requestFocus();

                }
            }
        });


        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(ed5.getText().toString().length()==1)
                {

                    code=ed1.getText().toString()+""+ed2.getText().toString()+""+ed3.getText().toString()+""+ed4.getText().toString()+""+ed5.getText().toString();

                }

                if(code.equals(otp))
                {
                    Intent i = new Intent(OTP.this,Register.class);
                    i.putExtra("number",num);
                    startActivity(i);

                }
            }
        });

    }

    }
