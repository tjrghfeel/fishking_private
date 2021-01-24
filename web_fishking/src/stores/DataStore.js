import { makeAutoObservable } from "mobx";
import Http from "../Http";

const ModalStore = new (class {
  constructor(props) {
    makeAutoObservable(this);
  }
  /********** ********** ********** ********** **********/
  /** observable */
  /********** ********** ********** ********** **********/

  /********** ********** ********** ********** **********/
  /** action */
  /********** ********** ********** ********** **********/
  isEmail = (text) => {
    const regex = /^([0-9a-zA-Z_-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/;
    return regex.test(text);
  };
  isPassword = (text) => {
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
  isMobile = (text) => {
    const regex = /^[0][1][0-9]*$/;
    return regex.test(text);
  };
  isNickName = (text) => {
    if (text.length >= 2 && text.length <= 7) return true;
    else return false;
  };
  removeItemOfArrayByKey = (list = [], keyData = "", keyValue = "") => {
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
  updateItemOfArrayByKey = (
    list = [],
    keyData = "",
    keyValue = "",
    updateData = {}
  ) => {
    let index = -1;
    for (let i = 0; i < list.length; i++) {
      if (list[i][keyData] === keyValue) {
        index = i;
        break;
      }
    }
    const updatedItem = {
      ...list[index],
      ...updateData,
    };
    const before = list.slice(0, index);
    const after = list.slice(index + 1, list.length);
    return before.concat(updatedItem).concat(after);
  };
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

export default ModalStore;