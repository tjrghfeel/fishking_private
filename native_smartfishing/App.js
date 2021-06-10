import React from 'react';
import {
  Platform,
  Linking,
  BackHandler,
  Share,
  StatusBar,
  SafeAreaView,
  ScrollView,
  RefreshControl,
  View,
  ActivityIndicator,
  Image,
} from 'react-native';
import {WebView} from 'react-native-webview';
import * as SendIntentAndroid from 'react-native-send-intent';
import {inject, observer, Provider} from 'mobx-react';
import {token} from './messaging';
import RNKakaoLink from 'react-native-kakao-links';

const SplashScreen = () => {
  return (
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
      <Image source={require('./splash.png')} width={102} height={120} />
      <ActivityIndicator
        size={'large'}
        color={'#3683d5'}
        style={{position: 'absolute', bottom: 100}}
      />
    </View>
  );
};

const App = inject()(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.webview = React.createRef(null);
        this.state = {
          initiated: false,
          uri: 'https://fishkingapp.com/smartfishing',
          // uri: 'http://112.220.72.178:3000/smartfishing',
          refreshEnabled: false,
          navigationState: null,
        };
      }
      componentDidMount() {
        if (Platform.OS === 'android') {
          BackHandler.addEventListener('hardwareBackPress', () => {
            if (
              this.state.navigationState?.url.includes(
                'https://fishkingapp.com',
              )
            ) {
              this.postMessage('goBack');
            } else {
              this.webview.current?.goBack();
            }
            return true;
          });
        }
        this.initPage();
      }
      componentWillUnmount() {
        BackHandler.removeEventListener('hardwareBackPress');
      }
      initPage = async () => {
        setTimeout(() => {
          this.setState({initiated: true});
        }, 1800);
      };
      postMessage = data => {
        this.executeJavaScript(
          `window.postMessage(${JSON.stringify(data)},'*'); true;`,
        );
      };
      executeJavaScript = script => {
        this.webview.current?.injectJavaScript(script);
      };
      onNavigationStateChange = async state => {
        await this.setState({navigationState: state});
      };

      kakaoLink = async url => {
        console.log(url);
        try {
          const options = {
            objectType: 'scrap',
            url: url,
          };
          const response = await RNKakaoLink.link(options);
          console.log(response);
        } catch (e) {
          console.warn(e);
        }
      };

      onMessage = async ({nativeEvent}) => {
        const {process, data} = JSON.parse(nativeEvent.data);
        switch (process) {
          case 'SetPermissions': {
            await Linking.openSettings();
            break;
          }
          case 'Exit': {
            // ----- > 앱종료
            BackHandler.exitApp();
            break;
          }
          case 'Refresh': {
            // ----- > RefreshControl Enabled
            const enabled = data['enabled'] || false;
            this.setState({refreshEnabled: enabled == 'Y'});
            break;
          }
          case 'Share': {
            // ----- > 공유하기
            const message = data['message'];
            const resolve = await Share.share({
              message,
              url: message,
              title: message,
            });
            break;
          }
          case 'Linking': {
            // ----- > URL-scheme 호출
            Linking.openURL(data).catch(err => {
              console.error(`[Linking.openURL] ${JSON.stringify(err)}`);
            });
            break;
          }
          case 'SNS': {
            Linking.canOpenURL(data).then((supported) => {
              if (supported) {
                Linking.openURL(data);
              } else {
                if (data.startsWith('bandapp')) {
                  Linking.openURL(data.replace('bandapp://create/post?text', 'https://band.us/plugin/share?body'))
                }
              }
            });
            break;
          }
          case 'SNS-kakao': {
            await this.kakaoLink(data);
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
              {!this.state.initiated && <SplashScreen />}
              <ScrollView
                contentContainerStyle={{flex: 1}}
                refreshControl={
                  <RefreshControl enabled={this.state.refreshEnabled} />
                }>
                <WebView
                  ref={this.webview}
                  source={{uri: this.state.uri}}
                  mixedContentMode={'always'}
                  originWhitelist={['*']}
                  cacheMode={'LOAD_DEFAULT'}
                  cacheEnabled={true}
                  userAgent={
                    Platform.OS === 'android'
                      ? 'Mozilla/5.0 (Linux; Android 10; LM-V500N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.127 Mobile Safari/537.36'
                      : 'Mozilla/5.0 (iPhone; CPU iPhone OS 13_7 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.1.2 Mobile/15E148 Safari/604.1'
                  }
                  allowFileAccess={true}
                  allowFileAccessFromFileURLs={true}
                  allowUniversalAccessFromFileURLs={true}
                  showsHorizontalScrollIndicator={false}
                  showsVerticalScrollIndicator={false}
                  javaScriptEnabled={true}
                  domStorageEnabled={true}
                  geolocationEnabled={true}
                  allowsFullscreenVideo={true}
                  allowsInlineMediaPlayback={true}
                  allowsBackForwardNavigationGestures={true}
                  textZoom={100}
                  injectedJavaScript={`
                    window.isNative = true;
                    window.fcm_token = '${token}';
                    window.wversion = 2;
                  `}
                  onNavigationStateChange={this.onNavigationStateChange}
                  onMessage={this.onMessage}
                  onShouldStartLoadWithRequest={request => {
                    console.log(
                      `[onShouldStartLoadWithRequest] ${JSON.stringify(
                        request,
                      )}`,
                    );
                    const {url} = request;
                    if (
                      url.search('https://') !== -1 ||
                      url.search('http://') !== -1
                    ) {
                      return true;
                    } else if (url.search('about:blank') !== -1) {
                      return false;
                    } else {
                      if (Platform.OS === 'android') {
                        SendIntentAndroid.openAppWithUri(url)
                          .then(isOpened => console.log(`[Intent] ${isOpened}`))
                          .catch(err =>
                            console.error(`[Intent] ${JSON.stringify(err)}`),
                          );
                      } else {
                        Linking.openURL(url);
                      }
                      return false;
                    }
                  }}
                />
              </ScrollView>
            </SafeAreaView>
          </React.Fragment>
        );
      }
    },
  ),
);

export default () => {
  return (
    <Provider>
      <App />
    </Provider>
  );
};
