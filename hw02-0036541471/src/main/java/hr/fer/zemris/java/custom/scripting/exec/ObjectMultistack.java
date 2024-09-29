package hr.fer.zemris.java.custom.scripting.exec;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class ObjectMultistack {

    Map<String, MultistackEntry> wrappersMap = new HashMap<>();

    public void push(String keyName, ValueWrapper valueWrapper) {
        boolean stackExists = wrappersMap.containsKey(keyName);

        MultistackEntry entry;
        if (stackExists) {
            entry = new MultistackEntry(valueWrapper, wrappersMap.get(keyName));
        } else {
            entry = new MultistackEntry(valueWrapper);
        }
        wrappersMap.put(keyName, entry);
    }


    public ValueWrapper pop(String keyName) throws NoSuchElementException {
        boolean stackExists = wrappersMap.containsKey(keyName);
        if (!stackExists) throw new NoSuchElementException("this stack is empty");

        MultistackEntry stackTop = wrappersMap.get(keyName);
        boolean isLastElement = stackTop.getNext() == null;

        if (isLastElement) {
            wrappersMap.remove(keyName);
        } else {
            wrappersMap.put(keyName, stackTop.getNext());
        }
        return stackTop.getValue();
    }

    /**
     * Returns the ValueWrapper that is on top of the stack. If there's at least one ValueWrapper
     * then the entry will be in the hashmap.
     *
     * @param keyName
     * @return ValueWrapper at the top of the stack
     */
    public ValueWrapper peek(String keyName) {
        if (!wrappersMap.containsKey(keyName)) return null;
        return wrappersMap.get(keyName).getValue();
    }

    public boolean isEmpty(String keyName) {
        return wrappersMap.values().isEmpty();
    }

    private static class MultistackEntry {
        private ValueWrapper value;
        private MultistackEntry next = null;

        public MultistackEntry(ValueWrapper value, MultistackEntry next) {
            this.value = value;
            this.next = next;
        }

        public MultistackEntry(ValueWrapper value) {
            this(value, null);
        }

        public ValueWrapper getValue() {
            return value;
        }

        public MultistackEntry getNext() {
            return next;
        }

        public void setNext(MultistackEntry next) {
            this.next = next;
        }
    }
}
