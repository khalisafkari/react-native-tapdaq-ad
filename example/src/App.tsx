import * as React from 'react';

import { StyleSheet, View } from 'react-native';
import { TapView } from 'react-native-tapdaq';

export default function App() {
  return (
    <View style={styles.container}>
      <TapView
        style={styles.box}
        adSize={'standard'}
        didClick={() => {}}
        placementId={''}
        didRefresh={() => {}}
        didLoad={() => {}}
        didFailToRefresh={() => {}}
        didFailToLoad={() => {}}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
