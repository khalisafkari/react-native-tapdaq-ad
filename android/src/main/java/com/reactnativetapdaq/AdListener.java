package com.reactnativetapdaq;

import android.util.Log;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.tapdaq.sdk.common.TMAdError;
import com.tapdaq.sdk.listeners.TMAdListener;
import com.tapdaq.sdk.model.rewards.TDReward;

import java.util.Locale;

public class AdListener extends TMAdListener {

  private ReactApplicationContext context;

  public AdListener(ReactApplicationContext context) {
    this.context = context;
  }


  private void SendEvent(final String name, @Nullable final WritableMap params) {
    context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(name, params);
  }

  @Override
  public void didLoad() {
    super.didLoad();
    SendEvent("didLoad", null);
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
    map.putString("error_message",error.getErrorMessage());
    map.putInt("error_code", error.getErrorCode());
    SendEvent("didFailToRefresh", map);
  }

  @Override
  public void willDisplay() {
    super.willDisplay();
    SendEvent("willDisplay", null);
  }

  @Override
  public void didDisplay() {
    super.didDisplay();
    SendEvent("didDisplay", null);
  }

  @Override
  public void didFailToDisplay(TMAdError error) {
    super.didFailToDisplay(error);
    WritableMap map = Arguments.createMap();
    map.putString("error_message",error.getErrorMessage());
    map.putInt("error_code", error.getErrorCode());
    SendEvent("didFailToDisplay", map);
  }

  @Override
  public void didClick() {
    super.didClick();
    SendEvent("didClick", null);
  }

  @Override
  public void didClose() {
    super.didClose();
    SendEvent("didClose", null);
  }

  @Override
  public void didFailToLoad(TMAdError error) {
    super.didFailToLoad(error);
    WritableMap map = Arguments.createMap();
    map.putString("error_message",error.getErrorMessage());
    map.putInt("error_code", error.getErrorCode());
    SendEvent("didFailToLoad", map);
  }

  @Override
  public void didComplete() {
    super.didComplete();
    SendEvent("didComplete", null);
  }

  @Override
  public void didEngagement() {
    super.didEngagement();
    SendEvent("didEngagement", null);
  }

  @Override
  public void didRewardFail(TMAdError error) {
    super.didRewardFail(error);
    WritableMap map = Arguments.createMap();
    map.putString("error_message",error.getErrorMessage());
    map.putInt("error_code", error.getErrorCode());
    SendEvent("didRewardFail", map);
  }

  @Override
  public void onUserDeclined() {
    super.onUserDeclined();
    SendEvent("onUserDeclined", null);
  }

  @Override
  public void didVerify(TDReward reward) {
    super.didVerify(reward);
    WritableMap map = Arguments.createMap();
    map.putInt("reward_value", reward.getValue());
    map.putString("reward_event_id", reward.getEventId());
    map.putString("reward_name", reward.getName());
    map.putBoolean("reward_valid", reward.isValid());
    map.putString("reward_id", reward.getRewardId());
    SendEvent("didVerify", map);
    Log.i("MEDIATION-SAMPLE", String.format(Locale.ENGLISH, "didVerify: ID: %s, Tag: %s. Reward: %s. Value: %d. Valid: %b. Custom Json: %s", reward.getRewardId(), reward.getTag(), reward.getName(), reward.getValue(), reward.isValid(), reward.getCustom_json().toString()));
  }


}
