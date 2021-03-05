import React from 'react';
import {Platform, BackHandler, Linking} from 'react-native';
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
      onNavigationStateChange(state) {
        console.log(`onNavigationStateChange :: ${state.url}`);
        const {WebViewStore} = this.props;
        WebViewStore.setCanGoBack(state.canGoBack);
        WebViewStore.setRecentUrl(state.url);
      }
      async onMessage(nativeEvent) {
        const {process, data} = JSON.parse(nativeEvent.data);

        if (process === 'Clipboard') {
          // # Clipboard 복사
          Clipboard.setString(data);
        } else if (process === 'Linking') {
          // # URL-Scheme 호출
          Linking.openURL(data).catch((err) => {
            if (data.startsWith('kakaomap://')) {
              if (Platform.OS === 'android') {
                Linking.openURL('market://details?id=net.daum.android.map');
              }
            } else {
              console.log(`Linking Error -> ${data}`);
            }
          });
        } else if (process === 'Permission') {
          // # Permission 체크 및 호출
          const {AppStore} = this.props;
          if (data === 'Location') {
            // window.ReactNativeWebView.postMessage(JSON.stringify({process:'Permission',data:'Location'}))
            const check = await AppStore.checkMultiplePermission(
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
            if (!check)
              await AppStore.requestMultiplePermission(
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
          } else if (data === 'Camera') {
            // window.ReactNativeWebView.postMessage(JSON.stringify({process:'Permission',data:'Camera'}))
            const check = await AppStore.checkMultiplePermission(
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
            if (!check)
              await AppStore.requestMultiplePermission(
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
          } else if (data === 'CALL') {
            // window.ReactNativeWebView.postMessage(JSON.stringify({process:'Permission',data:'Call'}))
            const check = await AppStore.checkMultiplePermission(
              Platform.select({
                ios: [PERMISSIONS.IOS.CONTACTS],
                android: [
                  PERMISSIONS.ANDROID.CALL_PHONE,
                  PERMISSIONS.ANDROID.READ_CONTACTS,
                ],
              }),
            );
            if (!check)
              await AppStore.requestMultiplePermission(
                Platform.select({
                  ios: [PERMISSIONS.IOS.CONTACTS],
                  android: [
                    PERMISSIONS.ANDROID.CALL_PHONE,
                    PERMISSIONS.ANDROID.READ_CONTACTS,
                  ],
                }),
              );
          }
        } else if (process === 'Initiate') {
          // window.ReactNativeWebView.postMessage(JSON.stringify({process:'Initiate',data:null}))
          // # App Initiate 설정
          await AsyncStorage.setItem('@initiated', 'Y');
          this.props.WebViewStore.setApplicationUrl(
            'https://fishkingapp.com/cust/main/home',
          );
        }
      }
      onShouldStartLoadWithRequest(request) {
        console.log(`onShouldStartLoadWithRequest :: ${request.url}`);
        if (
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
    },
  ),
);
