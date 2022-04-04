package com.example.TRASPASOS_APP;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;


public class ActualizarClass extends AppCompatActivity {

    Uri uri;
    Button btnActualizar;
    String urlG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar);

        urlG = GlobalClass.gUrl + GlobalClass.gLink + getIntent().getStringExtra("url");

        btnActualizar = (Button) findViewById(R.id.btnActualizar);
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";

                String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";

                String fileExtension = MimeTypeMap.getFileExtensionFromUrl(urlG);
                String fileName = URLUtil.guessFileName(urlG, null, fileExtension);

                //String fileName = "bitacoraBaterias.apk";

                destination += fileName;
                uri = Uri.parse("file://" + destination);

                File file = new File(destination);

                if (file.exists()) {
                    //file.delete() - test this, I think sometimes it doesnt work
                    file.delete();

                }
                //get url of app on server
                String url = urlG;

                //set downloadmanager
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                // request.setDescription(getString(R.string.notification_description));
                request.setTitle(getString(R.string.app_name));

                //set destination
                request.setDestinationUri(uri);

                // get download service and enqueue file
                final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                final long downloadId = manager.enqueue(request);

                //set BroadcastReceiver to install app when .apk is downloaded
                BroadcastReceiver onComplete = new BroadcastReceiver() {
                    public void onReceive(Context ctxt, Intent intent) {


                        uri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", file);
                        Intent install = new Intent(Intent.ACTION_VIEW);
                        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        install.setDataAndType(uri, "application/vnd.android.package-archive");
                        startActivity(install);

                        //unregisterReceiver(this);
                        finish();

                    /*    Uri uri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + fileName));
                        Intent install = new Intent(Intent.ACTION_VIEW);
                        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        //setFlags change addFlags
                        install.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        install.setDataAndType(uri, "application/vnd.android.package-archive");
                        startActivity(install);
                        unregisterReceiver(this);
                        finish();*/

                    }
                };
                //register receiver for when .apk download is compete
                registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            }
        });

    }


}
