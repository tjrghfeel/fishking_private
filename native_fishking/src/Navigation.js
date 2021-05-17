import React, {useState} from 'react';
import {inject, observer} from 'mobx-react';
import {
  SafeAreaView,
  StatusBar,
  Platform,
  ScrollView,
  RefreshControl,
  View
} from 'react-native';

import MainPage from './page/MainPage';

export default inject(
  'WebViewStore',
  'AppStore',
)(
  observer(({WebViewStore, AppStore}) => {
    const [refreshing, setRefreshing] = useState(false);
    return (
      <>
        <SafeAreaView style={{flex: 1, backgroundColor: '#FFF'}}>
          <StatusBar
            barStyle={Platform.select({
              ios: 'dark-content',
              android: 'light-content',
            })}
            backgroundColor={'#3683d5'}
          />
          <View style={{flex: 1}}>
            <MainPage />
          </View>
          {/*<ScrollView*/}
          {/*  contentContainerStyle={{*/}
          {/*    flex: 1,*/}
          {/*  }}*/}
          {/*  refreshControl={*/}
          {/*    <RefreshControl*/}
          {/*      enabled={AppStore.refreshEnabled == 'Y'}*/}
          {/*      refreshing={refreshing}*/}
          {/*      onRefresh={() => {*/}
          {/*        WebViewStore.executeJavascript(`window.location.reload();`);*/}
          {/*      }}*/}
          {/*    />*/}
          {/*  }>*/}
          {/*  <MainPage />*/}
          {/*</ScrollView>*/}
        </SafeAreaView>
      </>
    );
  }),
);
