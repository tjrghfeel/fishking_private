import React from 'react';
import {NavigationContainer} from '@react-navigation/native';
import {Provider} from 'mobx-react';
import Store from './store';
import Navigation from './Navigation';

export default () => {
  return (
    <Provider {...Store}>
      <NavigationContainer>
        <Navigation />
      </NavigationContainer>
    </Provider>
  );
};
