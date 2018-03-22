package adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.models.LogHolder;
import com.example.andy.nessus_fenrir_callirrhoe.R;

import java.util.ArrayList;

/**
 * Created by Andy on 15/02/2018.
 */

public class LogListAdapter extends ArrayAdapter<LogHolder> implements View.OnClickListener{

    private ArrayList<LogHolder> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtType;
        TextView txtstatus;

    }

    public LogListAdapter(ArrayList<LogHolder> data, Context context) {
        super(context, R.layout.list_log, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        LogHolder logHolder=(LogHolder) object;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        LogHolder logHolder = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_log, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.sender);
            viewHolder.txtType = (TextView) convertView.findViewById(R.id.time);
            viewHolder.txtstatus= (TextView) convertView.findViewById(R.id.status);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        viewHolder.txtName.setText(logHolder.getName());
        viewHolder.txtType.setText(logHolder.getDate());
        viewHolder.txtstatus.setText(logHolder.getStatus());
        if (logHolder.getStatus().equals("Inbound")){
            viewHolder.txtstatus.setTextColor(Color.parseColor("#75a478"));
        }
        if(logHolder.getStatus().equals("Outbound")){
            viewHolder.txtstatus.setTextColor(Color.parseColor("#c63f17"));
        }

        // Return the completed view to render on screen
        return convertView;
    }
}