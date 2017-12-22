package com.raokui.testsql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by 饶魁 on 2017/12/22.
 */

public class BaseDao<T> implements IBaseDao<T> {

    private SQLiteDatabase database;


    private Class<T> entityClass;

    private boolean isInit = false;

    private String tableName;

    private HashMap<String, Field> cacheMap;

    public synchronized boolean init(Class<T> entity, SQLiteDatabase sqLiteDatabase) {

        if (!isInit) {
            entityClass = entity;

            database = sqLiteDatabase;

            tableName = entity.getAnnotation(DbTable.class).value();

            if (!sqLiteDatabase.isOpen()) {
                return false;
            }


            if (!autoCreateTable()) {
                return false;
            }
            isInit = true;
        }

        initCacheMap();

        return isInit;
    }

    private void initCacheMap() {
        cacheMap = new HashMap<>();
        // 将成员变量与实际表中的字段产生映射

        // 查一次空表得到表字段
        String sql = "select * from " + tableName + " limit 1,0";
        Cursor cursor = database.rawQuery(sql, null);
        // 得到字段名数组
        String[] columnNames = cursor.getColumnNames();
        Field[] fields = entityClass.getDeclaredFields();
        for (String columnName : columnNames) {
            Field resultField = null;
            for (Field field : fields) {
                if (columnName.equals(field.getAnnotation(DbField.class).value())) {
                    resultField = field;
                    break;
                }
            }

            if (resultField != null) {
                cacheMap.put(columnName, resultField);
            }
        }

        cursor.close();

    }

    private boolean autoCreateTable() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("CREATE TABLE IF NOT EXISTS ");
        stringBuffer.append(tableName + "(");
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            Class type = field.getType();
            if (type == String.class) {
                stringBuffer.append(field.getAnnotation(DbField.class).value() + " TEXT,");
            } else if (type == Integer.class) {
                stringBuffer.append(field.getAnnotation(DbField.class).value() + " INTEGER,");
            } else if (type == Double.class) {
                stringBuffer.append(field.getAnnotation(DbField.class).value() + " DOUBLE,");
            } else if (type == Long.class) {
                stringBuffer.append(field.getAnnotation(DbField.class).value() + " BIGINT,");
            } else if (type == byte[].class) {
                stringBuffer.append(field.getAnnotation(DbField.class).value() + " BLOB,");
            } else {
                // 其他类型
                continue;
            }
        }
        if (',' == stringBuffer.charAt(stringBuffer.length() - 1)) {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        stringBuffer.append(")");
        try {
            this.database.execSQL(stringBuffer.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public Long insert(T entity) {
        ContentValues contentValues = getValues(entity);
        database.insert(tableName, null, contentValues);
        return null;
    }

    private ContentValues getValues(T entity) {
        ContentValues contentValues = new ContentValues();
        Iterator<Map.Entry<String, Field>> iterator = cacheMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Field> fieldEntry = iterator.next();
            Field field = fieldEntry.getValue();
            String key = fieldEntry.getKey();
            field.setAccessible(true);

            try {
                Object object = field.get(entity);
                Class type = field.getType();
                if (type == String.class) {
                    String value = (String) object;
                    contentValues.put(key, value);
                } else if (type == Double.class) {
                    Double value = (Double) object;
                    contentValues.put(key, value);
                } else if (type == Integer.class) {
                    Integer value = (Integer) object;
                    contentValues.put(key, value);
                } else if (type == byte[].class) {
                    byte[] value = (byte[]) object;
                    contentValues.put(key, value);
                } else if (type == Long.class) {
                    Long value = (Long) object;
                    contentValues.put(key, value);
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        return contentValues;
    }

}
