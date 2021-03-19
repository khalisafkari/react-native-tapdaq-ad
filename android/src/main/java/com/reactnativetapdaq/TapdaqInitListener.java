package com.reactnativetapdaq;

import android.app.Activity;

import com.facebook.react.bridge.Promise;
import com.tapdaq.sdk.Tapdaq;
import com.tapdaq.sdk.TapdaqConfig;
import com.tapdaq.sdk.common.TMAdError;
import com.tapdaq.sdk.listeners.TMInitListener;

public class TapdaqInitListener extends TMInitListener {

  private Promise promise;

  public TapdaqInitListener(Activity activity, String applicationId, String clientKey, Promise promise1) {
    promise = promise1;
    Tapdaq.getInstance().initialize(activity, applicationId, clientKey, null, this);
  }

  public TapdaqInitListener(Activity activity, String applicationId, String clientKey, TapdaqConfig config, Promise promise1) {
    promise = promise1;
    Tapdaq.getInstance().initialize(activity, applicationId, clientKey, config, this);
  }


  @Override
  public void didInitialise() {
    super.didInitialise();
    promise.resolve(true);
  }

  @Override
  public void didFailToInitialise(TMAdError error) {
    super.didFailToInitialise(error);
    promise.reject(String.valueOf(error.getErrorCode()), error.getErrorMessage());
  }
}
