package com.example.googleclaudvision;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.TextAnnotation;

import org.apache.commons.io.output.ByteArrayOutputStream;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    public Vision vision;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView facedetection= findViewById(R.id.FaceDetection);
        TextView facedetection2 =findViewById(R.id.FaceDetection1);
        TextView labeldetection= findViewById(R.id.LabelDetection);
        TextView textdetection= findViewById(R.id.TextDetection);


        textdetection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detectionText = new Intent (MainActivity.this, TextDetection.class);
                MainActivity.this.startActivity(detectionText);
                finish();
            }
        });

        facedetection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detectionFace = new Intent (MainActivity.this, FaceDetection.class);
                MainActivity.this.startActivity(detectionFace);
                finish();
            }
        });

        facedetection2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detectionFace2 = new Intent (MainActivity.this, FaceDetection2.class);
                MainActivity.this.startActivity(detectionFace2);
                finish();
            }
        });

        labeldetection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Labeldetection = new Intent (MainActivity.this, LabelDetection.class);
                MainActivity.this.startActivity(Labeldetection);
                finish();
            }
        });


    }

}
