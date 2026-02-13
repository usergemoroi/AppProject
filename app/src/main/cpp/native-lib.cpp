#include <jni.h>
#include <string>
#include <android/log.h>

#define LOG_TAG "BombaDay-Native"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

extern "C" JNIEXPORT jstring JNICALL
Java_com_bombaday_app_util_NativeLib_getVersion(
        JNIEnv* env,
        jobject /* this */) {
    std::string version = "BombaDay Native v1.0.0";
    LOGI("Native library loaded: %s", version.c_str());
    return env->NewStringUTF(version.c_str());
}
