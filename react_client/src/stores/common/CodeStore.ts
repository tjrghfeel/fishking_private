import { makeAutoObservable, runInAction } from "mobx";
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
  /** 어종 코드 데이터 */
  fishspecies: Array<fishspeciesProps> = [];
  /** 어종 코드 데이터 :: n x 3 */
  fishspecies2: Array<Array<fishspeciesProps>> = [];
  /** 어종 코드 데이터 로드 */
  loadFishspecies = () => {
    return new Promise((resolve) => {
      Http.request(
        "GET",
        "/v2/api/commonCode/80",
        null,
        null,
        (response: any) => {
          runInAction(() => {
            if (response.success && response.list.length > 0) {
              // --> 어종 코드 데이터
              for (let fish of response.list) {
                const { id, code, codeName } = fish;
                this.fishspecies.push({ id, code, codeName });
              }

              // --> 어종 코드 데이터 :: n x 3
              let tmp = [];
              for (let i = 0; i < response.list.length; i++) {
                const { id, code, codeName } = response.list[i];
                tmp.push({ id, code, codeName });
                if (tmp.length === 3) {
                  this.fishspecies2.push(tmp);
                  tmp = [];
                }
                if (i === response.list.length - 1) {
                  while (tmp.length < 3) {
                    tmp.push({ id: "blank", code: null, codeName: null });
                  }

                  this.fishspecies2.push(tmp);
                }
              }
            }
            resolve(true);
          });
        },
        (err: any) => {
          resolve(false);
        }
      );
    });
  };
}

export const createStore = () => {
  return new CodeStore();
};

export type TCodeStore = ReturnType<typeof createStore>;
