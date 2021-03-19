package com.reactnativetapdaq;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

public class TapdaqViewManager extends ViewGroupManager<BannerAdView> {

  private ReactApplicationContext reactApplicationContext;
  private BannerAdView adView;
  private String adId;
  private String format;



  @NonNull
  @Override
  public String getName() {
    return "TapdaqView";
  }

  public TapdaqViewManager(ReactApplicationContext context) {
    this.reactApplicationContext = context;
  }

  @NonNull
  @Override
  protected BannerAdView createViewInstance(@NonNull ThemedReactContext reactContext) {
    adView = new BannerAdView(reactContext);
    return adView;
  }

  @ReactProp(name = "placementId")
  public void setPlacementId(final BannerAdView view, final @Nullable String placementId) {
    this.adId = placementId;
    adView.attachView(placementId,format);
  }

  @ReactProp(name = "adSize")
  public void setAdSize(final BannerAdView view, final @Nullable String adSize) {
    this.format = adSize;
    adView.attachView(adId, adSize);
  }

  @Override
  public @Nullable Map<String, Object> getExportedCustomDirectEventTypeConstants() {
    return MapBuilder.of(
      "didLoad",
      MapBuilder.of("registrationName","didLoad"),
      "didFailToLoad",
      MapBuilder.of("registrationName","didFailToLoad"),
      "didRefresh",
      MapBuilder.of("registrationName","didRefresh"),
      "didFailToRefresh",
      MapBuilder.of("registrationName","didFailToRefresh"),
      "didClick",
      MapBuilder.of("registrationName","didClick")
    );
  }
}
