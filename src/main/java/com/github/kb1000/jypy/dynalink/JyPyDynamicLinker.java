package com.github.kb1000.jypy.dynalink;

import java.lang.invoke.MethodHandles;
import java.util.function.Supplier;

import jdk.dynalink.linker.GuardedInvocation;
import jdk.dynalink.linker.GuardingDynamicLinker;
import jdk.dynalink.linker.GuardingTypeConverterFactory;
import jdk.dynalink.linker.LinkRequest;
import jdk.dynalink.linker.LinkerServices;
import jdk.dynalink.linker.TypeBasedGuardingDynamicLinker;

import com.github.kb1000.jypy.PyObject;

public class JyPyDynamicLinker implements GuardingDynamicLinker, TypeBasedGuardingDynamicLinker, GuardingTypeConverterFactory {
    @Override
    public GuardedInvocation getGuardedInvocation(LinkRequest request, LinkerServices services) {
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
