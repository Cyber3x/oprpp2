package hr.fer.zemris.java.custom.scripting.demo;

import hr.fer.zemris.java.custom.scripting.exec.ValueWrapper;

public class ValueWrapperDemo {
    public static void main(String[] args) {
        ValueWrapper v1 = new ValueWrapper(null);
        ValueWrapper v2 = new ValueWrapper(null);
        v1.add(v2.getValue()); // v1 now stores Integer(0); v2 still stores null.
        System.out.println(v1.getValue());
        ValueWrapper v3 = new ValueWrapper("1.2E1");
        ValueWrapper v4 = new ValueWrapper(Integer.valueOf(1));
        v3.add(v4.getValue()); // v3 now stores Double(13); v4 still stores Integer(1).
        System.out.println(v3.getValue());
        ValueWrapper v5 = new ValueWrapper("12");
        ValueWrapper v6 = new ValueWrapper(Integer.valueOf(1));
        v5.add(v6.getValue()); // v5 now stores Integer(13); v6 still stores Integer(1).
        System.out.println(v5);
        System.out.println("-".repeat(10));


        ValueWrapper veci = new ValueWrapper(24);
        ValueWrapper manji = new ValueWrapper("23.9");
        veci.subtract(manji.getValue());
        System.out.println(veci);
        System.out.println(veci.numCompare(manji.getValue()));

    }
}
