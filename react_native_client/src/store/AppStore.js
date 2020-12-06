import {makeAutoObservable} from 'mobx';
import DialogStore from './DialogStore';
import {Platform} from 'react-native';
import {
  checkMultiple,
  PERMISSIONS,
  requestMultiple,
  RESULTS,
} from 'react-native-permissions';

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
        ]
      : [PERMISSIONS.IOS.LOCATION_ALWAYS, PERMISSIONS.IOS.LOCATION_WHEN_IN_USE];
  /** action :: 권한 여부 체크 */
  async checkPermissions() {
    let locationPermissions = await this.checkLocationPermissions();
    if (!locationPermissions)
      locationPermissions = await this.requestLocationPermissions();
    if (!locationPermissions) {
      DialogStore.openAlert('Permissions', '권한 오류');
    }
  }
  /** action :: Location 권한 체크 */
  checkLocationPermissions() {
    return new Promise(async (resolve) => {
      let permissions;
      if (Platform.OS === 'android') {
        permissions = [
          PERMISSIONS.ANDROID.ACCESS_FINE_LOCATION,
          PERMISSIONS.ANDROID.ACCESS_COARSE_LOCATION,
        ];
        const statuses = await checkMultiple(permissions);
        for (let permission of permissions) {
          if (statuses[permission] !== RESULTS.GRANTED) resolve(false);
        }
        resolve(true);
      } else {
        permissions = [
          PERMISSIONS.IOS.LOCATION_ALWAYS,
          PERMISSIONS.IOS.LOCATION_WHEN_IN_USE,
        ];
        const statuses = await checkMultiple(permissions);
        for (let permission of permissions) {
          if (statuses[permission] === RESULTS.GRANTED) resolve(true);
        }
        resolve(false);
      }
    });
  }
  /** action :: Location 권한 요청 */
  requestLocationPermissions() {
    return new Promise(async (resolve) => {
      let permissions;
      if (Platform.OS === 'android') {
        permissions = [
          PERMISSIONS.ANDROID.ACCESS_FINE_LOCATION,
          PERMISSIONS.ANDROID.ACCESS_COARSE_LOCATION,
        ];
        const statuses = await requestMultiple(this.permissions);
        for (let permission of permissions) {
          if (statuses[permission] !== RESULTS.GRANTED) resolve(false);
        }
        resolve(true);
      } else {
        permissions = [
          PERMISSIONS.IOS.LOCATION_ALWAYS,
          PERMISSIONS.IOS.LOCATION_WHEN_IN_USE,
        ];
        const statuses = await requestMultiple(permissions);
        for (let permission of permissions) {
          if (statuses[permission] === RESULTS.GRANTED) resolve(true);
        }
        resolve(false);
      }
    });
  }
})();
