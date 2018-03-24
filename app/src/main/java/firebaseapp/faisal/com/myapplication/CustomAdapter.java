package firebaseapp.faisal.com.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ussl-01 on 3/21/2018.
 */

public class CustomAdapter  extends BaseAdapter{
    private List<Student> stlist;
    private Context context;
    CustomAdapter(List<Student> stlist,Context context)
    {
        this.stlist=stlist;
        this.context=context;
    }

    @Override
    public int getCount() {
        return stlist.size();
    }

    @Override
    public Object getItem(int position) {
        return stlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        if(convertView==null)
        {
            convertView=inflater.inflate(R.layout.studentitemlayout,null);
        }
        TextView nameText=convertView.findViewById(R.id.txtstuname);
        TextView depText=convertView.findViewById(R.id.txtstdep);
        TextView contactText=convertView.findViewById(R.id.txtstcontact);

        nameText.setText(stlist.get(position).getName());
        depText.setText(stlist.get(position).getDepartment());
        contactText.setText(stlist.get(position).getContact());
        return convertView;
    }
}
