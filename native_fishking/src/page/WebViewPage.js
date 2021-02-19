import React from 'react';
import {Platform, BackHandler, Linking} from 'react-native';
import {inject, observer} from 'mobx-react';
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
        const {WebViewStore} = this.props;
        WebViewStore.setCanGoBack(state.canGoBack);
        WebViewStore.setRecentUrl(state.url);
      }
      onMessage(nativeEvent) {
        console.log(JSON.stringify(nativeEvent));
        const {process, data} = JSON.parse(nativeEvent.data);

        if (process === 'Linking') {
          Linking.openURL(data);
        }
      }
      onShouldStartLoadWithRequest(request) {
        const isHTTPS = request.url.search('https://') !== -1;

        if (isHTTPS) {
          return true;
        } else {
          Linking.openURL(request.url);
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
