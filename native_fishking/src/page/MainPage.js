import React, {useState, useEffect, useCallback, useRef} from 'react';
import {
  Platform,
  BackHandler,
  ToastAndroid,
  View,
  Image,
  ActivityIndicator,
  Linking,
  Alert,
  Share,
} from 'react-native';
import {inject, observer} from 'mobx-react';
import {WebView} from 'react-native-webview';
import * as SendIntentAndroid from 'react-native-send-intent';
import AsyncStorage from '@react-native-async-storage/async-storage';
import {token} from '../../messaging';
import Clipboard from '@react-native-clipboard/clipboard';
import {PERMISSIONS} from 'react-native-permissions';
import NetInfo from '@react-native-community/netinfo';

export default inject(
  'WebViewStore',
  'AppStore',
)(
  observer(({WebViewStore, AppStore}) => {
    const [backPressTime, setBackPressTime] = useState(0);
    const [initiated, setInitiated] = useState(false);
    const [uri, setUri] = useState('https://www.naver.com');
    const webview = useRef(null);
    /********** ********** **********/
    /********** functions
    /********** ********** **********/
    const wokeUp = useCallback((e) => {
      console.log('wokeUp: ' + e.url);
      let redirectUrl = e.url;
      if (redirectUrl && redirectUrl.split('?')[1]) {
        if (redirectUrl.includes('tiny.one')) {
          redirectUrl = redirectUrl.replaceAll(
            '%20https://tiny.one/25r3whkw',
            '',
          );
        }
        console.log(
          'redirectUrl : ' +
            'https://fishkingapp.com/cust' +
            redirectUrl.split('?')[1],
        );
        setUri('https://fishkingapp.com/cust' + redirectUrl.split('?')[1]);
      }
    }, []);
    const initiate = useCallback(async () => {
      const saved = await AsyncStorage.getItem('@initiated');
      if (saved === null) {
        setUri('https://fishkingapp.com/cust/init/intro');
      } else {
        setUri('https://fishkingapp.com/cust/main/home');
        // setUri('http://192.168.0.50:3000/cust/main/home');
      }
      const location = await AppStore.checkLocationPermissions();
      const storage = await AppStore.checkStoragePermissions();
      const contact = await AppStore.checkContactPermissions();
      setTimeout(() => {
        WebViewStore.runWebViewJavaScript(
          `
          window.isNative = true;
          window.fcm_token = '${token}';
          window.wversion = 2;
          window.permission_location = '${location}';
          window.permission_storage = '${storage}';
          window.permission_contact = '${contact}';
          `,
        );
        setInitiated(true);
      }, 1800);

      Linking.getInitialURL()
        .then((url) => {
          console.log('init: ' + url);
          let redirectUrl = url;
          if (redirectUrl && redirectUrl.split('?')[1]) {
            if (url.includes('tiny.one')) {
              redirectUrl = url.replaceAll('%20https://tiny.one/25r3whkw', '');
            }
            console.log(
              'redirectUrl : ' +
                'https://fishkingapp.com/cust' +
                redirectUrl.split('?')[1],
            );
            setUri('https://fishkingapp.com/cust' + redirectUrl.split('?')[1]);
          }
        })
        .catch((e) => {});
      Linking.addEventListener('url', wokeUp);
    }, [setUri, setInitiated, AppStore, WebViewStore, wokeUp]);
    useEffect(() => {
      WebViewStore.setWebView(webview);
      initiate();

      if (Platform.OS === 'android') {
        BackHandler.addEventListener('hardwareBackPress', () => {
          if (
            WebViewStore.navigationState?.url.includes(
              'https://fishkingapp.com',
            )
          ) {
            WebViewStore.postWindowMessage('goBack');
          } else {
            webview.current.goBack();
          }
          return true;

          // const now = new Date().getTime();
          // if (now - backPressTime < 2000) {
          //   BackHandler.exitApp();
          // } else {
          //   setBackPressTime(new Date().getTime());
          //   ToastAndroid.showWithGravity(
          //     '?????? ?????? ????????? ?????? ??? ???????????? ?????? ???????????????',
          //     ToastAndroid.SHORT,
          //     ToastAndroid.BOTTOM,
          //   );
          // }
          // return true;
        });
        return () => {
          BackHandler.removeEventListener('hardwareBackPress');
        };
      }
    }, [backPressTime, setBackPressTime, initiate, WebViewStore, wokeUp]);

    /********** ********** **********/
    /********** render
     /********** ********** **********/
    return (
      <React.Fragment>
        {!initiated && (
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
            <Image
              source={require('../images/splash.png')}
              width={102}
              height={120}
            />
            <ActivityIndicator
              size={'large'}
              color={'#3683d5'}
              style={{position: 'absolute', bottom: 100}}
            />
          </View>
        )}
        <WebView
          ref={webview}
          source={{uri}}
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
          textZoom={100}
          onNavigationStateChange={(state) =>
            WebViewStore.setNavigationState(state)
          }
          onShouldStartLoadWithRequest={(request) => {
            if (request.url === 'about:blank') {
              return false;
            } else if (
              request.url.startsWith('https://') ||
              request.url.startsWith('http://')
            ) {
              return true;
            } else {
              if (Platform.OS === 'android') {
                SendIntentAndroid.openAppWithUri(request.url)
                  .then((isOpened) => {
                    console.log('Intent -> ' + isOpened);
                  })
                  .catch((err) => {
                    console.log('IntentError -> ' + JSON.stringify(err));
                  });
              } else {
                Linking.openURL(request.url);
              }
              return false;
            }
          }}
          onMessage={async ({nativeEvent}) => {
            const {process, data} = JSON.parse(nativeEvent.data);
            switch (process) {
              case 'SetPlayWifi': {
                // console.log('set setting: ', data);
                await AsyncStorage.setItem('@playwifi', data);
                break;
              }
              case 'GetPlayWifi': {
                const playWifi = await AsyncStorage.getItem('@playwifi');
                // console.log('get setting: ', playWifi);
                webview.current.postMessage(playWifi, '*');
                break;
              }
              case 'Connections': {
                const playWifi = await AsyncStorage.getItem('@playwifi');
                NetInfo.fetch().then((state) => {
                  // console.log(state.type);
                  if (playWifi === 'Y') {
                    webview.current.postMessage(state.type, '*');
                  } else {
                    webview.current.postMessage('wifi', '*');
                  }
                });
                break;
              }
              case 'SetPermissions': {
                await Linking.openSettings();
                break;
              }
              case 'Share': {
                if (data.message.includes('map.kakao.com')) {
                  const result = await Share.share({
                    message: data.message,
                    url: data.message,
                    title: '???????????? ???????????? ??????',
                  });
                } else {
                  const result = await Share.share({
                    message: data.message,
                    url: data.message,
                    title: '????????????',
                  });
                }
                break;
              }
              case 'Refresh': {
                const enabled = data.enabled;
                AppStore.setRefreshEnabled(enabled);
                console.log(enabled);
                break;
              }
              case 'Exit': {
                // >>>>> ??? ??????
                BackHandler.exitApp();
                break;
              }
              case 'Linking': {
                // >>>>> URL Scheme ??????
                Linking.openURL(data).catch((err) => {
                  if (data.startsWith('kakaomap://')) {
                    if (Platform.OS === 'android') {
                      Linking.openURL(
                        'market://details?id=net.daum.android.map',
                      );
                    } else {
                      Linking.openURL(
                        'https://itunes.apple.com/us/app/id304608425?mt=8',
                      );
                    }
                  } else {
                    console.log(`Linking Error -> ${data}`);
                  }
                });
                break;
              }
              case 'Clipboard': {
                // >>>>> ???????????? ??????
                Clipboard.setString(data);
                Alert.alert(null, '?????????????????????.');
                // if (Platform.OS !== 'android') {
                //   Alert.alert(null, '?????????????????????.');
                // }
                break;
              }
              case 'Initiate': {
                // >>>>> ?????????????????? ????????? ??????
                await AsyncStorage.setItem('@initiated', 'Y');
                this.props.WebViewStore.setApplicationUrl(
                  'https://fishkingapp.com/cust/main/home',
                );
                break;
              }
              case 'Permissions': {
                // >>>>> ??? ?????? ?????? ??????
                const {
                  location = false,
                  alarm = false,
                  storage = false,
                  contact = false,
                } = data;
                console.log(location);
                console.log(storage);
                console.log(contact);
                if (location) {
                  await AppStore.grantPermissions(
                    Platform.select({
                      ios: [
                        PERMISSIONS.IOS.LOCATION_ALWAYS,
                        PERMISSIONS.IOS.LOCATION_WHEN_IN_USE,
                      ],
                      android: [
                        PERMISSIONS.ANDROID.ACCESS_FINE_LOCATION,
                        PERMISSIONS.ANDROID.ACCESS_COARSE_LOCATION,
                      ],
                    }),
                  );
                }
                if (storage) {
                  await AppStore.grantPermissions(
                    Platform.select({
                      ios: [
                        PERMISSIONS.IOS.CAMERA,
                        PERMISSIONS.IOS.PHOTO_LIBRARY,
                        PERMISSIONS.IOS.PHOTO_LIBRARY_ADD_ONLY,
                      ],
                      android: [
                        PERMISSIONS.ANDROID.CAMERA,
                        PERMISSIONS.ANDROID.READ_EXTERNAL_STORAGE,
                        PERMISSIONS.ANDROID.WRITE_EXTERNAL_STORAGE,
                      ],
                    }),
                  );
                }
                if (contact) {
                  await AppStore.grantPermissions(
                    Platform.select({
                      ios: [PERMISSIONS.IOS.CONTACTS],
                      android: [
                        PERMISSIONS.ANDROID.CALL_PHONE,
                        PERMISSIONS.ANDROID.READ_CONTACTS,
                      ],
                    }),
                  );
                }
                await AsyncStorage.setItem('@initiated', 'Y');
                setUri('https://fishkingapp.com/cust/main/home');
                break;
              }
            }
          }}
        />
      </React.Fragment>
    );
  }),
);
