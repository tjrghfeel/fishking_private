import {makeAutoObservable} from 'mobx';
import DialogStore from './DialogStore';
import {Platform} from 'react-native';

export default new (class {
  constructor() {
    makeAutoObservable(this);
  }
  /** observable :: 앱 버전 */
  version = 0.1;
})();
