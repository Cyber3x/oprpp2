package hr.fer.zemris.java.custom.scripting.exec;

import java.util.function.ToDoubleBiFunction;
import java.util.function.ToIntBiFunction;

public class ValueWrapper {
    private Object value;

    public ValueWrapper(Object value) {
        this.value = value;
    }


    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    // ARITHMETICS

    public void add(Object incValue) {
        value = doOperation(incValue, Double::sum, Integer::sum);
    }


    public void subtract(Object decValue) {
        value = doOperation(decValue,
                (aDouble, bDouble) -> aDouble - bDouble,
                (aInteger, bInteger) -> aInteger - bInteger);
        System.out.println("new values: " + value);
    }

    public void multiply(Object mulValue) {
        value = doOperation(mulValue,
                (aDouble, bDouble) -> aDouble * bDouble,
                (aInteger, bInteger) -> aInteger * bInteger);
    }

    public void divide(Object divValue) {
        value = doOperation(divValue,
                (aDouble, bDouble) -> aDouble / bDouble,
                (aInt, bInt) -> aInt / bInt);
    }

    public int numCompare(Object withValue) {
        if (value == null && withValue == null) return 0;

        Object result = doOperation(
                withValue,
                (aDouble, bDouble) -> aDouble - bDouble,
                (aInt, bInt) -> aInt - bInt
        );


        if (result instanceof Double d) {
            if (d == 0) return 0;
            return d < 0 ? -1 : 1;
        } else {
            int number = (Integer) result;
            if (number == 0) return 0;
            return number < 0 ? -1 : 1;
        }
    }

    private Object doOperation(Object otherObj, ToDoubleBiFunction<Double, Double> doubleBiFunction, ToIntBiFunction<Integer, Integer> integerBiFunction) {

        Object thisValue = convertObject(value);
        Object otherValue = convertObject(otherObj);

        boolean isThisDouble = thisValue instanceof Double;
        boolean isOtherDouble = otherValue instanceof Double;

        if (isThisDouble && isOtherDouble) {
            return doubleBiFunction.applyAsDouble((Double) thisValue, (Double) otherValue);
        } else if (isThisDouble) {
            return doubleBiFunction.applyAsDouble((Double) thisValue, ((Integer) otherValue).doubleValue());
        } else if (isOtherDouble) {
            return doubleBiFunction.applyAsDouble(((Integer) thisValue).doubleValue(), (Double) otherValue);
        } else {
            return integerBiFunction.applyAsInt((Integer) thisValue, (Integer) otherValue);
        }
    }

    /**
     * @param inputObject
     * @return Double or Integer
     */
    private Object convertObject(Object inputObject) {
        if (inputObject == null) return 0;
        if (inputObject instanceof String valueStr) {
            if (valueStr.contains(".") || valueStr.toLowerCase().contains("e")) {
                return Double.parseDouble(valueStr);
            } else {
                return Integer.valueOf(valueStr);
            }
        }
        if ((inputObject instanceof Integer) || (inputObject instanceof Double)) {
            return inputObject;
        }
        throw new RuntimeException("the current type of value is: " + value.getClass() + " which is not supported");
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
