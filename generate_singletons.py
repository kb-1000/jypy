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
        print(f"{'    ' * self._indent}{s}")


names = ["Ellipsis", "None", "NotImplemented"]
log = IndentLogger()
with log.indent("Generating Java classes..."):
    for name in names:
        with log.indent(f"Generating Py{name} Java class"):
            with open(f"src/main/java/com/github/kb1000/jypy/Py{name}.java", "w", encoding="utf-8") as fp:
                fp.write(f"""\
package com.github.kb1000.jypy;

public final class Py{name} extends PyObject {{
    /**
     * Do not create any additional instances. Doing so may corrupt the interpreter state, and <strong>will</strong>
     * cause wrong behavior on the newly created instance. You have been warned.
     */
    private Py{name}() {{
        if (INSTANCE != null) {{
            throw new UnsupportedOperationException("Can not create more than one instance of Py{name}!");
        }}
    }}

    public static final Py{name} INSTANCE = new Py{name}();
}}
""")
