package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Manoj on 12/29/2016.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    public EarthquakeAdapter(Context context, List<Earthquake> earthquakes) {
        super(context, 0,earthquakes); // '0' IS POSITION (not sure)

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.earthquake_list_item, parent, false);
        }

        Earthquake currentEarthqakeObj = getItem(position);


        double magnitude = currentEarthqakeObj.getmMagnitude();
        String strMag = formatMag(magnitude);


        TextView magView = (TextView) listItemView.findViewById(R.id.magnitude);
        magView.setText(strMag);

        GradientDrawable magnitudeCircle = (GradientDrawable) magView.getBackground();
        int magnitudeColor = getMagnitudeColor(currentEarthqakeObj.getmMagnitude());
        magnitudeCircle.setColor(magnitudeColor);



        String part1;
        String part2;

        String str = currentEarthqakeObj.getmLocation();

        if (str.contains("of")) {
            // Split it.
            String[] parts = str.split("of");
             part1 = parts[0]+" of"; //first part
             part2 = parts[1]; // second part
        } else {

            part1 = "Near the";
            part2 = str;

        }

        TextView offsetView = (TextView) listItemView.findViewById(R.id.offset_location);
        offsetView.setText(part1);


        TextView locView = (TextView) listItemView.findViewById(R.id.primary_location);
        locView.setText(part2);



       // DATE OBJECT TO CONVERT LONG MILI-SECONDS INTO DATE & TIME
        Date dateObject = new Date(currentEarthqakeObj.getmTime());

        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        String formattedDate = formatDate(dateObject); // CALLING FORMATDATE FUNCTION
        dateView.setText(formattedDate);


        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        String formattedTime = formatTime(dateObject);
        timeView.setText(formattedTime);

        // return the list item that now shows the appropriate data
        return listItemView;
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(dateObject);
    }

    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    private String formatMag(double magnitude)
    {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }


}
