package com.bage.generator;

import java.io.File;
import java.util.*;

/**
 * 值生成
 */
public class RandomValueGeneratorImpl extends ValueGenerator {

    private Random random = new Random();

    @Override
    protected Object generateEnumValue(Class cls) {
        return null;
    }

    public Character generateCharValue() {
        Character[] chars = getDefaultData().getCharValues();
        return chars[random.nextInt(chars.length)];
    }

    public Byte generateByteValue() {
        return Integer.valueOf(random.nextInt() * getDefaultData().getRandomMaxNumber()).byteValue();
    }

    public Boolean generateBooleanValue() {
        Boolean[] booleanValues = getDefaultData().getBooleanValues();
        return booleanValues[random.nextInt(booleanValues.length)];
    }

    public Short generateShortValue() {
        return Integer.valueOf(random.nextInt() * getDefaultData().getRandomMaxNumber()).shortValue();
    }

    public Long generateLongValue() {
        return random.nextLong() * getDefaultData().getRandomMaxNumber();
    }

    public Float generateFloatValue() {
        return random.nextFloat() * getDefaultData().getRandomMaxNumber();
    }

    public Double generateDoubleValue() {
        return random.nextDouble() * getDefaultData().getRandomMaxNumber();
    }

    public Integer generateIntValue() {
        return random.nextInt(getDefaultData().getRandomMaxNumber());
    }

    public String generateStringValue() {
        return null;
    }

    public Date generateDateValue() {
        return null;
    }

    public Object generateObjectValue() {
        return null;
    }

    public List generateListValue() {
        return null;
    }

    public Set generateSetValue() {
        return null;
    }

    public File generateFileValue() {
        return null;
    }

    public Map generateMapValue() {
        return null;
    }

}
