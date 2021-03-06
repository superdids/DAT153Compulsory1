package com.example.didrik.compulsory1v3.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.example.didrik.compulsory1v3.persistence.ApplicationDatabase;
import com.example.didrik.compulsory1v3.persistence.Person;
import com.example.didrik.compulsory1v3.R;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * A custom ArrayAdapter implementation, to render images in a ListView.
 * @author Didrik Emil Aubert
 * @author Ståle André Mikalsen
 * @author Viljar Buen Rolfsen
 */
public class ImageAdapter extends ArrayAdapter<Person> {

    /**
     * The list which the ListView shall be associated with.
     */
    private ArrayList<Person> personList;

    /**
     * The context of the activity initializing an instance of this class.
     */
    private Context context;

    /**
     * Database operations to be used when the delete button is pressed.
     */
    private ApplicationDatabase myDB;

    /**
     * Constructor assigning values to the field variables and invoking the
     * constructor of the superclass.
     * @param context The context of the activity initializing an instance of this class.
     * @param id The id of the ListView.
     * @param personList The list which the ListView shall be associated with.
     */
    public ImageAdapter(Context context, int id, ArrayList<Person> personList,
                        ApplicationDatabase myDB) {
        super(context, id, personList);
        this.context = context;
        this.personList = personList;
        this.myDB = myDB;
    }

    /**
     * Renders an image in an entry in the ListView.
     * @param position The index of the item/person.
     * @param view The item to be rendered.
     * @param parent The parent of the item, being the ListView.
     * @return The item (modified) to be rendered.
     */
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
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
                InputStream stream;
                try {
                    Uri uri = Uri.parse(person.getUriString());
                    stream = context.getContentResolver().openInputStream(uri);

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
                } catch (Exception e) {
                    throw new Error(e);
                }
            }
            Button button = (Button) v.findViewById(R.id.deletePersonImageButton);
            button.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                     public void onClick(View v) {
                                            myDB.deletePerson(personList.get(position).getName());
                                             personList.remove(position);
                                             ImageAdapter.this.notifyDataSetChanged();
                                         }
                                      }
            );
        }
        return v;
    }
}
