package com.lamfee.sas;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.View.*;

public class Tab3Fragment extends Fragment {
    public static final String TAG = "Tab3Fragment";
    private String[] dummyStrings;
    private String[] dummyDes;
    private int[] myImageList;
    private int[] colors;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab3,container,false);
        ListView listView = (ListView) view.findViewById(R.id.list_tab3);
        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.frnlyt);
        if(SafetyMode.isSafe){
            frameLayout.setBackgroundColor(Color.parseColor("#d1efa7"));
        } else {
            frameLayout.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.layoutred));
        }
        dummyStrings = new String[]{"Send a feedback!","Rate it","Share this with your friends!","Donate and Remove ads","License"};
        dummyDes = new String[]{"Send us any questions or feedback you have!","Rate Us On The Play Store!","Share this app with your family and friends to help them too!","Donate and support the Developer","Application License"};
        // myImageList = getResources().getIntArray(R.array.img_id_arr);
        myImageList = new int[]{R.drawable.ic_assignment_white_24dp, R.drawable.ic_star_white_24dp,R.drawable.ic_share_white_24dp,R.drawable.ic_favorite_white_24dp,R.drawable.ic_note_white_24dp,R.drawable.ic_action_tick};
        colors= new int[]{Color.rgb(0,206,209),Color.rgb(60,179,113),R.color.Safe, Color.rgb(255,99,71),Color.rgb(147,112,219)};


        CostumeAdapter costumeAdapter = new CostumeAdapter();
        listView.setAdapter(costumeAdapter);
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.simple_item_text, dummyStrings);
        //listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Toast.makeText(getContext(), "Coming Soon!", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getContext(), "Coming Soon!", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getContext(), "Coming Soon!", Toast.LENGTH_SHORT).show();
                        break;
                    case 3: {
                        Toast.makeText(getContext(), "Coming Soon!", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 4: {
                        Toast.makeText(getContext(), "Copyright © 2017 by Lamfee", Toast.LENGTH_SHORT).show();
                    }
                }



            }
        });

        return  view;
    }
    private class CostumeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dummyStrings.length ;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflate(getContext(), R.layout.costume_list, null);
            TextView title = (TextView) convertView.findViewById(R.id.textView_title);
            title.setText(dummyStrings[position]);
            TextView description= (TextView) convertView.findViewById(R.id.textView_description);
            description.setText(dummyDes[position]);
            ImageView img = (ImageView) convertView.findViewById(R.id.icon_img);
            img.setImageResource(myImageList[position]);
            RelativeLayout imgIcon = (RelativeLayout) convertView.findViewById(R.id.CircleLayout);
            GradientDrawable backgroundGradient = (GradientDrawable)imgIcon.getBackground();
            backgroundGradient.setColor(colors[position]);
            return convertView;
        }
    }
}
