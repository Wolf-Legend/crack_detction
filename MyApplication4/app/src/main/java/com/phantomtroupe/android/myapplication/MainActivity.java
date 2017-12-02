package com.phantomtroupe.android.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import org.opencv.core.Mat;
import org.opencv.core.Core;
import org.opencv.android.Utils;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView ImageView1;
    ImageView ImageView2;
    Button btn1, btn2;
    Bitmap photo;
    int max =0;
    static int cr =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.loadLibrary("opencv_java");


        ImageView1 = (ImageView)findViewById(R.id.iv1);
        ImageView2 = (ImageView)findViewById(R.id.iv2);
        btn1 = (Button)findViewById(R.id.b1);


        btn1.setOnClickListener(this);
        btn2 = (Button)findViewById(R.id.c);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent ,cr );
            }
        });

    }
    protected void onActivityResult(int rq, int rc, Intent data){
        if(rq ==cr && rc== Activity.RESULT_OK){
            photo = (Bitmap)data.getExtras().get("data");
            ImageView1.setImageBitmap(photo);
        }
    }


    @Override
    public void onClick(View view) {
        Bitmap img = photo;
        Mat source = new Mat();
        Mat dest = new Mat();
        Mat hierarchy = new Mat();
        Utils.bitmapToMat(img, source);
        Imgproc.cvtColor(source, dest, Imgproc.COLOR_RGB2GRAY);
        Imgproc.threshold(dest, dest, 80, 255 ,Imgproc.THRESH_BINARY);

//************************************************************************************************

// now iterate over all top level contours
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.GaussianBlur(dest, dest, new Size(3, 3), 0);
        Imgproc.Canny(dest, dest, 70, 110);

        Imgproc.findContours(dest,contours,hierarchy,Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        //Imgproc.GaussianBlur(dest, dest, new Size(5,5), 0);
        //Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ERODE, new Size(5,5));
       // Imgproc.morphologyEx( dest,dest , Imgproc.MORPH_OPEN, kernel);
        //Core.divide(dest,dest ,dest);
       // Core.normalize(dest,dest, 0, 255, Core.NORM_MINMAX);



        for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++) {
            Imgproc.drawContours(source, contours, contourIdx, new Scalar(0, 0, 255), -1);
        }
//************************************************************************************************
        Bitmap btmp = Bitmap.createBitmap(dest.width(), dest.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(source,  btmp);
        ImageView2.setImageBitmap(btmp);
    }
}
