package com.coolweather.app.model;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.db.CoolWeatherOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoolWeatherDB {
	
	/*
	 * 数据库名
	 */
	public static final String DB_NAME = "cool_weather";//final表示这是个常量，后面不再变化。
	
	/*
	 * 数据库版本
	 */
	public static final int VERSION = 1;//public是访问权限修饰符，用于控制外界对类内部成员的访问，声明为public的对象成员是完全共有的，外界可以随意访问，除此之外还有private,protected和默认。static控制类成员变化的修饰符。static是静态成员修饰符，其修饰的静态变量脱离具体对象独立存在，在内存中之后一份拷贝，所有的对象都公用这一个存储空间，所以对static修饰的静态变量进行的修改对该类的所有对象都起作用。static修饰的静态函数代表所有对象的统一操作，只能调用静态变量。static是针对面向对象中的“多态”而提出来的，static修饰的静态成员不存在多态性。
	
	private static CoolWeatherDB coolWeatherDB;
	
	private SQLiteDatabase db;//SQLitDatabase代表类型，db变量名。只能在当前类中使用。
	
	/*
	 * 将构造方法私有化
	 */
	private CoolWeatherDB(Context context) {
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context,DB_NAME,null,VERSION);
		db = dbHelper.getWritableDatabase();//构建出SQLiteOpenHelper的实例之后，再调用它的getReadableDatabase()或getWritableDatabase()方法就能够创建数据库了，数据库文件会存放在/data/data/<package name>/databases/目录下。		
	}
	
	/*
	 * 获取CoolWeatherDB的实例
	 */
	public synchronized static CoolWeatherDB getInstance(Context context){
		if (coolWeatherDB == null) {
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	
	/*
	 * 将Province实例存储到数据库。
	 */
	public void saveProvince(Province province){
		if (province != null){
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code",province.getProvinceCode());
			db.insert("Province",null,values);
		}
	}
	
	/*
	 * 从数据库读取全国所有的省份信息。
	 */
	public List<Province> loadProvince(){
		List<Province> list =new ArrayList<Province>();
		Cursor cursor = db.query("Province",null,null,null,null,null,null);
		if (cursor.moveToFirst()) {
			do {
				Province province = new Province();
				province.setID(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(province);
			} while (cursor.moveToNext());
		}
		return list;
	}
	/*
	 * 将City实例存储到数据库
	 */
	public void saveCity(City city) {
		if (city!=null) {
			ContentValues values = new ContentValues();//是一种存储的机制,数据库中插入数据的时候，首先应该有一个ContentValues的对象
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);
		}
	}
	/*
	 * 从数据库读取某省下所有的城市信息
	 */
	public List<City> loadCities(int provinceId) {
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id=?", new String[] {String.valueOf(provinceId)}, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				City city = new City();
				city.setID(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(cursor.getString(cursor.getColumnIndex("provinceId")));
				list.add(city);
			} while(cursor.moveToNext());
		}
		return list;
	}
	
	/*
	 * 将County实例存储到数据库
	 */
	public void saveCounty(County county){
		if (county!=null){
			ContentValues values = new ContentValues();
			values.put("county_name",county.getCountyName());
			values.put("county_code", county.getCountyCode());
			values.put("city_id", county.getCityId());
			db.insert("County", null, values);
		}
	}
	
	/*
	 * 从数据库读取某城市下所有的县信息
	 */
	public List<County> loadCounties(int cityId) {
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.query("County", null, "city_id=?", new String[] {String.valueOf(cityId)}, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCityId(cursor.getString(cursor.getColumnIndex("cityId")));
				} while(cursor.moveToNext());
			}
		return list;
	}	
	
}
