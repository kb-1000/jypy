#include "com_github_kb1000_jypy_PyEllipsis.h"
#include "com_github_kb1000_jypy_PyNone.h"
#include "com_github_kb1000_jypy_PyNotImplemented.h"


static bool registeredEllipsis = false;
static bool registeredNone = false;
static bool registeredNotImplemented = false;

JNIEXPORT jobject jypy_Ellipsis = nullptr;
JNIEXPORT jobject jypy_None = nullptr;
JNIEXPORT jobject jypy_NotImplemented = nullptr;


JNIEXPORT jboolean JNICALL Java_com_github_kb1000_jypy_PyEllipsis_registerEllipsisSingleton(JNIEnv * env, jclass clazz, jobject instance) {
    if (registeredEllipsis) {
        return false;
    }
    registeredEllipsis = true;
    if (!instance) {
        env->ThrowNew(env->FindClass("java/lang/NullPointerException"), "instance param can't be null");
	return false;
    }
    if (!env->IsInstanceOf(instance, clazz)) {
        env->ThrowNew(env->FindClass("java/lang/ClassCastException"), "instance param is not a PyEllipsis");
        return false;
    }
    jypy_Ellipsis = instance;
    return true;
}

JNIEXPORT jboolean JNICALL Java_com_github_kb1000_jypy_PyNone_registerNoneSingleton(JNIEnv * env, jclass clazz, jobject instance) {
    if (registeredNone) {
        return false;
    }
    registeredNone = true;
    if (!instance) {
        env->ThrowNew(env->FindClass("java/lang/NullPointerException"), "instance param can't be null");
	return false;
    }
    if (!env->IsInstanceOf(instance, clazz)) {
        env->ThrowNew(env->FindClass("java/lang/ClassCastException"), "instance param is not a PyNone");
        return false;
    }
    jypy_None = instance;
    return true;
}

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
