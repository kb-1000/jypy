import contextlib

class IndentLogger:
    _indent = 0

    @contextlib.contextmanager
    def indent(self, s=None):
        if s is not None:
            self.write(s)
        self._indent += 1
        yield
        self._indent -= 1

    def write(self, s):
        print(f"{'    '*self._indent}{s}")

names = ["Ellipsis", "None", "NotImplemented"]
log = IndentLogger()
with log.indent("Generating Java classes..."):
    for name in names:
        with log.indent(f"Generating Py{name} Java class"):
            with open(f"src/main/java/com/github/kb1000/jypy/Py{name}.java", "w", encoding="utf-8") as fp:
                fp.write(f"""\
package com.github.kb1000.jypy;

public final class Py{name} extends PyObject {{
    private Py{name}() {{
        synchronized (Py{name}.class) {{
            if (!register{name}Singleton(this)) {{
                throw new UnsupportedOperationException("Can not create more than one instance of Py{name}!");
            }}
        }}
    }}

    public static final Py{name} INSTANCE = new Py{name}();

    /**
     * This is used to prevent bad people from hackimg into this class using reflection and make more than one instance.
     * It will also be used to publish this instance to native code.
     *
     * @return whether there is already an instance
     */
    private static native boolean register{name}Singleton(Py{name} instance);
}}
""")

with log.indent("Generating singletons.cpp..."):
    with open("src/main/cpp/singletons.cpp", "w", encoding="utf-8") as fp:
        with log.indent("Generating JNI include directives..."):
            for name in names:
                with log.indent(f"Generating JNI include directives for Py{name}..."):
                    fp.write(f"#include \"com_github_kb1000_jypy_Py{name}.h\"\n")
        fp.write("\n\n")
        with log.indent("Generating private registration check variables..."):
            for name in names:
                with log.indent(f"Generating private registration check variable for Py{name}..."):
                    fp.write(f"static bool registered{name} = false;\n")
        fp.write("\n")
        with log.indent("Generating public variables for registered instances..."):
            for name in names:
                with log.indent(f"Generating public variable for registered Py{name}..."):
                    fp.write(f"JNIEXPORT jobject jypy_{name} = nullptr;\n")
        fp.write("\n")
        with log.indent("Generating JNI registration functions..."):
            for name in names:
                with log.indent(f"Generating JNI registration function for Py{name}..."):
                    fp.write(f"""\

JNIEXPORT jboolean JNICALL Java_com_github_kb1000_jypy_Py{name}_register{name}Singleton(JNIEnv * env, jclass clazz, jobject instance) {{
    if (registered{name}) {{
        return false;
    }}
    registered{name} = true;
    if (!instance) {{
        env->ThrowNew(env->FindClass("java/lang/NullPointerException"), "instance param can't be null");
	return false;
    }}
    if (!env->IsInstanceOf(instance, clazz)) {{
        env->ThrowNew(env->FindClass("java/lang/ClassCastException"), "instance param is not a Py{name}");
        return false;
    }}
    jypy_{name} = instance;
    return true;
}}
""")
