package com.example.weatherapp;


import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final List<String> itemname;
    private final List<Bitmap> imgid;

    public CustomListAdapter(Activity context, List<String> tempArray, List<String> itemname, List<Bitmap> imgid) {
        super(context,R.layout.date_list,itemname);

        this.context = context;
        this.itemname = itemname;
        this.imgid = imgid;
    }

    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.date_list,null,true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.itemname);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        txtTitle.setText(itemname.get(position));
        imageView.setImageBitmap(imgid.get(position));
        return rowView;
    };
}

//-----------------------------------

//package com.example.customlistapp;
//
//        import androidx.appcompat.app.AppCompatActivity;
//
//        import android.media.session.PlaybackState;
//        import android.os.Bundle;
//        import android.view.View;
//        import android.widget.AdapterView;
//        import android.widget.ListView;
//        import android.widget.Toast;
//
//public class MainActivity extends AppCompatActivity {
//
//    ListView list;
//    String[] text_list = {
//            "nm to mm",
//            "mm to cm",
//            "cm to m",
//            "m to km",
//            "cm to inches",
//            "inches to feet",
//            "km to miles"
//    };
//    Integer[] icon_list ={
//            R.drawable.pic_1,
//            R.drawable.pic_2,
//            R.drawable.pic_3,
//            R.drawable.pic_4,
//            R.drawable.pic_5,
//            R.drawable.pic_1,
//            R.drawable.pic_2
//    };
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        CustomListAdapter adapter = new CustomListAdapter(this, text_list,icon_list);
//        list = (ListView) findViewById(R.id.list_View);
//        list.setAdapter(adapter);
//
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String Slecteditem = text_list[+position];
//                Toast.makeText(getApplicationContext(),Slecteditem,Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//
//    }
//}
