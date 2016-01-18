package com.zzu.me.sample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView mImgView;
    private Button mButton;
    private Bitmap mBitmap;
    final static String TAG = "MainActivity";

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch(status) {
                case LoaderCallbackInterface.SUCCESS:
                    Log.i(TAG, "OpenCV Manager Connected");
                    break;
                case LoaderCallbackInterface.INIT_FAILED:
                    Log.i(TAG,"Init Failed");
                    break;
                case LoaderCallbackInterface.INSTALL_CANCELED:
                    Log.i(TAG,"Install Cancelled");
                    break;
                case LoaderCallbackInterface.INCOMPATIBLE_MANAGER_VERSION:
                    Log.i(TAG,"Incompatible Version");
                    break;
                case LoaderCallbackInterface.MARKET_ERROR:
                    Log.i(TAG,"Market Error");
                    break;
                default:
                    Log.i(TAG,"OpenCV Manager Install");
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //加载图像资源，并设置
        mImgView = (ImageView)findViewById(R.id.imageView);
        mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.testimage1);
        mImgView.setImageBitmap(mBitmap);
        //添加Button Listener
        mButton = (Button)findViewById(R.id.button);
        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //将bitmap转换为Mat矩阵
        Mat rgbMat = new Mat();
        Utils.bitmapToMat(mBitmap, rgbMat);
        //将彩色图像转换为灰度图像
        Mat grayMat = new Mat();
        Imgproc.cvtColor(rgbMat,grayMat,Imgproc.COLOR_BGR2GRAY);
        //将Mat转换为bitmap并设置
        Bitmap grayBmp = Bitmap.createBitmap(grayMat.width(),grayMat.height(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(grayMat,grayBmp);
        mImgView.setImageBitmap(grayBmp);
    }
}
