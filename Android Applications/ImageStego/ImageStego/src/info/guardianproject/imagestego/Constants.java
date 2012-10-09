package info.guardianproject.imagestego;

import android.os.Environment;

public interface Constants {
	public final static String LOG = "***************** ImageStego **************";
	public final static String DUMP = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ImageStego";
	
	public static class Source {
		public static final int CAMERA = 1000;
		public static final int GALLERY = 1001;
	}
}
