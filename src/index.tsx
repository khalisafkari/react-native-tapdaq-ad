import {
  requireNativeComponent,
  ViewStyle,
  NativeModules,
  NativeEventEmitter,
  StyleProp,
} from 'react-native';
const { RNTapdaqModule } = NativeModules;

const AdFormat = { STANDARD: 'standard', MEDIUM: 'medium' };

const eventName = {
  didLoad: 'didLoad',
  didRefresh: 'didRefresh',
  didFailToRefresh: 'didFailToRefresh',
  willDisplay: 'willDisplay',
  didDisplay: 'didDisplay',
  didFailToDisplay: 'didFailToDisplay',
  didClick: 'didClick',
  didClose: 'didClose',
  didFailToLoad: 'didFailToLoad',
  didComplete: 'didComplete',
  didEngagement: 'didEngagement',
  didRewardFail: 'didRewardFail',
  onUserDeclined: 'onUserDeclined',
  didVerify: 'didVerify',
};

interface subscriptions {
  [key: string]: any;
}

const emitter = new NativeEventEmitter(RNTapdaqModule);
const subscriptions: subscriptions = {};

type handler = (event: any) => void;

const addEventListener = (event: string, handler: handler) => {
  let subscription = emitter.addListener(event, handler);
  let currentSubscription = subscriptions[event];
  if (currentSubscription) {
    currentSubscription.remove();
  }
  subscriptions[event] = subscription;
};

const removeEventListener = (event: string) => {
  let currentSubscription = subscriptions[event];
  if (currentSubscription) {
    currentSubscription.remove();
    delete subscriptions[event];
  }
};

type config = {
  initialize: (appId: string, key: string) => Promise<any>;
  initializeWithConfig: (
    appId: string,
    key: string,
    config: any
  ) => Promise<any>;
  isInitialized(): boolean;
  setConsentGiven(id: number): void;
  setUserSubjectToGDPR(id: number): void;
  setIsAgeRestrictedUser(id: number): void;
  setAdMobContentRating(rating: string): void;
  loadRewardedVideo: (placement: string) => Promise<any>;
  showRewardedVideo: (placement: string) => Promise<any>;
  loadInterstitial: (placement: string) => Promise<any>;
  showInterstitial: (placement: string) => Promise<any>;
  loadVideo: (placement: string) => Promise<any>;
  showVideo: (placement: string) => Promise<any>;
  startTestActivity(): void;
};
const ModuleAd = RNTapdaqModule as config;
type TapdaqProps = {
  placementId: string;
  style: StyleProp<ViewStyle>;
  adSize: string;
  didLoad: () => void;
  didFailToLoad: (event: any) => void;
  didRefresh: () => void;
  didFailToRefresh: (event: any) => void;
  didClick: () => void;
};

const TapdaqViewManager = requireNativeComponent<TapdaqProps>('TapdaqView');
const TapView = TapdaqViewManager;

export { TapView, AdFormat, addEventListener, removeEventListener, eventName };
export default ModuleAd;
