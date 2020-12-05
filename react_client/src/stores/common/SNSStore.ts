import { makeAutoObservable } from "mobx";

declare global {
  let Kakao: any;
}

export class SNSStore {
  constructor() {
    makeAutoObservable(this);
  }
  /** 카카오 로그인 요청 */
  kakaoLogin() {
    Kakao.Auth.login({
      persistAccessToken: false,
      throughTalk: false,
      success: (response: any) => {
        console.log(JSON.stringify(response));
      },
      fail: (error: any) => {
        console.log(error);
      },
    });
  }
}

export const createStore = () => {
  return new SNSStore();
};

export type TSNSStore = ReturnType<typeof createStore>;
