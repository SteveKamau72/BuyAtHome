package com.softtech.stevekamau.buyathome.activites;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.helper.ImageInputHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import cz.msebera.android.httpclient.Header;

public class AddNewProduct extends AppCompatActivity implements ImageInputHelper.ImageActionListener {
    Bitmap bitmap;
    ImageView image1;
    EditText edname, edamount, eddetails;
    ProgressDialog loading;
    Spinner spinner1;
    String tags;
    String rating, new_product, featured_product, recommended_product, phone_s, laptop_s, laptop_acc_s, phone_acc_s;
    CheckBox new_p, featured, recommended, phone, laptop, laptop_acc, phone_acc;

    private ImageInputHelper imageInputHelper;

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageInputHelper = new ImageInputHelper(this);
        imageInputHelper.setImageActionListener(this);

        image1 = (ImageView) findViewById(R.id.img1);

        edname = (EditText) findViewById(R.id.name);
        edamount = (EditText) findViewById(R.id.amount);
        eddetails = (EditText) findViewById(R.id.description);
        addListenerOnSpinnerItemSelection();

        new_p = (CheckBox) findViewById(R.id.new_p);
        featured = (CheckBox) findViewById(R.id.featured);
        recommended = (CheckBox) findViewById(R.id.recommended);
        laptop = (CheckBox) findViewById(R.id.laptop);
        laptop_acc = (CheckBox) findViewById(R.id.laptop_acc);
        phone = (CheckBox) findViewById(R.id.phone);
        phone_acc = (CheckBox) findViewById(R.id.phone_acc);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(AddNewProduct.this);

                if (new_p.isChecked()) {
                    new_product = "new_product";
                }
                if (featured.isChecked()) {
                    featured_product = "featured_product";
                }
                if (recommended.isChecked()) {
                    recommended_product = "recommended_product";
                }
                if (laptop.isChecked()) {
                    laptop_s = "phone";
                }
                if (laptop_acc.isChecked()) {
                    laptop_acc_s = "laptop_accessory";
                }
                if (phone.isChecked()) {
                    phone_s = "phone";
                }
                if (phone_acc.isChecked()) {
                    phone_acc_s = "phone_accessory";
                }
                tags = new_product + "," + featured_product + "," + recommended_product + "," +
                        phone_s + "," + laptop_s + "," + laptop_acc_s + "," + phone_acc_s;
                addProductRequest();
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img1:

                final CharSequence[] items = {
                        "From Gallery", "Take with camera"
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Make your selection");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection

                        if (items[item].equals("From Gallery")) {
                            imageInputHelper.selectImageFromGallery();
                        } else {
                            imageInputHelper.takePhotoWithCamera();
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                break;
        }
    }

    public void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rating = String.valueOf(spinner1.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageInputHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onImageSelectedFromGallery(Uri uri, File imageFile) {
        // cropping the selected image. crop intent will have aspect ratio 16/9 and result image
        // will have size 800x450
        imageInputHelper.requestCropImage(uri, 800, 800, 1, 1);
    }

    @Override
    public void onImageTakenFromCamera(Uri uri, File imageFile) {
        // cropping the taken photo. crop intent will have aspect ratio 16/9 and result image
        // will have size 800x450
        imageInputHelper.requestCropImage(uri, 800, 800, 1, 1);
    }

    @Override
    public void onImageCropped(Uri uri, File imageFile) {
        try {
            // getting bitmap from uri
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

            // showing bitmap in image view
            image1.setImageBitmap(bitmap);


        } catch (IOException e) {
            e.printStackTrace();
        }
        File filepath = Environment.getExternalStorageDirectory();

        // Create a new folder in SD Card
        File dir = new File(filepath.getAbsolutePath()
                + "/BuyAtHome/");
        dir.mkdirs();

        OutputStream output;
        // Create a name for the saved image
        File file = new File(dir, "product_image.png");

        try {

            output = new FileOutputStream(file);

            // Compress into png format image from 0% - 100%
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.flush();
            output.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    void addProductRequest() {

        String url = getResources().getString(R.string.url) + "add_new_product.php";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("name", edname.getText().toString());
        params.put("amount", edamount.getText().toString());
        params.put("details", eddetails.getText().toString());
        params.put("rating", rating);
        params.put("tags", tags);
        String imagePath = "/BuyAtHome/product_image.png";
        String final_path = Environment.getExternalStorageDirectory() + imagePath;
        File myFile = new File(final_path);
        try {
            params.put("image", myFile);
        } catch (FileNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Sorry Image not found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        client.post(url,
                params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        loading = ProgressDialog.show(AddNewProduct.this, "Uploading...", "Please wait...", false, false);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        loading.dismiss();
                        String s = new String(responseBody);
                        Log.d("product_created", s);
                        try {

                            JSONObject obj = new JSONObject(s);

                            Boolean success = obj.getBoolean("success");
                            if (success) {
                                String s_success = obj.getString("message");
                                Snackbar.make(findViewById(android.R.id.content), "Successfully added product", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                // productDB.addProduct("1", name, price, description, "", "", "");


                            } else {
                                String failure = obj.getString("message");
                                Snackbar.make(findViewById(android.R.id.content), "Our servers are down for maintainance", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }

                        } catch (JSONException e) {

                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        loading.dismiss();
                        Log.d("something_went_wrong", String.valueOf(error));
                        Snackbar.make(findViewById(android.R.id.content), "Check your internet connectivity", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                    }
                }

        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
