import React from 'react';
import {View, Image} from 'react-native';
import ActivityIndicator from '../component/ActivityIndicator';
import {inject, observer} from 'mobx-react';

export default inject('WebViewStore')(
  observer(
    class extends React.Component {
      componentDidMount() {
        this.initiate();
      }
      initiate = async () => {
        const {WebViewStore} = this.props;
        WebViewStore.setApplicationUrl('https://fishkingapp.com/smartfishing');
        const {navigation} = this.props;
        setTimeout(() => {
          navigation.navigate('webview');
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
