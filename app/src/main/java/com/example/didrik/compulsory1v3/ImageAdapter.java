package com.example.didrik.compulsory1v3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by didrik on 24.01.16.
 */
public class ImageAdapter extends ArrayAdapter<Person> {
    private ArrayList<Person> personList;
    private Context context;

    public ImageAdapter(Context context, int id, ArrayList<Person> personList) {
        super(context, id, personList);
        this.context = context;
        this.personList = personList;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View v = view;
        if (v == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = layoutInflater.inflate(R.layout.list_view_images, null);
        }
        Person person = personList.get(position);
        if (person != null) {
            ImageView imageView = (ImageView) v.findViewById(R.id.listViewImages);
            if (imageView != null) {
                InputStream stream = null;
                try {
                    //Uri uri = Uri.fromFile(new File(person.getPath()));
                    Uri uri = Uri.parse(person.getUriString());
                    stream = context.getContentResolver().openInputStream(uri);
                    assert stream != null;
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    float scaleWidth = (float) width / (float) 150;
                    float scaleHeight = (float) height / (float) 150;
                    float scale;
                    if (scaleWidth < scaleHeight) scale = scaleHeight;
                    else scale = scaleWidth;
                    bitmap = Bitmap.createScaledBitmap(bitmap, (int) (width / scale),
                            (int) (height / scale), true);
                    imageView.setImageBitmap(bitmap);
                    //  imageView.setImageBitmap(BitmapFactory.decodeStream(stream));
                } catch (Exception e) {
                    throw new Error(e);
                }
            }
        }
        return v;
    }
}
