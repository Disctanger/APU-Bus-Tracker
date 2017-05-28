package united.powers.rttest.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import united.powers.rttest.R;

/**
 * Created by Disctanger on 2/26/2016.
 */
public class BusStopListAdapter extends BaseAdapter  {

    public List<String> results;
    String [] result;
    Context context;
    int [] imageId;
    private static LayoutInflater inflater=null;



    public BusStopListAdapter(Activity mainActivity, String[] prgmNameList ) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        context=mainActivity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return result[position];
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.bus_stop_list, null);
        holder.tv= (TextView) rowView.findViewById(android.R.id.text1);
        holder.img = (ImageView) rowView.findViewById(R.id.list_image);
        holder.img.setImageResource(R.drawable.stop_icon);
        holder.tv.setText(result[position]);
        return rowView;
    }
}
