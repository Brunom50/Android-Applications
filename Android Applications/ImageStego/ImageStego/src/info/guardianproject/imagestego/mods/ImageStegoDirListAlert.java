package info.guardianproject.imagestego.mods;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import info.guardianproject.imagestego.Constants;
import info.guardianproject.imagestego.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ImageStegoDirListAlert extends AlertDialog implements Constants {
	Activity a;
	ImageStegoDirListAlert isda;
	DirListListener dll;
	
	LayoutInflater li;
	
	View inner;
	ListView dirList;
	
	String lastF;
	
	
	public interface DirListListener {
		public void onAscSelected(String asc);
	}
	
	public ImageStegoDirListAlert(Activity a) {
		super(a);
		this.a = a;
		dll = (DirListListener) a;
		
		li = LayoutInflater.from(a);
		inner = li.inflate(R.layout.imagestegodirlist, null);
		
		setView(inner);
		
		lastF = Environment.getExternalStorageDirectory().getAbsolutePath();
		
		dirList = (ListView) inner.findViewById(R.id.dirlist);
		dirList.setAdapter(new DirListAdapter(lastF));
		
		isda = this;
	}
	
	public ImageStegoDirListAlert(Context context) {
		super(context);
	}
	
	@Override
	public void setTitle(CharSequence t) {
		
	}
	
	public class DirListAdapter extends BaseAdapter {
		List<File> files;
		File dir;
		
		public DirListAdapter(String dir_) {
			dir = new File(dir_);
			files = new ArrayList<File>();
			for(File f : dir.listFiles()) {
				files.add(f);
				Log.d(LOG, f.getAbsolutePath());
			}
		}
		
		@Override
		public int getCount() {
			return files.size();
		}

		@Override
		public Object getItem(int position) {
			return files.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final File f = files.get(position);
			convertView = li.inflate(R.layout.dirlistitem, null);
			Drawable d = a.getResources().getDrawable(R.drawable.ic_folder_grey);
			
			if(f.isDirectory()) {
				if(!f.getParent().equals("/")) {
					d = a.getResources().getDrawable(R.drawable.ic_folder_green);
					convertView.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							lastF = f.getAbsolutePath();
							dirList.setAdapter(new DirListAdapter(f.getAbsolutePath()));
							
						}
					});
				}
			} else if(f.isFile()){
				d = a.getResources().getDrawable(R.drawable.ic_file_grey);
			
				if(f.getName().lastIndexOf(".") != -1) {
					String lastBit = f.getName().substring(f.getName().lastIndexOf("."));
					Log.d(LOG, lastBit);
					if(lastBit.equals(".asc")) {
						d = a.getResources().getDrawable(R.drawable.ic_file_blue);
						convertView.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								dll.onAscSelected(f.getAbsolutePath());
								isda.dismiss();
							}
						});
					}
				}
					
			}
			
			ImageView fThumb = (ImageView) convertView.findViewById(R.id.fThumb);
			fThumb.setImageDrawable(d);
			
			TextView fName = (TextView) convertView.findViewById(R.id.fName);
			fName.setText(f.getName());
			return convertView;
		}
		
	}

}
