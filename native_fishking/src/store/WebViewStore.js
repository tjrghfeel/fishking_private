import {makeAutoObservable} from 'mobx';
import {BackHandler, Platform} from 'react-native';

export default new (class {
  constructor() {
    makeAutoObservable(this);
  }
  applicationUrl = 'http://3.34.51.244:40080/';
  /** observable :: webview 객체 */
  webview = null;
  /** observable :: 최근 히스토리 도메인 */
  recentUrl = null;
  /** action :: webview 객체 설정 */
  setWebView(webview) {
    this.webview = webview;
  }
  /** observable :: useragent */
  useragent =
    Platform.OS === 'android'
      ? 'Mozilla/5.0 (Linux; Android 10; LM-V500N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.127 Mobile Safari/537.36'
      : 'Mozilla/5.0 (iPhone; CPU iPhone OS 13_7 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.1.2 Mobile/15E148 Safari/604.1';
  /** action :: inject javascript */
  executeJavascript(script) {
    this.webview.current.injectJavaScript(script);
  }
  /** action :: post window message */
  postWindowMessage(data) {
    this.webview.current.injectJavaScript(
      `window.postMessage(` +
        JSON.stringify(data) +
        `,'*');
      true;`,
    );
  }
  /** observable :: canGoBack */
  canGoBack = false;
  /** action :: canGoBack 설정 */
  setCanGoBack(canGoBack) {
    this.canGoBack = canGoBack;
  }
  /** action :: recentUrl 설정 */
  setRecentUrl(recentUrl) {
    this.recentUrl = recentUrl;
  }
  /** action :: goBack */
  goBack() {
    if (this.canGoBack) {
      this.webview.current.goBack();
      return true;
    } else {
      BackHandler.exitApp();
      return true;
    }
  }
})();
