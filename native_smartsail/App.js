import React from 'react';
import {
  Platform,
  BackHandler,
  ToastAndroid,
  Linking,
  SafeAreaView,
  StatusBar,
  View,
  Image,
  ActivityIndicator,
} from 'react-native';
import {WebView} from 'react-native-webview';
import * as SendIntentAndroid from 'react-native-send-intent';

export default class extends React.Component {
  constructor(props) {
    super(props);
    this.webview = React.createRef(null);
    this.state = {
      initiated: false,
      uri: 'https://fishkingapp.com/smartsail',
      backPressTime: 0,
    };
  }
  componentDidMount() {
    if (Platform.OS === 'android') {
      BackHandler.addEventListener('hardwareBackPress', () => {
        const now = new Date().getTime();
        if (now - this.state.backPressTime < 2000) {
          BackHandler.exitApp();
        } else {
          this.setState({backPressTime: new Date().getTime()});
          ToastAndroid.showWithGravity(
            '뒤로 가기 버튼을 한번 더 누르시면 앱이 종료됩니다',
            ToastAndroid.SHORT,
            ToastAndroid.BOTTOM,
          );
        }
        return true;
      });
    }
    setTimeout(() => {
      this.setState({initiated: true});
    }, 1800);
  }
  componentWillUnmount() {
    BackHandler.removeEventListener('hardwareBackPress');
  }
  onNavigationStateChange = status => {};
  onShouldStartLoadWithRequest = request => {
    const url = request.url;
    if (url === 'about:blank') {
      return false;
    } else if (url.search('http://') !== -1 || url.search('https://') !== -1) {
      return true;
    } else {
      if (Platform.OS === 'android') {
        SendIntentAndroid.openAppWithData(url)
          .then(isOpened => console.log(`Intent:${isOpened}`))
          .catch(err => console.log(`Intent:${err}`));
      } else {
        Linking.openURL(url);
      }
      return false;
    }
  };
  onMessage = async ({nativeEvent}) => {
    const {process, data} = JSON.parse(nativeEvent.data);
    switch (process) {
      case 'SetPermissions': {
        await Linking.openSettings();
        break;
      }
      case 'Linking': {
        Linking.openURL(data);
        break;
      }
    }
  };
  render() {
    return (
      <React.Fragment>
        <StatusBar
          barStyle={Platform.select({
            ios: 'dark-content',
            android: 'light-content',
          })}
          backgroundColor={'#3683d5'}
        />
        <SafeAreaView style={{flex: 1, backgroundColor: '#FFF'}}>
          {!this.state.initiated && (
            <View
              style={{
                backgroundColor: '#FFF',
                flex: 1,
                justifyContent: 'center',
                alignItems: 'center',
                paddingLeft: 22,
                paddingRight: 22,
                zIndex: 999,
                position: 'absolute',
                width: '100%',
                height: '100%',
              }}>
              {/*<Image*/}
              {/*  source={require('./images/splash.png')}*/}
              {/*  width={102}*/}
              {/*  height={120}*/}
              {/*/>*/}
              <ActivityIndicator
                size={'large'}
                color={'#3683d5'}
                style={{position: 'absolute', bottom: 100}}
              />
            </View>
          )}
          <WebView
            ref={this.webview}
            source={{uri: this.state.uri}}
            mixedContentMode={'always'}
            originWhitelist={['*']}
            allowFileAccess={true}
            allowFileAccessFromFileURLs={true}
            allowUniversalAccessFromFileURLs={true}
            userAgent={
              Platform.OS === 'android'
                ? 'Mozilla/5.0 (Linux; Android 10; LM-V500N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.127 Mobile Safari/537.36'
                : 'Mozilla/5.0 (iPhone; CPU iPhone OS 13_7 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.1.2 Mobile/15E148 Safari/604.1'
            }
            cacheMode={'LOAD_DEFAULT'}
            cacheEnabled={true}
            showsHorizontalScrollIndicator={false}
            showsVerticalScrollIndicator={false}
            javaScriptEnabled={true}
            domStorageEnabled={true}
            geolocationEnabled={true}
            allowsFullscreenVideo={true}
            allowsInlineMediaPlayback={true}
            allowsBackForwardNavigationGestures={true}
            onNavigationStateChange={this.onNavigationStateChange}
            onShouldStartLoadWithRequest={this.onShouldStartLoadWithRequest}
            onMessage={this.onMessage}
          />
        </SafeAreaView>
      </React.Fragment>
    );
  }
}
