import React, {useRef, useEffect} from 'react';
import {WebView} from 'react-native-webview';
import {inject, observer} from 'mobx-react';

export default inject('WebViewStore')(
  observer(({WebViewStore, uri, onNavigationStateChange, onMessage}) => {
    const webview = useRef(null);
    useEffect(() => {
      WebViewStore.setWebView(webview);
    });
    return (
      <WebView
        ref={webview}
        source={{uri}}
        originWhitelist={['*']}
        userAgent={WebViewStore.useragent}
        cacheMode={'LOAD_NO_CACHE'}
        showsHorizontalScrollIndicator={false}
        showsVerticalScrollIndicator={false}
        javaScriptEnabled={true}
        domStorageEnabled={true}
        geolocationEnabled={true}
        allowsBackForwardNavigationGestures={true}
        onNavigationStateChange={(state) => onNavigationStateChange(state)}
        onMessage={({nativeEvent}) => onMessage(nativeEvent)}
        injectedJavaScriptBeforeContentLoaded={`
            window.isNative = true;
            true;
            `}
      />
    );
  }),
);
