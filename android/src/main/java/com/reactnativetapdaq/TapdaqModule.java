package com.reactnativetapdaq;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.tapdaq.sdk.STATUS;
import com.tapdaq.sdk.Tapdaq;
import com.tapdaq.sdk.TapdaqConfig;
import java.util.Map;

public class TapdaqModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

  private final ReactApplicationContext reactContext;

  private String TAG = this.getName();

  private static final String KEY_USER_SUBJECT_TO_GDPR = "userSubjectToGDPR";
  private static final String KEY_CONSENT_GIVEN = "consentGiven";
  private static final String KEY_IS_AGE_RESTRICTED_USER = "isAgeRestrictedUser";

  public TapdaqModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }



  @NonNull
  @Override
  public String getName() {
    return "RNTapdaqModule";
  }

  @ReactMethod
  public void initialize(String appId, String clientId, Promise promise) {
    new TapdaqInitListener(reactContext.getCurrentActivity(),appId, clientId, promise);
  }

  @ReactMethod
  public void initializeWithConfig(String applicationId, String clientKey, ReadableMap config, Promise promise) {
    TapdaqConfig tapdaqConfig = new TapdaqConfig();
    ReadableMapKeySetIterator iterator = config.keySetIterator();
    while (iterator.hasNextKey()) {
      String key = iterator.nextKey();
      Boolean value = config.getBoolean(key);
      switch (key) {
        case KEY_CONSENT_GIVEN:
          tapdaqConfig.setConsentStatus(value ? STATUS.TRUE : STATUS.FALSE);
          break;
        case KEY_IS_AGE_RESTRICTED_USER:
          tapdaqConfig.setAgeRestrictedUserStatus(value ? STATUS.TRUE : STATUS.FALSE);
          break;
        case KEY_USER_SUBJECT_TO_GDPR:
          tapdaqConfig.setUserSubjectToGdprStatus(value ? STATUS.TRUE : STATUS.FALSE);
          break;
        default:
          Log.d(TAG, String.format("Unknown config key (%s) passed to initialize()", key));
      }
    }
    new TapdaqInitListener(reactContext.getCurrentActivity(), applicationId, clientKey, tapdaqConfig, promise);
  }

  @ReactMethod(isBlockingSynchronousMethod = true)
  public boolean isInitialized() {
    return Tapdaq.getInstance().IsInitialised();
  }

  @ReactMethod
  public void setConsentGiven(int value) { // 0 = false, 1 = true, default: UNKNOWN
    Tapdaq.getInstance().setConsentGiven(reactContext.getApplicationContext(),STATUS.valueOf(value));
  }

  @ReactMethod
  public void setUserSubjectToGDPR(int value) {
    Tapdaq.getInstance().setUserSubjectToGDPR(reactContext.getApplicationContext(), STATUS.valueOf(value));
  }

  @ReactMethod
  public void setIsAgeRestrictedUser(int value) {
    Tapdaq.getInstance().setIsAgeRestrictedUser(reactContext.getApplicationContext(), STATUS.valueOf(value));
  }

  @ReactMethod
  public void setAdMobContentRating(String value) {
    Tapdaq.getInstance().config().setAdMobContentRating(value);
  }

  private void sendLog(@Nullable final String log) {
    Log.d(TAG, log);
    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("tapdaq", log);
  }

  @ReactMethod
  public void loadRewardedVideo(String placement,Promise promise) {
    try {
      Tapdaq.getInstance().loadRewardedVideo(reactContext.getCurrentActivity(), placement, new AdListener(reactContext));
      promise.resolve(true);
    } catch (Exception e) {
      sendLog(e.getMessage());
      promise.reject(e);
    }
  }

  @ReactMethod
  public void showRewardedVideo(String placement, Promise promise) {
    try {
      boolean isReady = Tapdaq.getInstance().isRewardedVideoReady(reactContext.getCurrentActivity(), placement);
      if (isReady) {
        Tapdaq.getInstance().showRewardedVideo(reactContext.getCurrentActivity(), placement, new AdListener(reactContext));
        promise.resolve(true);
      } else {
        Exception error = new Exception();
        promise.reject(error);
        sendLog(error.getMessage());
      }
    } catch (Exception err) {
      sendLog(err.getMessage());
      promise.reject(err);
    }
  }

  @ReactMethod
  public void loadInterstitial(String placement, Promise promise) {
    try {
      Tapdaq.getInstance().loadInterstitial(reactContext.getCurrentActivity(), placement, new AdListener(reactContext));
      promise.resolve(true);
    } catch (Exception err) {
      sendLog(err.getMessage());
      promise.reject(err);
    }
  }

  @ReactMethod
  public void showInterstitial(String placement, Promise promise) {
    try {
      boolean isReady = Tapdaq.getInstance().isInterstitialReady(reactContext.getCurrentActivity(), placement);
      if (isReady) {
        Tapdaq.getInstance().showInterstitial(reactContext.getCurrentActivity(), placement, new AdListener(reactContext));
        return;
      } else {
        Exception exception = new Exception("cannot ready");
        sendLog(exception.getMessage());
        promise.reject(exception);
      }
    } catch (Exception err) {
      sendLog(err.getMessage());
      promise.reject(err);
    }
  }

  @ReactMethod
  public void loadVideo(String placement, Promise promise) {
    try {
      Tapdaq.getInstance().loadVideo(reactContext.getCurrentActivity(), placement, new AdListener(reactContext));
      promise.resolve(true);
    } catch (Exception err) {
      promise.reject(err);
      sendLog(err.getMessage());
    }
  }

  @ReactMethod
  public void showVideo(String placement, Promise promise) {
    try {
      boolean isReady = Tapdaq.getInstance().isVideoReady(reactContext.getCurrentActivity(), placement);
      if (isReady) {
        Tapdaq.getInstance().showVideo(reactContext.getCurrentActivity(), placement, new AdListener(reactContext));
        promise.resolve(true);
        return;
      } else {
        Exception exception = new Exception("failed show video");
        promise.resolve(exception);
        sendLog(exception.getMessage());
        return;
      }
    } catch (Exception err) {
      sendLog(err.getMessage());
      promise.resolve(err);
    }
  }

  @ReactMethod(isBlockingSynchronousMethod = true)
  public void startTestActivity() {
    Tapdaq.getInstance().startTestActivity(reactContext.getCurrentActivity());
  }

  @Nullable
  @Override
  public Map<String, Object> getConstants() {
    return super.getConstants();
  }

  @Override
  public void onHostResume() {

  }

  @Override
  public void onHostPause() {

  }

  @Override
  public void onHostDestroy() {
    if (Tapdaq.getInstance().IsInitialised()) {
     Tapdaq.getInstance().config().setAutoReloadAds(true);
    }
  }
}
