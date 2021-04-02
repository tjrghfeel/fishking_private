import React, {useState} from 'react';
import {inject, observer} from 'mobx-react';
import {
  SafeAreaView,
  StatusBar,
  Platform,
  ScrollView,
  RefreshControl,
} from 'react-native';

import MainPage from './page/MainPage';

export default inject('WebViewStore')(
  observer(({WebViewStore}) => {
    const [refreshing, setRefreshing] = useState(false);
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
          {/*<ScrollView*/}
          {/*  contentContainerStyle={{*/}
          {/*    flex: 1,*/}
          {/*  }}*/}
          {/*  refreshControl={*/}
          {/*    <RefreshControl*/}
          {/*      refreshing={refreshing}*/}
          {/*      onRefresh={() => {*/}
          {/*        WebViewStore.executeJavascript(`window.location.reload();`);*/}
          {/*      }}*/}
          {/*    />*/}
          {/*  }>*/}
          <MainPage />
          {/*</ScrollView>*/}
        </SafeAreaView>
      </>
    );
  }),
);
