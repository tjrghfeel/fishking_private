import {makeAutoObservable} from 'mobx';
import AsyncStorage from '@react-native-async-storage/async-storage';

export default new (class {
  constructor() {
    makeAutoObservable(this);
  }
  /** action :: 객체데이터 저장 */
  async setObject(key, object) {
    try {
      await AsyncStorage.setItem(key, JSON.stringify(object));
      return true;
    } catch (err) {
      return false;
    }
  }
  /** action :: 데이터 저장 */
  async setData(key, data) {
    try {
      await AsyncStorage.setItem(key, data);
      return true;
    } catch (err) {
      return false;
    }
  }
  /** action :: 객체데이터 가져오기 */
  async getObject(key) {
    try {
      const stringify = (await AsyncStorage.getItem(key)) || null;
      if (stringify !== null) return JSON.parse(stringify);
      else return null;
    } catch (err) {
      return null;
    }
  }
  /** action :: 데이터 가져오기 */
  async getData(key) {
    try {
      return (await AsyncStorage.getItem(key)) || null;
    } catch (err) {
      return null;
    }
  }
})();
