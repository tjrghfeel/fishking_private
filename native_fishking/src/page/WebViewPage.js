import React from 'react';
import {Platform, BackHandler, Linking, Alert} from 'react-native';
import {inject, observer} from 'mobx-react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import * as SendIntentAndroid from 'react-native-send-intent';
import Clipboard from '@react-native-clipboard/clipboard';
import {PERMISSIONS} from 'react-native-permissions';
import WebView from '../component/WebView';

export default inject(
  'WebViewStore',
  'AppStore',
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
      }
      async componentDidMount() {
        const {WebViewStore} = this.props;
        if (Platform.OS === 'android') {
          BackHandler.addEventListener('hardwareBackPress', () =>
            WebViewStore.goBack(),
          );
        }
      }
      componentWillUnmount() {
        BackHandler.removeEventListener('hardwareBackPress');
      }
      async onNavigationStateChange(state) {
        const {AppStore, WebViewStore} = this.props;
        console.log(`onNavigationStateChange :: ${state.url}`);
        WebViewStore.setCanGoBack(state.canGoBack);
        WebViewStore.setRecentUrl(state.url);

        const location = await AppStore.checkLocationPermissions();
        const storage = await AppStore.checkStoragePermissions();
        const contact = await AppStore.checkContactPermissions();
        WebViewStore.runWebViewJavaScript(
          `
          window.permission_location = '${location}';
          window.permission_storage = '${storage}';
          window.permission_contact = '${contact}';
          `,
        );
      }
      onShouldStartLoadWithRequest(request) {
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
      }
      render() {
        const {WebViewStore} = this.props;
        return (
          <React.Fragment>
            <WebView
              uri={WebViewStore.applicationUrl}
              onNavigationStateChange={(state) =>
                this.onNavigationStateChange(state)
              }
              onMessage={(nativeEvent) => this.onMessage(nativeEvent)}
              onShouldStartLoadWithRequest={this.onShouldStartLoadWithRequest}
            />
          </React.Fragment>
        );
      }
      // >>>>> Javascript Message Handler
      async onMessage(nativeEvent) {
        const {AppStore, WebViewStore} = this.props;
        const {process, data} = JSON.parse(nativeEvent.data);
        switch (process) {
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
                  Linking.openURL('market://details?id=net.daum.android.map');
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
            this.props.WebViewStore.setApplicationUrl(
              'https://fishkingapp.com/cust/main/home',
            );
            break;
          }
        }
      }
    },
  ),
);
