package com.example.googleclaudvision;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

;import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

public class FaceDetection extends AppCompatActivity {


    private TextView scanResults;
  //  private ImageView imageView;


    ImageView imageView;
    Button button1, button, button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detection);


      final Paint boxPaint =new Paint();
        boxPaint.setStrokeWidth(5);
        boxPaint.setColor(Color.GREEN);
        boxPaint.setStyle(Paint.Style.STROKE);

        button1= findViewById(R.id.tomarfoto);
        imageView= findViewById(R.id.imageViewid);
      //  button = findViewById(R.id.procesar);
        scanResults = (TextView) findViewById(R.id.results);
        button2 = findViewById(R.id.procesar2);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);

            }
        });


        final Bitmap bitmap =BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.face);/// aqui debe ir el imageView pero la conversion
        imageView.setImageBitmap(bitmap);

        final Bitmap tempBitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),Bitmap.Config.RGB_565);
        final Canvas canvas =new Canvas(tempBitmap);
        canvas.drawBitmap(bitmap,0,0,null);

       button2.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                FaceDetector faceDetector =new FaceDetector.Builder(getApplicationContext())
                        .setTrackingEnabled(false)
                        .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                        .setMode(FaceDetector.FAST_MODE)
                        .build();

                if (!faceDetector.isOperational()){

                    Toast.makeText(FaceDetection.this,"Face Detection ",Toast.LENGTH_SHORT).show();
                return;
                }

                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<com.google.android.gms.vision.face.Face> sparseArray =faceDetector.detect(frame);

                for (int i=0; i<sparseArray.size();i++){

                    Face face =sparseArray.valueAt(i);
                    float x1=face.getPosition().x;
                    float y1=face.getPosition().y;
                    float x2 =x1+face.getWidth();
                    float y2 =y1+face.getHeight();
                    RectF rectF = new RectF(x1,y1,x2,y2);
                    canvas.drawRoundRect(rectF,2,2,boxPaint);
                }
                    imageView.setImageDrawable(new BitmapDrawable(getResources(),tempBitmap));
            }
        });

    }


 protected  void onActivityResult (int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode,resultCode,data);
        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        imageView.setImageBitmap(bitmap);

    }


}
