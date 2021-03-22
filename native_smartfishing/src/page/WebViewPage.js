import React from 'react';
import {Platform, BackHandler} from 'react-native';
import {inject, observer} from 'mobx-react';
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
      }
      onShouldStartLoadWithRequest(request) {
        if (request.url === 'about:blank') return false;
        else return true;
      }
      render() {
        const {WebViewStore} = this.props;
        return (
          <React.Fragment>
            <WebView
              uri={WebViewStore.applicationUrl}
              onNavigationStateChange={state =>
                this.onNavigationStateChange(state)
              }
              onMessage={nativeEvent => this.onMessage(nativeEvent)}
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
          default:
            break;
        }
      }
    },
  ),
);
