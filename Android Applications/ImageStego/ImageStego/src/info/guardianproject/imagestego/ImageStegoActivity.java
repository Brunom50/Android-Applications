package info.guardianproject.imagestego;

import java.io.File;

import info.guardianproject.imagestego.ImageStegoMediaScanner.MediaScannerListener;
import info.guardianproject.imagestego.mods.ImageStegoChoiceAlert;
import info.guardianproject.imagestego.mods.ImageStegoChoiceAlert.ChoiceAlertListener;
import info.guardianproject.imagestego.mods.ImageStegoDirListAlert;
import info.guardianproject.imagestego.mods.ImageStegoDirListAlert.DirListListener;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;

public class ImageStegoActivity extends Activity implements Constants, MediaScannerListener, OnClickListener, OnCheckedChangeListener, ChoiceAlertListener, DirListListener {
	String pathToAsc = null;
	Uri coverImageUri;
	String pathToCoverImage = null;
	String stegoMessage = null;
	
	boolean canSave = false;
	
    ImageView coverImage;
    EditText stegoMessageHolder;
    CheckBox encryptionSelect;
    Button save, clear, encryptionChange;
    
    File dump, coverImageFile;
    Handler h;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        dump = new File(DUMP);
        if(!dump.exists())
        	dump.mkdir();
        
        h = new Handler();
        initLayout();
    }
    
    public void initLayout() {
    	
    	setContentView(R.layout.imagestegoactivity);
    	
    	coverImage = (ImageView) findViewById(R.id.cover_image);
    	coverImage.setOnClickListener(this);
    	
    	encryptionSelect = (CheckBox) findViewById(R.id.encryption_select);
    	encryptionSelect.setOnCheckedChangeListener(this);
    	
    	encryptionChange = (Button) findViewById(R.id.encryption_change);
    	encryptionChange.setOnClickListener(this);
    	
    	stegoMessageHolder = (EditText) findViewById(R.id.stego_message_holder);
    	
    	save = (Button) findViewById(R.id.submit_save);
    	save.setOnClickListener(this);
    	
    	clear = (Button) findViewById(R.id.submit_clear);
    	clear.setOnClickListener(this);
    	
    	refreshLayout();
    	
    }
    
    private void clear() {
    	stegoMessage = null;
    	pathToCoverImage = null;
    	pathToAsc = null;
    	canSave = false;
    	coverImageFile = null;
    	
    	stegoMessageHolder.setText("");
    	coverImage.setImageResource(R.drawable.empty_image);
    	encryptionSelect.setChecked(false);
    	
    	refreshLayout();
    }
    
    private void refreshLayout() {
    	if(stegoMessageHolder.getText().toString().length() > 0)
    		stegoMessage = stegoMessageHolder.getText().toString();
    	
    	if(
    		pathToCoverImage != null &&
    		stegoMessage != null
    	)
    		canSave = true;
    	
    	if(pathToAsc == null) {
    		encryptionSelect.setText(getString(R.string.encryption_activate));
    		encryptionChange.setVisibility(View.GONE);
    	} else {
    		encryptionSelect.setText(pathToAsc);
    		encryptionChange.setVisibility(View.VISIBLE);
    	}
    	
    	save.setClickable(canSave);
    		
    }
    
    private void setImageData() {
    	h.post(new Runnable() {
			@Override
			public void run() {
				Bitmap b = BitmapFactory.decodeFile(pathToCoverImage);
				Log.d(LOG, "height: " + b.getHeight());
				int scale = Math.min(8, b.getWidth()/10);
				if(b.getHeight() > b.getWidth())
					scale = Math.min(8, b.getHeight()/10);
				
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inSampleSize = scale;
					
				coverImage.setImageBitmap(BitmapFactory.decodeFile(pathToCoverImage, opts));
				refreshLayout();
			}
		});
    }
    
    private String pullPathFromUri(Uri uri) {
    	if(uri.getScheme() != null && uri.getScheme().equals("file"))
    		return uri.toString();
    	else {
    		String path = null;
    		String[] cols = {MediaStore.Images.Media.DATA};
    		Cursor c = getContentResolver().query(uri, cols, null, null, null);
    		
    		if(c != null && c.moveToFirst()) {
    			path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
    			c.close();
    		}
    		
    		return path;
    	}
    		
    }
    
    private void parseKey(String asc) {
    	boolean isValid = true;
    	
    	if(isValid) {
    		pathToAsc = asc;
    		h.post(new Runnable() {
    			@Override
    			public void run() {
    				refreshLayout();
    			}
    		});
    	}
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if(resultCode == Activity.RESULT_OK) {
    		if(requestCode == Source.GALLERY || requestCode == Source.CAMERA) {
    			if(requestCode == Source.GALLERY) {
    				coverImageUri = data.getData();
    				pathToCoverImage = pullPathFromUri(coverImageUri);
    				coverImageFile = new File(pathToCoverImage);
    				setImageData();
    			} else if(requestCode == Source.CAMERA) {
    				new ImageStegoMediaScanner(ImageStegoActivity.this, pathToCoverImage);
    			}
    			
    			Log.d(LOG, pathToCoverImage);
    		}
    		
    	}
    }
    
    
    @Override
    public void onClick(View v) {
    	if(v == coverImage) {
    		ImageStegoChoiceAlert isca = new ImageStegoChoiceAlert(this);
    		isca.setTitle(getString(R.string.stego_cover_image_choice_title));
    		isca.setMessage(getString(R.string.stego_cover_image_choice_summary));
    		isca.addChoice(getString(R.string.stego_cover_image_choice_camera), Source.CAMERA);
    		isca.addChoice(getString(R.string.stego_cover_image_choice_gallery), Source.GALLERY);
    		isca.show();
    	} else if(v == encryptionChange) {
    		ImageStegoDirListAlert isda = new ImageStegoDirListAlert(ImageStegoActivity.this);
			isda.show();
    	} else if(v == clear) {
    		clear();
    	} else if(v == save) {
    		
    	}
    }

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked) {
			ImageStegoDirListAlert isda = new ImageStegoDirListAlert(ImageStegoActivity.this);
			isda.show();
		} else {
			pathToAsc = null;
			refreshLayout();
		}
		
	}

	@Override
	public void onChoice(int choice) {
		switch(choice) {
		case Source.GALLERY:
			Intent galleryIntent = new Intent(Intent.ACTION_PICK).setType("image/*");
			startActivityForResult(galleryIntent, Source.GALLERY);
			break;
		case Source.CAMERA:
			coverImageFile = new File(DUMP, "temp_img.jpg");
			coverImageUri = Uri.fromFile(coverImageFile);
			pathToCoverImage = coverImageFile.getAbsolutePath();
			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, coverImageUri);
			startActivityForResult(cameraIntent, Source.CAMERA);
			break;
		}
		
	}

	@Override
	public void onMediaScanned(String path, Uri uri) {
		setImageData();
		
	}

	@Override
	public void onAscSelected(String asc) {
		parseKey(asc);
	}
}