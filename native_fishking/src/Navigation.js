import React from 'react';
import {inject, observer} from 'mobx-react';
import {SafeAreaView, StatusBar, Platform} from 'react-native';

import MainPage from './page/MainPage';

export default inject()(
  observer(() => {
    return (
      <>
        <StatusBar
          barStyle={Platform.select({
            ios: 'dark-content',
            android: 'light-content',
          })}
          backgroundColor={'#3683d5'}
        />
        <SafeAreaView style={{flex: 1, backgroundColor: '#FFF'}}>
          <MainPage />
        </SafeAreaView>
      </>
    );
  }),
);
