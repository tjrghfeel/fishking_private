import {makeAutoObservable} from 'mobx';
import {Alert} from 'react-native';

export default new (class {
  constructor() {
    makeAutoObservable(this);
  }
  /** action :: Alert 표시 */
  openAlert(title, message, buttons, options) {
    options = {
      cancelable: false,
      ...(options || {}),
    };
    Alert.alert(title, message, buttons, options);
  }
})();
