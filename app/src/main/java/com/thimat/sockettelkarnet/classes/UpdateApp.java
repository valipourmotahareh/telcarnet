package com.thimat.sockettelkarnet.classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import androidx.core.content.FileProvider;

import com.thimat.sockettelkarnet.BuildConfig;
import com.thimat.sockettelkarnet.myUtils.CTypeFace;
import com.thimat.sockettelkarnet.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateApp extends AsyncTask<String, String, String> {
	private ProgressDialog pDialog;

	public void ShowProgress() {
		pDialog = new ProgressDialog(context);
		pDialog.setMessage(context.getText(R.string.dialog_download_file));
		pDialog.setIndeterminate(false);
		pDialog.setMax(100);
		pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pDialog.setCancelable(false);
		pDialog.show();
	}
	@Override
	protected void onProgressUpdate(String... values) {
		// TODO Auto-generated method stub
		pDialog.setProgress(Integer.parseInt(values[0]));
	}
	private static final String TAG = "-- UpdateApp --";
	private Context context;
	public void setContext(Context contextf) {
		context = contextf;
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		ShowProgress();
	}
	@Override
	protected String doInBackground(String... arg0) {
		try {
			URL url = new URL(arg0[0]);
			CTypeFace.logOBJ(TAG, "URL is : " + url.getPath());

			HttpURLConnection c = (HttpURLConnection) url.openConnection();
			c.setRequestMethod("GET");
			// c.setDoOutput(true);
			c.connect();

			int lenghtOfFile = c.getContentLength();

			// String PATH = "/mnt/sdcard/Download/";
			String PATH = context.getCacheDir().getPath() + "/Telkarnet/";
			File file = new File(PATH);
			file.mkdirs();
			File outputFile = new File(file, "/Telcarnetupdate.apk");
			if (outputFile.exists()) {
				outputFile.delete();
			}
				FileOutputStream fos = new FileOutputStream(outputFile);

				InputStream is = c.getInputStream();

				byte[] buffer = new byte[1024];
				int len1 = 0;

				long total = 0;
				while ((len1 = is.read(buffer)) != -1) {

					total += len1;

					fos.write(buffer, 0, len1);
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));
				}
				fos.close();
				is.close();

//				String PATHALL = Environment.getExternalStorageState()
//						+ "/Download/update.apk";
//				Intent install = new Intent(Intent.ACTION_VIEW);
////				intent.setDataAndType(Uri.fromFile(new File(PATHALL)),
////						"application/vnd.android.package-archive");
//				install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag
//				install.setDataAndType(Uri.fromFile(new File(
//						Environment.getExternalStorageDirectory() + "/Download/" +
//								"update.apk")), "application/vnd.android.package-archive");
//                // End New Approach
//				context.startActivity(install);
				File toInstall = new File(context.getCacheDir().getPath() + "/Telkarnet/"+"Telcarnetupdate.apk");
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
					Uri apkUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", toInstall);
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
					intent.setData(apkUri);
					intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
					context.startActivity(intent);
				} else {
					Uri apkUri = Uri.fromFile(toInstall);
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				}


		} catch (Exception e) {
			e.printStackTrace();
			CTypeFace.logOBJ("UpdateAPP", "Update error! " + e.getMessage());
		}
		return null;
	}
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		pDialog.dismiss();
	}
}