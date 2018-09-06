package org.mybatis.example;

import java.sql.Date;

/**
 * 
CREATE TABLE weather (
    city            VARCHAR(80),
    temp_lo         INT,           -- low temperature
    temp_hi         INT,           -- high temperature
    prcp            REAL,          -- precipitation
    DATE            DATE
);

INSERT INTO weather VALUES ('San Francisco', 46, 50, 0.25, '1994-11-27');
INSERT INTO weather VALUES ('nanjing', 129, 45, 0.25, '1999-11-27');
 * @author bage
 *
 */
public class Weather {

	private String city;
	private int temp_lo;
	private int temp_hi;
	private Date date;
	
	public Weather() {
		super();
	}
	
	public Weather(String city, int temp_lo, int temp_hi, Date date) {
		super();
		this.city = city;
		this.temp_lo = temp_lo;
		this.temp_hi = temp_hi;
		this.date = date;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getTemp_lo() {
		return temp_lo;
	}
	public void setTemp_lo(int temp_lo) {
		this.temp_lo = temp_lo;
	}
	public int getTemp_hi() {
		return temp_hi;
	}
	public void setTemp_hi(int temp_hi) {
		this.temp_hi = temp_hi;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	@Override
	public String toString() {
		return "Weather [city=" + city + ", temp_lo=" + temp_lo + ", temp_hi=" + temp_hi + ", date=" + date + "]";
	}
	
	
}
