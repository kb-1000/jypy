#include "com_github_kb1000_jypy_PyNotImplemented.h"

static bool registeredNotImplemented = false;
JNIEXPORT jobject jypy_NotImplemented = nullptr;

JNIEXPORT jboolean JNICALL Java_com_github_kb1000_jypy_PyNotImplemented_registerNotImplementedSingleton(JNIEnv * env, jclass clazz, jobject instance) {
    if (registeredNotImplemented) {
        return false;
    }
    registeredNotImplemented = true;
    if (!instance) {
        env->ThrowNew(env->FindClass("java/lang/NullPointerException"), "instance param can't be null");
	return false;
    }
    if (!env->IsInstanceOf(instance, clazz)) {
        env->ThrowNew(env->FindClass("java/lang/ClassCastException"), "instance param is not a PyNotImplemented");
        return false;
    }
    jypy_NotImplemented = instance;
    return true;
}
