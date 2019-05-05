package com.example.dell.cyclepath;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Myrides_adapter extends BaseAdapter implements AdapterView.OnItemClickListener {
   public ArrayList<Rides> arrayList;
    Context context;
    LayoutInflater layoutInflater = null;


    public Myrides_adapter(Context con, ArrayList<Rides> rides)
    {    Log.e("Myadapter","entered in adapter");
        context=con;
        arrayList=rides;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Rides getItem(int position) {

        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(context, "Position is:"+position, Toast.LENGTH_SHORT).show();
    }

    private static class ViewHolder{
        TextView dateday,start1,end1,fare,cal,pol,time1,time2;
    }
    ViewHolder viewHolder = null;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {




        View view=convertView;
        final int pos = position;
        Log.e("Myadapter","entered in view of adapter");
        if(view==null)
        {

            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(context);
            //convertView = inflater.inflate(R.layout.list_xml, parent, false);
            // inflate list_rowcell for each row
            view = layoutInflater.inflate(R.layout.list_xml,null);
            viewHolder.start1 = (TextView) view.findViewById(R.id.start);
            viewHolder.end1 = (TextView) view.findViewById(R.id.end);
            viewHolder.time1=(TextView) view.findViewById(R.id.time1);
            viewHolder.time2=(TextView) view.findViewById(R.id.time2);
            viewHolder.dateday = (TextView) view.findViewById(R.id.dateday);
            viewHolder.fare = (TextView) view.findViewById(R.id.fare);
            viewHolder.cal = (TextView) view.findViewById(R.id.cal);
            viewHolder.pol = (TextView) view.findViewById(R.id.poll);



            /*We can use setTag() and getTag() to set and get custom objects as per our requirement.
            The setTag() method takes an argument of type Object, and getTag() returns an Object.*/
            view.setTag(viewHolder);
        }else {

            /* We recycle a View that already exists */
            viewHolder= (ViewHolder) view.getTag();
        }

        viewHolder.start1.setText(arrayList.get(pos).getstartloc());
        viewHolder.end1.setText(arrayList.get(pos).getendloc());
        viewHolder.time1.setText(arrayList.get(pos).getstart());
        viewHolder.time2.setText(arrayList.get(pos).getend());
        viewHolder.dateday.setText(arrayList.get(pos).getdate()+","+arrayList.get(pos).getday());
        viewHolder.fare.setText("Rs."+arrayList.get(pos).getfare());
        viewHolder.cal.setText(arrayList.get(pos).getcal());
        viewHolder.pol.setText(arrayList.get(pos).getpol());
        return view;
    }

}
