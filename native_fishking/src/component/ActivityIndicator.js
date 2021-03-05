import React from 'react';
import {ActivityIndicator} from 'react-native';
import {Const} from '../Style';

export default (props) => {
  return (
    <ActivityIndicator
      size={'large'}
      color={Const.statusBar.backgroundColor}
      {...props}
    />
  );
};
