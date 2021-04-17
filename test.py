from __future__ import print_function
import gc

getattr(gc, "addJythonGCFlags", lambda f: None)(getattr(gc, "MONITOR_GLOBAL", None))


def a():
    o = object()
    yield o
    ref = gc.get_referrers(o)
    print(ref)
    t = next(iter(filter(lambda t: isinstance(t, tuple), ref)))
    print(t)
    print(t[1])


tuple(a())
