import React from 'react';
import {SafeAreaView} from 'react-native';
import {Style} from '../Style';

export default ({children}) => {
  return <SafeAreaView style={Style.safeAreaView}>{children}</SafeAreaView>;
};
