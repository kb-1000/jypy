package com.github.kb1000.jypy.dynalink;

import java.lang.invoke.MethodHandles;
import java.util.function.Supplier;

import jdk.dynalink.Operation;
import jdk.dynalink.StandardOperation;
import jdk.dynalink.linker.*;

import com.github.kb1000.jypy.PyObject;

public class JyPyDynamicLinker implements GuardingDynamicLinker, TypeBasedGuardingDynamicLinker, GuardingTypeConverterFactory {
    @Override
    public GuardedInvocation getGuardedInvocation(LinkRequest request, LinkerServices services) {
        if (!(request.getReceiver() instanceof PyObject)) {
            return null;
        }

        Operation operation = request.getCallSiteDescriptor().getOperation();
        if (DynalinkHelper.getStandardOperation(operation) == StandardOperation.CALL) {
            return null;
        }

        return null;
    }

    @Override
    public boolean canLinkType(Class<?> clazz) {
        return PyObject.class.isAssignableFrom(clazz);
    }

    @Override
    public GuardedInvocation convertToType(Class<?> sourceType, Class<?> targetType, Supplier<MethodHandles.Lookup> lookupSupplier) {
        return null;
    }
}
