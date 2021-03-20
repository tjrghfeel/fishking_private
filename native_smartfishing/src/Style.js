import React from 'react';
import {StyleSheet, Platform} from 'react-native';

export const Style = StyleSheet.create({
  safeAreaView: {
    flex: 1,
    backgroundColor: '#FFF',
  },
});

export const Const = {
  statusBar: {
    barStyle: Platform.select({
      ios: 'dark-content',
      android: 'light-content',
    }),
    backgroundColor: '#3683d5',
  },
};
