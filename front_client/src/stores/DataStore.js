import { makeAutoObservable } from "mobx";
import Http from "../Http";

const DataStore = new (class {
  constructor(props) {
    makeAutoObservable(this);
  }
  /********** ********** ********** ********** **********/
  /** observable */
  /********** ********** ********** ********** **********/

  /********** ********** ********** ********** **********/
  /** action */
  /********** ********** ********** ********** **********/
  isValidEmail = (text) => {
    const regex = /^([0-9a-zA-Z_-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/;
    return regex.test(text);
  };
  isValidPassword = (text) => {
    const regex1 = /^(?=.*[a-zA-Z])(?=.*[0-9]).{8,15}$/;
    const regex2 = /^(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).{8,15}$/;
    const regex3 = /^(?=.*[^a-zA-Z0-9])(?=.*[0-9]).{10,12}$/;

    if (
      text.length >= 8 &&
      (regex1.test(text) || regex2.test(text) || regex3.test(text))
    ) {
      return true;
    } else return false;
  };
  isValidMobile = (text) => {
    const regex = /^[0][1][0-9]*$/;
    return regex.test(text);
  };
  isValidNickName = (text) => {
    if (text.length >= 2 && text.length <= 7) return true;
    else return false;
  };
  // --> 날짜 객체로부터 요일 조회
  getWeek = (date) => {
    try {
      const day = date.getDay();
      switch (day) {
        case 0:
          return "일";
          break;
        case 1:
          return "월";
          break;
        case 2:
          return "화";
          break;
        case 3:
          return "수";
          break;
        case 4:
          return "목";
          break;
        case 5:
          return "금";
          break;
        case 6:
          return "토";
          break;
      }
    } catch (err) {
      return null;
    }
  };
  // --> array 로부터 item 삭제 후 리스트 반환하기 : keyData = id 가 되는 키네임, keyValue = 키밸류
  removeItemOfArray = (list = [], keyData = "", keyValue = "") => {
    let index = -1;
    for (let i = 0; i < list.length; i++) {
      if (list[i][keyData] === keyValue) {
        index = i;
        break;
      }
    }
    const before = list.slice(0, index);
    const after = list.slice(index + 1, list.length);
    return before.concat(after);
  };
  // --> n분전 포맷 :: 포맷 이외의 경우 기준 날짜 반환
  latestTimeFormat = (dateString) => {
    try {
      const second = 1000;
      const minute = second * 60;
      const hour = minute * 60;

      const when = new Date(dateString);
      const when_year = when.getFullYear();
      const when_month =
        when.getMonth() + 1 < 10
          ? "0".concat(when.getMonth() + 1)
          : when.getMonth() + 1;
      const when_date =
        when.getDate() < 10 ? "0".concat(when.getDate()) : when.getDate();

      const prev = when.getTime();
      const now = new Date().getTime();
      const between = now - prev;

      if (between > hour * 2) {
        // 2시간 ~ :: 기준 날짜
        return when_year + "." + when_month + "." + when_date + "";
      } else if (between <= hour * 2 && between >= hour) {
        // 1시간 ~ 2시간 :: 1시간전
        return "1시간 전";
      } else if (between < minute) {
        // ~ 1분 :: 방금 전
        return "방금 전";
      } else if (between < hour) {
        // ~ 1시간 :: n분 전
        return Math.round(between / minute) + "분 전";
      } else {
        return "";
      }
    } catch (err) {
      return "";
    }
  };
  // --> n분전 타임시간
  latestTimeMillis = (dateString) => {
    try {
      const second = 1000;
      const minute = second * 60;
      const hour = minute * 60;

      const when = new Date(dateString);
      const when_year = when.getFullYear();
      const when_month =
        when.getMonth() + 1 < 10
          ? "0".concat(when.getMonth() + 1)
          : when.getMonth() + 1;
      const when_date =
        when.getDate() < 10 ? "0".concat(when.getDate()) : when.getDate();

      const prev = when.getTime();
      const now = new Date().getTime();
      return now - prev;
    } catch (err) {
      return "";
    }
  };
  /** --> enum 데이터 목록 조회 */
  getEnums = async (type, columnLength = 0) => {
    try {
      const resolve = await Http._get("/v2/api/value");

      const enums = [];
      if (resolve[type] && resolve[type].length > 0) {
        let row = [];
        for (let i = 0; i < resolve[type].length; i++) {
          const item = resolve[type][i];
          const { key, value } = item;
          if (columnLength === 0) {
            enums.push({ key, value });
          } else {
            row.push({ key, value });
            if (row.length === columnLength) {
              enums.push(row);
              row = [];
            }
            if (i === resolve[type].length - 1) {
              while (row.length < columnLength) {
                row.push({ key: null, value: null });
              }
              enums.push(row);
            }
          }
        }
      }

      return enums;
    } catch (err) {
      console.error(err);
      return null;
    }
  };
  /** --> enum 데이터 조회 */
  getEnumValue = async (type, key) => {
    try {
      const resolve = await Http._get("/v2/api/value");
      if (resolve[type] && resolve[type].length > 0) {
        for (let enm of resolve[type]) {
          if (enm.key === key) return enm;
        }
      }
      return null;
    } catch (err) {
      console.error(err);
      return null;
    }
  };
  /** --> enum index 기준으로 데이터 조회 */
  getEnumValueByIndex = async (type, index = 0) => {
    try {
      const resolve = await Http._get("/v2/api/value");
      if (resolve[type] && resolve[type].length > 0) {
        return resolve[type][new Number(index)];
      }
      return null;
    } catch (err) {
      console.error(err);
      return null;
    }
  };
  /** --> 코드 데이터 목록 조회 */
  getCodes = async (groupId, columnLength = 0) => {
    try {
      const resolve = await Http._get("/v2/api/commonCode/" + groupId);

      const codes = [];
      if (resolve.list && resolve.list.length > 0) {
        let row = [];
        for (let i = 0; i < resolve.list.length; i++) {
          const item = resolve.list[i];
          const { id, code, codeName } = item;
          if (columnLength === 0) {
            codes.push({ id, code, codeName });
          } else {
            row.push({ id, code, codeName });
            if (row.length === columnLength) {
              codes.push(row);
              row = [];
            }
            if (i === resolve.list.length - 1) {
              while (row.length < columnLength) {
                row.push({ id: null, code: null, codeName: null });
              }
              codes.push(row);
            }
          }
        }
      }

      return codes;
    } catch (err) {
      console.error(err);
      return null;
    }
  };
})();

export default DataStore;
