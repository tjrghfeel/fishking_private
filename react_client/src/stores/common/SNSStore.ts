import { makeAutoObservable } from "mobx";

declare global {
  let Kakao: any;
  let FB: any;
  let AppleID: any;
}

export class SNSStore {
  constructor() {
    makeAutoObservable(this);
  }
  /** Kakao 로그인 요청 */
  authorizeKakao() {
    window.location.href =
      "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" +
      process.env["REACT_APP_KAKAO_JAVASCRIPT_KEY"] +
      "&redirect_uri=" +
      process.env["REACT_APP_KAKAO_REDIRECT_URI"];
  }

  /** 페이스북 로그인 요청 */
  authorizeFacebook() {
    window.location.href =
      "https://www.facebook.com/v9.0/dialog/oauth?client_id=" +
      process.env["REACT_APP_FACEBOOK_APP_ID"] +
      "&redirect_uri=" +
      process.env["REACT_APP_FACEBOOK_REDIRECT_URI"] +
      "&state=";
  }

  /** 네이버 로그인 요청 */
  authorizeNaver() {
    window.location.href =
      "https://nid.naver.com/oauth2.0/authorize?client_id=" +
      process.env["REACT_APP_NAVER_CLIENT_ID"] +
      "&response_type=code&redirect_uri=" +
      process.env["REACT_APP_NAVER_REDIRECT_URI"] +
      "&state=";
  }

  /** 애플 로그인 요청 */
  authorizeApple() {
    AppleID.auth.signIn();
  }
}

export const createStore = () => {
  return new SNSStore();
};

export type TSNSStore = ReturnType<typeof createStore>;
