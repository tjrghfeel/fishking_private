import React from 'react';
import {StatusBar} from 'react-native';
import {Const} from '../Style';

export default () => {
  return (
    <StatusBar
      barStyle={Const.statusBar.barStyle}
      backgroundColor={Const.statusBar.backgroundColor}
    />
  );
};
