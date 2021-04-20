##### 어복황제 고객앱

---
##### PG 등 외부 인텐트 호출 관련 모듈 설정
```
* react-native-send-intent
```
```
// android/setting.gradle
include ':RNSendIntentModule', ':app'
project(':RNSendIntentModule').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-send-intent/android')
```
```
// android/app/build.gradle
...
dependencies {
    ...
    compile project(':RNSendIntentModule')
}
```
```
// MainApplication.java
import com.burnweb.rnsendintent.RNSendIntentPackage;
...
```
```
// node_modules/react-native-send-intent/android/src/main/java/com/burnweb/rnsendintent/RNSendIntentModule.java
...
    @ReactMethod
        public void openAppWithUri(String intentUri, ReadableMap extras, final Promise promise) {
            try {
                Intent intent = Intent.parseUri(intentUri, Intent.URI_INTENT_SCHEME);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Intent existPackage = this.reactContext.getPackageManager().getLaunchIntentForPackage(intent.getPackage());
                if (existPackage != null) {
                    this.reactContext.startActivity(intent);
                } else {
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                    marketIntent.setData(Uri.parse("market://details?id="+intent.getPackage()));
                    marketIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    this.reactContext.startActivity(marketIntent);
                }
                promise.resolve(true);
            } catch (Exception e) {
                promise.resolve(false);
            }
        }
...
```
```
// node_modules/react-native-send-intent/index.js
...
    openAppWithUri(intentUri, extras) {
            return RNSendIntentAndroid.openAppWithUri(intentUri, extras || {});
    }
...
```
---
