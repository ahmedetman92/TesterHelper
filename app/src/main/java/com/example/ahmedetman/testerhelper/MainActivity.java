package com.example.ahmedetman.testerhelper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import static com.example.ahmedetman.testerhelper.Constants.MIME_TYPE;
import static com.example.ahmedetman.testerhelper.Constants.PICK_IMAGE_REQUEST;

public class MainActivity extends AppCompatActivity {

    private Button btnShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    /**
     * initializing the views
     */
    private void initViews() {
        btnShare = findViewById(R.id.btn_share);
        Button btnSelect = findViewById(R.id.btn_open);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
    }

    /**
     * Opening the gallery to allow the tester to select an image
     */
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType(MIME_TYPE);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.selecting_title)),
                PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null &&
                data.getData() != null) {

            final Uri uri = data.getData();
            try {
                //converting the selected image to bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ImageView imageView = findViewById(R.id.iv_preview);
                //displaying that image on the UI
                imageView.setImageBitmap(bitmap);
                if (uri != null) {
                    btnShare.setEnabled(true);
                }
                btnShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendViaEmail(Uri.parse("file://" + Helper.getRealPathFromURI(getApplication()
                                , uri)));
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * send selected image as attachment to an email with some extra info
     *
     * @param uri : holds the absolute value of the selected image
     */
    private void sendViaEmail(Uri uri) {
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_title));
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_TEXT, Helper.prepareExtraData(this));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            startActivity(Intent.createChooser(intent, "Send mail..."));
        } catch (Exception e) {
            System.out.println("is exception raises during sending mail" + e);
            Toast.makeText(this, "Can not send this email", Toast.LENGTH_SHORT).show();
        }
    }


}
