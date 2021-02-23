import React from 'react';
import {Platform, BackHandler, Linking} from 'react-native';
import {inject, observer} from 'mobx-react';
import * as SendIntentAndroid from 'react-native-send-intent';
import Clipboard from '@react-native-clipboard/clipboard';
import WebView from '../component/WebView';

export default inject('WebViewStore')(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
      }
      componentDidMount() {
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
      onMessage(nativeEvent) {
        console.log(JSON.stringify(nativeEvent));
        const {process, data} = JSON.parse(nativeEvent.data);

        if (process === 'Clipboard') {
          Clipboard.setString(data);
        } else if (process === 'Linking') {
          Linking.openURL(data).catch((err) => {
            if (data.startsWith('kakaomap://')) {
              if (Platform.OS === 'android') {
                Linking.openURL('market://details?id=net.daum.android.map');
              }
            } else {
              console.log(`Linking Error -> ${data}`);
            }
          });
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
        const {
          WebViewStore: {applicationUrl},
        } = this.props;
        return (
          <React.Fragment>
            <WebView
              uri={applicationUrl}
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
