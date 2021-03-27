import React from 'react';
import {View, Image} from 'react-native';
import ActivityIndicator from '../component/ActivityIndicator';
import {inject, observer} from 'mobx-react';
import AsyncStorage from '@react-native-async-storage/async-storage';

export default inject(
  'WebViewStore',
  'AppStore',
)(
  observer(
    class extends React.Component {
      componentDidMount() {
        this.initiate();
      }
      initiate = async () => {
        const initiated = await AsyncStorage.getItem('@initiated');
        const {WebViewStore} = this.props;
        if (initiated === null) {
          WebViewStore.setApplicationUrl(
            'https://fishkingapp.com/cust/init/intro',
          );
        } else {
          WebViewStore.setApplicationUrl(
            'https://fishkingapp.com/cust/main/home',
          );
        }
        const {navigation} = this.props;
        setTimeout(() => {
          // navigation.navigate('webview');
          this.props.AppStore.setSplashed(true);
        }, 1500);
      };
      render() {
        return (
          <View
            style={{
              backgroundColor: '#FFF',
              flex: 1,
              justifyContent: 'center',
              alignItems: 'center',
              paddingLeft: 22,
              paddingRight: 22,
            }}>
            <Image
              source={require('../images/splash.png')}
              width={102}
              height={120}
            />
            <ActivityIndicator style={{position: 'absolute', bottom: 100}} />
          </View>
        );
      }
    },
  ),
);
