package com.vanima.mvips.activities;

import android.os.Bundle;
import android.app.Activity;
import android.widget.ListView;
import android.widget.TextView;

import com.vanima.mvips.R;

import java.util.Date;

public class LogActivity extends Activity {

    ListView listSyncLog;
    TextView txtLastSync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        txtLastSync = (TextView) findViewById(R.id.txtDatumZadnjeSinkronizacije_main);

        listSyncLog = (ListView) findViewById(R.id.listSyncLog_main);
        listSyncLog.setAdapter(MainActivity.getListaZadnjiLog());
        Date lastSync = MainActivity.getZadnjaSinkronizacijaVrijeme();
        txtLastSync.setText(MainActivity.parseDateFromSQLLiteDBFormatToMyFormat_DateTime(lastSync));


    }

}
