
package uk.co.dazcorp.android.upcomingdvds;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONArrayAdapter extends ArrayAdapter<JSONObject> {
    private int mResource;
    private LayoutInflater mInflater;
    protected String[] mFrom;
    protected int[] mTo;

    public JSONArrayAdapter(Context context, int textViewResourceId, ArrayList<JSONObject> objects,
            String[] from, int[] to) {
        super(context, textViewResourceId, 0, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResource = textViewResourceId;
        mFrom = from;
        mTo = to;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = mInflater.inflate(mResource, parent, false);
        } else {
            view = convertView;
        }
        JSONObject item = (JSONObject) getItem(position);
        bindView(view, item);

        return view;
    }

    /**
     * a la bindView of {@link CursorAdapter} TODO allow for more complex
     * bindings
     */
    public void bindView(View view, JSONObject json) {
        final int count = mTo.length;
        final String[] from = mFrom;
        final int[] to = mTo;

        for (int i = 0; i < count; i++) {
            final View v = view.findViewById(to[i]);
            if (v != null) {
                String text = "";
                text = getText(json, from[i]);

                if (v instanceof TextView) {
                    setViewText((TextView) v, text);
                } else if (v instanceof ImageView) {
                    setViewImage((ImageView) v, text);
                } else {
                    throw new IllegalStateException(v.getClass().getName() + " is not a "
                            + " view that can be bound by this JSONArrayAdapter");
                }
            }
        }
    }
    
    public String getText(JSONObject json, String from){
        if (json.has(from)){
            try {
                return json.getString(from);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;

    }

    public void setViewImage(ImageView v, String value) {
        try {
            v.setImageResource(Integer.parseInt(value));
        } catch (NumberFormatException nfe) {
            v.setImageURI(Uri.parse(value));
        }
    }

    public void setViewText(TextView v, String text) {
        v.setText(text);
    }

}
