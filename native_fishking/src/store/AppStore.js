import {makeAutoObservable} from 'mobx';
import DialogStore from './DialogStore';
import {Platform} from 'react-native';
import {check, request, PERMISSIONS, RESULTS} from 'react-native-permissions';

export default new (class {
  constructor() {
    makeAutoObservable(this);
  }
  /** observable :: 앱 버전 */
  version = 0.1;

  /** observable :: permissions 배열 */
  permissions =
    Platform.OS === 'android'
      ? [
          PERMISSIONS.ANDROID.ACCESS_FINE_LOCATION,
          PERMISSIONS.ANDROID.ACCESS_COARSE_LOCATION,
          PERMISSIONS.ANDROID.CAMERA,
          PERMISSIONS.ANDROID.READ_EXTERNAL_STORAGE,
          PERMISSIONS.ANDROID.WRITE_EXTERNAL_STORAGE,
        ]
      : [
          PERMISSIONS.IOS.LOCATION_ALWAYS,
          PERMISSIONS.IOS.LOCATION_WHEN_IN_USE,
          PERMISSIONS.IOS.CAMERA,
          PERMISSIONS.IOS.PHOTO_LIBRARY,
        ];
  /** action :: 권한 여부 체크 */
  isGrantedPermissions = async () => {
    return new Promise(async (resolve) => {
      try {
        for (let permission of this.permissions) {
          let granted = await this.checkPermission(permission);
          if (!granted) {
            granted = await this.requestPermission(permission);
          }
          if (!granted) {
            DialogStore.openAlert('알림', '권한동의가 필요합니다.');
          }
          console.log(permission + ' -> ' + granted);
        }
        resolve(true);
      } catch (err) {
        console.log(JSON.stringify(err));
        resolve(false);
      }
    });
  };
  /** action : 퍼미션 체크 */
  checkPermission = async (permission) => {
    return new Promise((resolve) => {
      check(permission).then((result) => {
        if (result === RESULTS.GRANTED) resolve(true);
        else resolve(false);
      });
    });
  };
  /** action : 퍼미션 요청 */
  requestPermission = async (permission) => {
    return new Promise((resolve) => {
      request(permission).then((result) => {
        if (result === RESULTS.GRANTED) resolve(true);
        else resolve(false);
      });
    });
  };
})();
