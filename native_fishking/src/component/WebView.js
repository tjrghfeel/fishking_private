import React, {useRef, useEffect} from 'react';
import {WebView} from 'react-native-webview';
import {inject, observer} from 'mobx-react';
import {token} from '../../messaging';

export default inject('WebViewStore')(
  observer(
    ({
      WebViewStore,
      uri,
      onNavigationStateChange,
      onMessage,
      onShouldStartLoadWithRequest,
    }) => {
      const webview = useRef(null);
      useEffect(() => {
        WebViewStore.setWebView(webview);
        WebView.isFileUploadSupported().then((res) => {
          if (res === true) {
            console.log('file upload supported');
          } else {
            console.log('file upload not supported');
          }
        });
      });

      return (
        <WebView
          ref={webview}
          source={{uri}}
          originWhitelist={['*']}
          allowFileAccess={true}
          allowFileAccessFromFileURLs={true}
          allowUniversalAccessFromFileURLs={true}
          userAgent={WebViewStore.useragent}
          cacheMode={'LOAD_NO_CACHE'}
          // cacheEnabled={true}
          showsHorizontalScrollIndicator={false}
          showsVerticalScrollIndicator={false}
          javaScriptEnabled={true}
          domStorageEnabled={true}
          geolocationEnabled={true}
          allowsFullscreenVideo={true}
          allowsInlineMediaPlayback={true}
          allowsBackForwardNavigationGestures={true}
          onNavigationStateChange={(state) => onNavigationStateChange(state)}
          onMessage={({nativeEvent}) => onMessage(nativeEvent)}
          onShouldStartLoadWithRequest={onShouldStartLoadWithRequest}
          injectedJavaScriptBeforeContentLoaded={`
            window.isNative = true;
            window.fcm_token = '${token}'
            true;
            `}
        />
      );
    },
  ),
);
