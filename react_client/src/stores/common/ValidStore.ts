import { makeAutoObservable } from "mobx";

export class ValidStore {
  constructor() {
    makeAutoObservable(this);
  }
  /** 이메일 형식 검증 */
  isEmail = (text: string) => {
    const regex = /^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/;
    return regex.test(text);
  };

  /** 전화번호 형식 검증 :: 숫자만 */
  isPhoneNo = (text: string) => {
    const regex = /^[0][1][0-9]*$/;
    return regex.test(text);
  };

  /** 이모티콘 포함 여부 :: 4byte unicode */
  containsEmoji = (text: string) => {
    const regex = /(?:[\u2700-\u27bf]|(?:\ud83c[\udde6-\uddff]){2}|[\ud800-\udbff][\udc00-\udfff]|[\u0023-\u0039]\ufe0f?\u20e3|\u3299|\u3297|\u303d|\u3030|\u24c2|\ud83c[\udd70-\udd71]|\ud83c[\udd7e-\udd7f]|\ud83c\udd8e|\ud83c[\udd91-\udd9a]|\ud83c[\udde6-\uddff]|\ud83c[\ude01-\ude02]|\ud83c\ude1a|\ud83c\ude2f|\ud83c[\ude32-\ude3a]|\ud83c[\ude50-\ude51]|\u203c|\u2049|[\u25aa-\u25ab]|\u25b6|\u25c0|[\u25fb-\u25fe]|\u00a9|\u00ae|\u2122|\u2139|\ud83c\udc04|[\u2600-\u26FF]|\u2b05|\u2b06|\u2b07|\u2b1b|\u2b1c|\u2b50|\u2b55|\u231a|\u231b|\u2328|\u23cf|[\u23e9-\u23f3]|[\u23f8-\u23fa]|\ud83c\udccf|\u2934|\u2935|[\u2190-\u21ff])/g;
    return regex.test(text);
  };

  /** 영문/숫자 조합 여부 */
  isMultiCheck1 = (text: string) => {
    const regex = /^(?=.*[a-zA-Z])(?=.*[0-9]).{8,15}$/;
    return regex.test(text);
  };

  /** 영문/특수문자 조합 여부 */
  isMultiCheck2 = (text: string) => {
    const regex = /^(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).{8,15}$/;
    return regex.test(text);
  };

  /** 숫자/특수문자 조합 여부 */
  isMultiCheck3 = (text: string) => {
    const regex = /^(?=.*[^a-zA-Z0-9])(?=.*[0-9]).{10,12}$/;
    return regex.test(text);
  };
}

export const createStore = () => {
  return new ValidStore();
};

export type TValidStore = ReturnType<typeof createStore>;
