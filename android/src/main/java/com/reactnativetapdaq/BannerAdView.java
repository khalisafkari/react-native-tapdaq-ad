package com.reactnativetapdaq;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.views.view.ReactViewGroup;
import com.tapdaq.sdk.TMBannerAdView;
import com.tapdaq.sdk.common.TMAdError;
import com.tapdaq.sdk.common.TMBannerAdSizes;
import com.tapdaq.sdk.listeners.TMAdListener;

public class BannerAdView extends ReactViewGroup {

  private final ThemedReactContext reactContext;

  private final Runnable measureRunnable = () -> {
    for (int i = 0;i < getChildCount();i++) {
      View child = getChildAt(i);
      child.measure(
        MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY),
        MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY)
      );
      child.layout(0,0, child.getMeasuredWidth(), child.getMeasuredHeight());
    }
  };

  public BannerAdView(final Context context) {
    super(context);
    this.reactContext = (ThemedReactContext) context;
  }


  public void attachView(final String placement, @Nullable  final String size) {
    final TMBannerAdView ad = new TMBannerAdView(reactContext);
    final TMBannerAdView oldView = (TMBannerAdView) getChildAt(0);

    if (oldView != null) {
      oldView.destroy(reactContext);
    }

    addView(ad, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    ad.load(reactContext.getCurrentActivity(), placement, TMBannerAdSizes.get(size), new Listener());
  }

  @Override
  public void requestLayout() {
    super.requestLayout();
    post(measureRunnable);
  }

  private class Listener extends TMAdListener {

    final TMBannerAdView oldView = (TMBannerAdView) getChildAt(0);

    @Override
    public void didLoad() {
      super.didLoad();
      SendEvent("didLoad", null);
      requestLayout();
    }

    @Override
    public void didFailToLoad(TMAdError error) {
      super.didFailToLoad(error);
      WritableMap map = Arguments.createMap();
      map.putInt("error_code", error.getErrorCode());
      map.putString("error_message", error.getErrorMessage());
      SendEvent("didFailToLoad",map);
    }

    @Override
    public void didRefresh() {
      super.didRefresh();
      SendEvent("didRefresh", null);
    }

    @Override
    public void didFailToRefresh(TMAdError error) {
      super.didFailToRefresh(error);
      WritableMap map = Arguments.createMap();
      map.putInt("error_code", error.getErrorCode());
      map.putString("error_message", error.getErrorMessage());
      SendEvent("didFailToRefresh",map);
    }

    @Override
    public void didClick() {
      super.didClick();
      SendEvent("didClick", null);
    }
  }

  private RCTEventEmitter getEmitter() {
    ReactContext context = (ReactContext) reactContext;
    return context.getJSModule(RCTEventEmitter.class);
  }

  private void SendEvent(final String eventName, @Nullable final WritableMap map) {
    getEmitter().receiveEvent(getId(),eventName, map);
  }

}
