#include <jni.h>
#include <android/bitmap.h>
#include <android/log.h>
#include <cstring>
#include <vector>
#include <algorithm>

#define LOG_TAG "VideoCompressor"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_bombaday_app_util_VideoCompressor_compressImageNative(
        JNIEnv* env,
        jobject /* this */,
        jobject bitmap,
        jint quality) {

    AndroidBitmapInfo info;
    void* pixels;

    if (AndroidBitmap_getInfo(env, bitmap, &info) < 0) {
        LOGE("Failed to get bitmap info");
        return nullptr;
    }

    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        LOGE("Bitmap format is not RGBA_8888");
        return nullptr;
    }

    if (AndroidBitmap_lockPixels(env, bitmap, &pixels) < 0) {
        LOGE("Failed to lock bitmap pixels");
        return nullptr;
    }

    int width = info.width;
    int height = info.height;
    
    // Simple compression: reduce to RGB (remove alpha) and apply quality reduction
    int targetWidth = width;
    int targetHeight = height;
    
    // Reduce size based on quality
    if (quality < 80) {
        float scale = quality / 100.0f;
        targetWidth = (int)(width * scale);
        targetHeight = (int)(height * scale);
        
        if (targetWidth < 1) targetWidth = 1;
        if (targetHeight < 1) targetHeight = 1;
    }

    std::vector<uint8_t> rgbData;
    rgbData.reserve(targetWidth * targetHeight * 3);

    uint32_t* pixelArray = (uint32_t*)pixels;
    
    // Simple downsampling
    for (int y = 0; y < targetHeight; y++) {
        for (int x = 0; x < targetWidth; x++) {
            int srcX = (x * width) / targetWidth;
            int srcY = (y * height) / targetHeight;
            
            uint32_t pixel = pixelArray[srcY * width + srcX];
            
            uint8_t r = (pixel >> 16) & 0xFF;
            uint8_t g = (pixel >> 8) & 0xFF;
            uint8_t b = pixel & 0xFF;
            
            rgbData.push_back(r);
            rgbData.push_back(g);
            rgbData.push_back(b);
        }
    }

    AndroidBitmap_unlockPixels(env, bitmap);

    jbyteArray result = env->NewByteArray(rgbData.size());
    env->SetByteArrayRegion(result, 0, rgbData.size(), 
                           reinterpret_cast<const jbyte*>(rgbData.data()));

    LOGI("Compressed image from %dx%d to %dx%d (quality: %d)", 
         width, height, targetWidth, targetHeight, quality);

    return result;
}

extern "C" JNIEXPORT void JNICALL
Java_com_bombaday_app_util_VideoCompressor_applyBlurNative(
        JNIEnv* env,
        jobject /* this */,
        jobject bitmap,
        jint radius) {

    AndroidBitmapInfo info;
    void* pixels;

    if (AndroidBitmap_getInfo(env, bitmap, &info) < 0) {
        LOGE("Failed to get bitmap info");
        return;
    }

    if (AndroidBitmap_lockPixels(env, bitmap, &pixels) < 0) {
        LOGE("Failed to lock bitmap pixels");
        return;
    }

    int width = info.width;
    int height = info.height;
    uint32_t* pixelArray = (uint32_t*)pixels;

    std::vector<uint32_t> output(width * height);

    // Simple box blur
    int r = std::min(radius, 20);
    
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            int totalR = 0, totalG = 0, totalB = 0, totalA = 0;
            int count = 0;

            for (int ky = -r; ky <= r; ky++) {
                for (int kx = -r; kx <= r; kx++) {
                    int px = std::clamp(x + kx, 0, width - 1);
                    int py = std::clamp(y + ky, 0, height - 1);
                    
                    uint32_t pixel = pixelArray[py * width + px];
                    
                    totalA += (pixel >> 24) & 0xFF;
                    totalR += (pixel >> 16) & 0xFF;
                    totalG += (pixel >> 8) & 0xFF;
                    totalB += pixel & 0xFF;
                    count++;
                }
            }

            uint8_t a = totalA / count;
            uint8_t red = totalR / count;
            uint8_t green = totalG / count;
            uint8_t blue = totalB / count;

            output[y * width + x] = (a << 24) | (red << 16) | (green << 8) | blue;
        }
    }

    std::memcpy(pixelArray, output.data(), width * height * sizeof(uint32_t));
    AndroidBitmap_unlockPixels(env, bitmap);

    LOGI("Applied blur with radius %d to %dx%d image", radius, width, height);
}
