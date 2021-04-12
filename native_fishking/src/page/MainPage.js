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
    const initiate = useCallback(async () => {
      const saved = await AsyncStorage.getItem('@initiated');
      if (saved === null) {
        setUri('https://fishkingapp.com/cust/init/intro');
      } else {
        setUri('https://fishkingapp.com/cust/main/home');
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
    }, [setUri, setInitiated, AppStore, WebViewStore]);
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
          //     '뒤로 가기 버튼을 한번 더 누르시면 앱이 종료됩니다',
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
    }, [backPressTime, setBackPressTime, initiate, WebViewStore]);
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
          onNavigationStateChange={(state) =>
            WebViewStore.setNavigationState(state)
          }
          onShouldStartLoadWithRequest={(request) => {
            if (request.url === 'about:blank') return false;
            else if (
              request.url.search('https://') !== -1 ||
              request.url.search('http://') !== -1
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
              case 'Share': {
                const result = await Share.share({
                  message: data.message,
                  url: data.message,
                  title: '어복황제',
                });
                break;
              }
              case 'Refresh': {
                const enabled = data['enabled'];
                AppStore.setRefreshEnabled(enabled);
                console.log(enabled);
                break;
              }
              case 'Exit': {
                // >>>>> 앱 종료
                BackHandler.exitApp();
                break;
              }
              case 'Linking': {
                // >>>>> URL Scheme 호출
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
                // >>>>> 클립보드 복사
                Clipboard.setString(data);
                if (Platform.OS !== 'android') {
                  Alert.alert(null, '복사되었습니다.');
                }
                break;
              }
              case 'Initiate': {
                // >>>>> 어플리케이션 초기화 여부
                await AsyncStorage.setItem('@initiated', 'Y');
                this.props.WebViewStore.setApplicationUrl(
                  'https://fishkingapp.com/cust/main/home',
                );
                break;
              }
              case 'Permissions': {
                // >>>>> 앱 접근 권한 요청
                const {
                  location = false,
                  alarm = false,
                  storage = false,
                  contact = false,
                } = data;
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
