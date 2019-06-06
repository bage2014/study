package com.bage.config;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 默认值对象
 */
public class DefaultConfig {

    ///////////////////////////////     默认值     ///////////////////////////////
    /**
     * 默认 int 值
     */
    private Integer intValue = 0;
    /**
     * 默认 double 值
     */
    private Double doubleValue = 0.0;
    /**
     * 默认 float 值
     */
    private Float floatValue = 0.0f;
    /**
     * 默认 long 值
     */
    private Long longValue = 0L;
    /**
     * 默认 short 值
     */
    private Short shortValue = 0;
    /**
     * 默认 boolean 值
     */
    private Boolean booleanValue = false;
    /**
     * 默认 byte 值
     */
    private Byte byteValue = 0;
    /**
     * 默认 char 值
     */
    private Character charValue = '0';

    private String stringValue = "";

    private Date dateValue = new Date();

    private List listValue = new ArrayList();

    private Map mapValue = new HashMap();

    private Set setValue = new HashSet();

    private File fileValue = new File("default-file.txt");

    private Object objectValue = null;

    ///////////////////////////////     默认值     ///////////////////////////////


    ///////////////////////////////     默认值选值列表     ///////////////////////////////
    /**
     * 默认 int 可选值
     */
    private Integer[] intValues = {intValue};
    /**
     * 默认 double 可选值
     */
    private Double[] doubleValues = {doubleValue};
    /**
     * 默认 float 可选值
     */
    private Float[] floatValues = {floatValue};
    /**
     * 默认 long 可选值
     */
    private Long[] longValues = {longValue};
    /**
     * 默认 short 可选值
     */
    private Short[] shortValues = {shortValue};
    /**
     * 默认 boolean 可选值
     */
    private Boolean[] booleanValues = {booleanValue};
    /**
     * 默认 byte 可选值
     */
    private Byte[] byteValues = {byteValue};
    /**
     * 默认 char 可选值
     */
    private Character[] charValues = {charValue};
    ///////////////////////////////     默认值选值列表     ///////////////////////////////

    ///////////////////////////////     常用对象选值列表     ///////////////////////////////
    private String[] stringValues = {stringValue};

    private Date[] dateValues = {dateValue};

    private List[] listValues = {listValue};

    private Map[] mapValues = {mapValue};

    private Set[] setValues = {setValue};

    private File[] fileValues = {fileValue};

    private Object[] objectValues = {objectValue};
    ///////////////////////////////     常用对象选值列表     ///////////////////////////////



    ///////////////////////////////     配置     ///////////////////////////////
    /**
     * 是否随机
     */
    private boolean isRandom = false;
    /**
     * 随机情况下的最大值
     */
    private int maxRandomNumber = 10000;

    private boolean isDebug = true;
    ///////////////////////////////     配置     ///////////////////////////////

    private Random random = new Random();

    ///////////////////////////////     类单例     ///////////////////////////////
    /**
     * 持有自身单例对象
     */
    private static volatile DefaultConfig instance = null;

    /**
     * 私有化构造函数
     */
    private DefaultConfig() {

    }

    /**
     * 单例获取静态方法
     *
     * @return
     */
    public static DefaultConfig newInstance() {
        if (instance == null) {
            synchronized (DefaultConfig.class) {
                if (instance == null) {
                    instance = new DefaultConfig();
                }
            }
        }
        return instance;
    }
    ///////////////////////////////     类单例     ///////////////////////////////


    ///////////////////////////////     setter、getter     ///////////////////////////////

    public Integer getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public Double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public Float getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(float floatValue) {
        this.floatValue = floatValue;
    }

    public Long getLongValue() {
        return longValue;
    }

    public void setLongValue(long longValue) {
        this.longValue = longValue;
    }

    public Short getShortValue() {
        return shortValue;
    }

    public void setShortValue(short shortValue) {
        this.shortValue = shortValue;
    }

    public Boolean isBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public Byte getByteValue() {
        return byteValue;
    }

    public void setByteValue(byte byteValue) {
        this.byteValue = byteValue;
    }

    public Character getCharValue() {
        return charValue;
    }

    public void setCharValue(char charValue) {
        this.charValue = charValue;
    }

    public String getStringValue() {
        return stringValue + random.nextInt(maxRandomNumber);
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    public List getListValue() {
        try {
            return listValue.getClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setListValue(List listValue) {
        this.listValue = listValue;
    }

    public Map getMapValue() {
        return mapValue;
    }

    public void setMapValue(Map mapValue) {
        this.mapValue = mapValue;
    }

    public Set getSetValue() {
        return setValue;
    }

    public void setSetValue(Set setValue) {
        this.setValue = setValue;
    }

    public File getFileValue() {
        return fileValue;
    }

    public void setFileValue(File fileValue) {
        this.fileValue = fileValue;
    }

    public Object getObjectValue() {
        return objectValue;
    }

    public void setObjectValue(Object objectValue) {
        this.objectValue = objectValue;
    }

    public Integer[] getIntValues() {
        return intValues;
    }

    public void setIntValues(Integer[] intValues) {
        this.intValues = intValues;
    }

    public Double[] getDoubleValues() {
        return doubleValues;
    }

    public void setDoubleValues(Double[] doubleValues) {
        this.doubleValues = doubleValues;
    }

    public Float[] getFloatValues() {
        return floatValues;
    }

    public void setFloatValues(Float[] floatValues) {
        this.floatValues = floatValues;
    }

    public Long[] getLongValues() {
        return longValues;
    }

    public void setLongValues(Long[] longValues) {
        this.longValues = longValues;
    }

    public Short[] getShortValues() {
        return shortValues;
    }

    public void setShortValues(Short[] shortValues) {
        this.shortValues = shortValues;
    }

    public Boolean[] getBooleanValues() {
        return booleanValues;
    }

    public void setBooleanValues(Boolean[] booleanValues) {
        this.booleanValues = booleanValues;
    }

    public Byte[] getByteValues() {
        return byteValues;
    }

    public void setByteValues(Byte[] byteValues) {
        this.byteValues = byteValues;
    }

    public Character[] getCharValues() {
        return charValues;
    }

    public void setCharValues(Character[] charValues) {
        this.charValues = charValues;
    }

    public String[] getStringValues() {
        return stringValues;
    }

    public void setStringValues(String[] stringValues) {
        this.stringValues = stringValues;
    }

    public Date[] getDateValues() {
        return dateValues;
    }

    public void setDateValues(Date[] dateValues) {
        this.dateValues = dateValues;
    }

    public List[] getListValues() {
        return listValues;
    }

    public void setListValues(List[] listValues) {
        this.listValues = listValues;
    }

    public Map[] getMapValues() {
        return mapValues;
    }

    public void setMapValues(Map[] mapValues) {
        this.mapValues = mapValues;
    }

    public Set[] getSetValues() {
        return setValues;
    }

    public void setSetValues(Set[] setValues) {
        this.setValues = setValues;
    }

    public File[] getFileValues() {
        return fileValues;
    }

    public void setFileValues(File[] fileValues) {
        this.fileValues = fileValues;
    }

    public Object[] getObjectValues() {
        return objectValues;
    }

    public void setObjectValues(Object[] objectValues) {
        this.objectValues = objectValues;
    }

    public boolean isRandom() {
        return isRandom;
    }

    public void setRandom(boolean random) {
        isRandom = random;
    }

    public int getMaxRandomNumber() {
        return maxRandomNumber;
    }

    public void setMaxRandomNumber(int maxRandomNumber) {
        this.maxRandomNumber = maxRandomNumber;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }

    public Object getEnumValue(Class cls) {
        Field[] fields = cls.getDeclaredFields();
        if(fields.length > 0){
            int index = new Random().nextInt(fields.length);
            return Enum.valueOf(cls,fields[index].getName());
        }
        return null;
    }
///////////////////////////////     setter、getter     ///////////////////////////////

    ///////////////////////////////     others     ///////////////////////////////
    // TODO
    ///////////////////////////////     others     ///////////////////////////////

}
