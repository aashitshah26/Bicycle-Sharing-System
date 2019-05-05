package com.example.dell.cyclepath;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class Wallet_Fragment extends Fragment {
    View v;
    Button btn;
    MyInterface listener;


    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MyInterface) {
            listener = (MyInterface) context;
        } else {
            throw new RuntimeException("Context not Provided");
        }
    }

    public static EditText card_number, card_name, card_date, card_month, amount,cvv;
    int position;
    DBHandler dbHandler;
    SQLiteDatabase db;
    public String myresult;
    public static String final_date;
    HttpHandler httpHandler;
   public static ProgressBar progressBar;
    @SuppressLint("ValidFragment")
    public Wallet_Fragment(int i) {

        this.position = i;

    }

    public interface MyInterface {
        void getData(String url);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = getView();
        switch (position) {
            case 0: {

                v = inflater.inflate(R.layout.layout_credit_wallet, container, false);// inflating our fragment layout...
                btn = v.findViewById(R.id.dbtncredit);
                card_name = v.findViewById(R.id.dedttextcredit);
                card_number = v.findViewById(R.id.dedttextc);
                card_date = v.findViewById(R.id.dedtdate);
                card_month = v.findViewById(R.id.dedtmonth);
                amount = v.findViewById(R.id.dedtamount);
                progressBar=v.findViewById(R.id.dprogress);
                cvv=v.findViewById(R.id.dedtcvv);

                dbHandler = new DBHandler(this.getContext());
                db = dbHandler.getWritableDatabase();


                final String web_name = "";
                final String web_cardno = "";
                final String web_date = "";
                // listener.getData("https://psyclepath.000webhostapp.com/bank.php?card_no="+cardnumber);


                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final String cardnumber = card_number.getText().toString();
                        final String tempdate = card_date.getText().toString();
                        final String tempmonth = card_month.getText().toString();
                       /* try{tempintdate = (int) new Integer(tempdate).intValue();
                        }
                        catch (Exception e){}*/

                        final_date = tempdate + "/" + tempmonth;
                        final String cardname = card_name.getText().toString();
                        final String cardamount = amount.getText().toString();
                        final String cvvno = cvv.getText().toString();

                        if (cardname.equals("")) {
                            Toast.makeText(getContext(), "Please enter your name!!", Toast.LENGTH_SHORT).show();
                            card_name.setFocusable(true);
                        } else if (cardnumber.equals("")) {
                            Toast.makeText(getContext(), "Please enter your card number!!", Toast.LENGTH_SHORT).show();
                            card_number.setFocusable(true);
                        } else if (tempdate.equals("")) {
                            Toast.makeText(getContext(), "Please enter date!!", Toast.LENGTH_SHORT).show();
                            card_date.setFocusable(true);
                        } else if (tempmonth.equals("")) {
                            Toast.makeText(getContext(), "Please enter month!!", Toast.LENGTH_SHORT).show();
                            card_month.setFocusable(true);
                        } else if (cardamount.equals("")) {
                            Toast.makeText(getContext(), "Please enter amount!!", Toast.LENGTH_SHORT).show();
                            amount.setFocusable(true);
                        }
                        else if (cvvno.equals("")) {
                            Toast.makeText(getContext(), "Please enter cvv number!!", Toast.LENGTH_SHORT).show();
                            amount.setFocusable(true);
                        }else {
                        listener.getData("https://psyclepath.000webhostapp.com/bank.php?card_no=" + cardnumber);
                    }
                    }
                });
                break;
            }


            case 1: {

                v = inflater.inflate(R.layout.layout_credit_wallet, container, false);// inflating our fragment layout...
                btn = v.findViewById(R.id.dbtncredit);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast.makeText(getActivity(), "This is second fragment", Toast.LENGTH_SHORT).show();
                    }
                });

                break;
            }


            default: {

            }
        }

        return v;


    }


    
}
