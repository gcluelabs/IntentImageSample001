package com.example.intentimage;

import java.io.File;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class IntentImageSample extends Activity implements View.OnClickListener {
	
	/**
	 * Button1
	 */
	Button mButton1;
	
	/**
	 * Button2
	 */
	Button mButton2;

	/**
	 * ImageView1
	 */
	ImageView mImageView1;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	
	
		mButton1 = (Button) findViewById(R.id.button1);
		mButton1.setOnClickListener(this);
		
		mButton2 = (Button) findViewById(R.id.button2);
		mButton2.setOnClickListener(this);
		
		mImageView1 = (ImageView) findViewById(R.id.imageView1);	
	}

	@Override
	public void onClick(View view) {
		if(view.equals(mButton1)){
			
			// 写真撮影を起動するIntentを作成
			Intent takeIntent = new Intent();
			takeIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
			// MediaStore.EXTRA_OUTPUTにファイルの保存場所を渡す
			File mFile = new File(Environment.getExternalStorageDirectory(), "test.jpg");
			takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));
			
			// requestCode 1でIntentを起動
			startActivityForResult(takeIntent, 1);
		}
		else if(view.equals(mButton2)){
			Intent selectIntent = new Intent(Intent.ACTION_GET_CONTENT);
			selectIntent.setType("image/*");
			startActivityForResult(selectIntent, 2);
		}
	}
	
	/**
	 * startActivityForResult()でActivityが起動し、処理が終わった際に呼ばれる
	 * OutOfMemoryが出る可能性あり
	 */
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (resultCode == RESULT_OK && requestCode == 1) {
//			// MediaStore.EXTRA_OUTPUTで指定した場所と同じ場所から画像を読み込み
//			File mFile = new File(Environment.getExternalStorageDirectory(), "test.jpg");
//			Uri mUri = Uri.fromFile(mFile);
//			
//			// ImageViewに渡す
//			mImageView1.setImageURI(mUri);
//		}
//		else if (resultCode == RESULT_OK && requestCode == 2) {
//			Uri mUri = data.getData();;
//			mImageView1.setImageURI(mUri);
//		}
//	}
	/**
	 * startActivityForResult()でActivityが起動し、処理が終わった際に呼ばれる
	 * (OutOfMemoryの修正をしたつもり)
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == 1) {
			// MediaStore.EXTRA_OUTPUTで指定した場所と同じ場所から画像を読み込み
			File mFile = new File(Environment.getExternalStorageDirectory(), "test.jpg");
			Uri mUri = Uri.fromFile(mFile);
			// 画像を取得
			ContentResolver conReslv = getContentResolver();
			if (mUri != null) {
				try {
					// ビットマップ画像を取得
					Bitmap bitmap = MediaStore.Images.Media.getBitmap(conReslv, mUri);
					// ImageViewに渡す
					mImageView1.setImageBitmap(bitmap);
				} catch (OutOfMemoryError e) {
					Toast.makeText(this, "OutOfMemory発生しました", Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		else if (resultCode == RESULT_OK && requestCode == 2) {
			// 画像URIを取得
			Uri photoUri = data.getData();
			// 画像を取得
			ContentResolver conReslv = getContentResolver();
			if (photoUri != null) {
				try {
					// ビットマップ画像を取得
					Bitmap bitmap = MediaStore.Images.Media.getBitmap(conReslv, photoUri);
					// ImageViewに渡す
					mImageView1.setImageBitmap(bitmap);
				} catch (OutOfMemoryError e) {
					Toast.makeText(this, "OutOfMemory発生しました", Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}