package com.github.kb1000.jypy.dynalink;

import jdk.dynalink.*;

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

    public static Namespace[] getOperationNamespaces(Operation operation) {
        if (operation instanceof NamedOperation) {
            operation = ((NamedOperation) operation).getBaseOperation();
        }
        return ((NamespaceOperation) operation).getNamespaces();
    }
}
