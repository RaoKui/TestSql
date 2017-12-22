package com.raokui.testsql;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void insert(View view) {
        String sqLiteDatabasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tesssaGGG.db";
        Log.i(TAG, "onCreate: " + sqLiteDatabasePath);
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(sqLiteDatabasePath, null);
        BaseDao<Person> baseDao = new BaseDao();
        baseDao.init(Person.class, sqLiteDatabase);
        Person person = new Person();
        person.setName("haha");
//        person.setPassword(21121L);
        baseDao.insert(person);

    }
}
