package com.example.googleclaudvision;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.FaceAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import org.apache.commons.io.output.ByteArrayOutputStream;

import java.util.Arrays;
import java.util.List;

public class FaceDetection2 extends AppCompatActivity {
    ImageView imageView;
    Button button;
    public Vision vision;
    public ImageView imagen;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detection2);

        imagen = (ImageView)findViewById(R.id.imageView);
        Vision.Builder visionBuilder = new Vision.Builder(new NetHttpTransport(),
                new AndroidJsonFactory(),  null);
        visionBuilder.setVisionRequestInitializer(new
                VisionRequestInitializer("AIzaSyCV_ADpVQp5_K1CQ98gc6KeOVq5p1sjqKQ"));
        vision = visionBuilder.build();

        imageView=(ImageView)findViewById(R.id.imageView);
        button=(Button)findViewById(R.id.BtnCargarImagen);
    }

    public void buttonClickFace(View v){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ImageView imagen=(ImageView)findViewById(R.id.imageView);
                BitmapDrawable drawable = (BitmapDrawable) imagen.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                bitmap = scaleBitmapDown(bitmap, 1200);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                byte[] imageInByte = stream.toByteArray();


                Image inputImage = new Image();
                inputImage.encodeContent(imageInByte);

                Feature desiredFeature = new Feature();
                desiredFeature.setType("FACE_DETECTION");

                //3 arma la solicitud
                AnnotateImageRequest request = new AnnotateImageRequest();
                request.setImage(inputImage);
                request.setFeatures(Arrays.asList(desiredFeature));

                BatchAnnotateImagesRequest batchRequest =  new BatchAnnotateImagesRequest();
                batchRequest.setRequests(Arrays.asList(request));


                try {
                    //4 asignamos el control visionbuilder la solicitud
                    Vision.Images.Annotate annotateRequest;
                    annotateRequest = vision.images().annotate(batchRequest);

                    //5 enviamos la solicitud
                    annotateRequest.setDisableGZipContent(true);
                    BatchAnnotateImagesResponse batchResponse  = annotateRequest.execute();


                    List<FaceAnnotation> faces = batchResponse .getResponses()
                            .get(0).getFaceAnnotations();
                    int numberOfFaces = faces.size();
                    String likelihoods = "";
                    for(int i=0; i<numberOfFaces; i++) {
                        likelihoods += "\n It is " +  faces.get(i).getJoyLikelihood() +
                                " that face " + i + " is happy";
                    }
                    final String message =
                            "This photo has " + numberOfFaces + " faces" + likelihoods;
                    //return message.toString();

                    //6 obtener la respuesta
                    /*TextAnnotation text = batchResponse .getResponses().get(0).getFullTextAnnotation();
                    final String res= text.getText();*/
                    //7 asignar la respuesta a la ui
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView imageDetail = (TextView)findViewById(R.id.textView2);
                            imageDetail.setText(message);
                        }});

                }catch (Exception e){
                    e.printStackTrace();
                }



            }
        });

    }

    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;
        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {        resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }    return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }
    public void botonClickCargar(View view){
        openGallery();
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            imagen.setImageURI(imageUri);
        }
    }
}
