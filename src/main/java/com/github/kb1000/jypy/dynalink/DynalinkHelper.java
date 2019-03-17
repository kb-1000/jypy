package com.github.kb1000.jypy.dynalink;

import jdk.dynalink.NamedOperation;
import jdk.dynalink.NamespaceOperation;
import jdk.dynalink.Operation;
import jdk.dynalink.StandardOperation;

public class DynalinkHelper {
    private DynalinkHelper() {
    }

    public static StandardOperation getStandardOperation(Operation operation) {
        while (true) {
            if (operation instanceof NamespaceOperation) {
                operation = ((NamespaceOperation) operation).getBaseOperation();
            } else if (operation instanceof NamedOperation) {
                operation = ((NamedOperation) operation).getBaseOperation();
            } else {
                return (StandardOperation) operation; // throws ClassCastException if unsupported type
            }
        }
    }
}
