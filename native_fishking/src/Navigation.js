import React, {useEffect} from 'react';
import {inject, observer} from 'mobx-react';
import StatusBar from './component/StatusBar';
import SafeAreaView from './component/SafeAreaView';
import {createStackNavigator} from '@react-navigation/stack';

const RootStackNav = createStackNavigator();

import WebViewPage from './page/WebViewPage';

export default inject(
  'AppStore',
  'DialogStore',
)(
  observer(({AppStore}) => {
    useEffect(() => {
      AppStore.isGrantedPermissions();
    });
    return (
      <>
        <StatusBar />
        <SafeAreaView>
          <RootStackNav.Navigator screenOptions={{headerShown: false}}>
            <RootStackNav.Screen name={'webview'} component={WebViewPage} />
          </RootStackNav.Navigator>
        </SafeAreaView>
      </>
    );
  }),
);
