import { makeAutoObservable } from "mobx";
import Http from "../../commons/Http";

export interface fishspeciesProps {
  id: number;
  code: string;
  codeName: string;
}

export class CodeStore {
  constructor() {
    makeAutoObservable(this);
  }
  /** enum 데이터 조회 */
  REST_GET_commonCode_value = async (
    type: string = "tideTime",
    columnLength: number = 0
  ) => {
    try {
      const resolve = await Http._get("/v2/api/value");

      const enums: Array<any> = [];
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
      console.error("[REST] 프로필 > 탈퇴하기 :: " + JSON.stringify(err));
      return null;
    }
  };
  /** 코드 데이터 조회 :: n열 가공 */
  REST_GET_commonCode_group = async (
    groupId: string,
    columnLength: number = 0
  ) => {
    try {
      const resolve = await Http._get("/v2/api/commonCode/" + groupId);

      const arr_code = [];
      if (resolve.list && resolve.list.length > 0) {
        let row = [];
        for (let i = 0; i < resolve.list.length; i++) {
          const item = resolve.list[i];
          const { id, code, codeName } = item;
          if (columnLength === 0) {
            arr_code.push({ id, code, codeName });
          } else {
            row.push({ id, code, codeName });
            if (row.length === columnLength) {
              arr_code.push(row);
              row = [];
            }
            if (i === resolve.list.length - 1) {
              while (row.length < columnLength) {
                row.push({ id: null, code: null, codeName: null });
              }
              arr_code.push(row);
            }
          }
        }
      }

      return arr_code;
    } catch (err) {
      console.error("[REST] 프로필 > 탈퇴하기 :: " + JSON.stringify(err));
      return null;
    }
  };
}

export const createStore = () => {
  return new CodeStore();
};

export type TCodeStore = ReturnType<typeof createStore>;
