import React from 'react';
import {Platform, BackHandler, ToastAndroid} from 'react-native';
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
        this.state = {
          backPressTime: 0,
        };
      }
      async componentDidMount() {
        const {WebViewStore} = this.props;
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
            // WebViewStore.goBack();
            return true;
          });
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
          case 'Exit': {
            // >>>>> 앱 종료
            BackHandler.exitApp();
            break;
          }
          default:
            break;
        }
      }
    },
  ),
);
