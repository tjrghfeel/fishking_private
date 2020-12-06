import React from 'react';
import {Platform, BackHandler} from 'react-native';
import {inject, observer} from 'mobx-react';
import WebView from '../component/WebView';

export default inject('WebViewStore')(
  observer(
    class extends React.Component {
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
      }
      onMessage(nativeEvent) {}
      render() {
        return (
          <WebView
            uri={'https://fishing.devcodeits.com/'}
            onNavigationStateChange={(state) =>
              this.onNavigationStateChange(state)
            }
            onMessage={(nativeEvent) => this.onMessage(nativeEvent)}
          />
        );
      }
    },
  ),
);
