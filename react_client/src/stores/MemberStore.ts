import { makeAutoObservable } from "mobx";
import Http from "../commons/Http";

export class MemberStore {
  constructor() {
    makeAutoObservable(this);
  }
  /********** ********** ********** ********** **********/
  /** observable **/
  /********** ********** ********** ********** **********/
  loggedIn: boolean = false; // 로그인 여부

  /********** ********** ********** ********** **********/
  /** action **/
  /********** ********** ********** ********** **********/
  // --> loggedIn 갱신
  loadLoggedIn = () => {
    if (localStorage.getItem("@accessToken") !== null) {
      this.loggedIn = true;
    } else {
      this.loggedIn = false;
    }
  };

  // --> [REST] 프로필 > 로그인
  REST_POST_login = async (memberId: string, password: string) => {
    try {
      const resolve = await Http._post("/v2/api/login", {
        memberId,
        password,
      });
      if (resolve) {
        localStorage.setItem("@accessToken", resolve);
        this.loadLoggedIn();
      }
      return resolve;
    } catch (err) {
      console.error("[REST] 프로필 > 로그인 :: " + JSON.stringify(err));
      return null;
    }
  };

  // --> [REST] 프로필 > 프로필 관리 페이지 조회
  REST_GET_profileManage = async () => {
    try {
      const resolve = await Http._get("/v2/api/profileManage");
      return resolve;
    } catch (err) {
      console.error(
        "[REST] 프로필 > 프로필 관리 페이지 조회 :: " + JSON.stringify(err)
      );
      return null;
    }
  };

  // --> [PUT] 프로필 > 사용자 프로필사진 변경
  REST_PUT_profileImage = async (profileImage: any) => {
    try {
      const form = new FormData();
      form.append("profileImage", profileImage);

      const resolve = await Http._put_upload(
        "/v2/api/profileManage/profileImage",
        form
      );

      return resolve;
    } catch (err) {
      console.error(
        "[REST] 프로필 > 사용자 프로필사진 변경 :: " + JSON.stringify(err)
      );
      return null;
    }
  };

  // --> [PUT] 프로필 > 닉네임 변경
  REST_PUT_profileManage_nickName = async (nickName: string) => {
    try {
      const resolve = await Http._put("/v2/api/profileManage/nickName", {
        nickName,
      });
      return resolve;
    } catch (err) {
      console.error("[REST] 프로필 > 닉네임 변경 :: " + JSON.stringify(err));
      return null;
    }
  };

  // --> [PUT] 프로필 > 상태메세지 변경
  REST_PUT_profileManage_statusMessage = async (statusMessage: string) => {
    try {
      const resolve = await Http._put("/v2/api/profileManage/statusMessage", {
        statusMessage,
      });
      return resolve;
    } catch (err) {
      console.error(
        "[REST] 프로필 > 상태메세지 변경 :: " + JSON.stringify(err)
      );
      return null;
    }
  };

  // --> [PUT] 프로필 > 이메일 변경
  REST_PUT_profileManage_email = async (email: string) => {
    try {
      const resolve = await Http._put("/v2/api/profileManage/email", { email });
      return resolve;
    } catch (err) {
      console.error("[REST] 프로필 > 이메일 변경 :: " + JSON.stringify(err));
      return null;
    }
  };

  // --> [PUT] 프로필 > 비번 변경
  REST_PUT_profileManage_password = async (
    currentPw: string,
    newPw: string
  ) => {
    try {
      const resolve = await Http._put("/v2/api/profileManage/password", {
        currentPw,
        newPw,
      });
      return resolve;
    } catch (err) {
      console.error("[REST] 프로필 > 비번 변경 :: " + JSON.stringify(err));
      return null;
    }
  };

  // --> [DELETE] 프로필 > 탈퇴하기
  REST_DELETE_profileManage_delete = async () => {
    try {
      const resolve = await Http._delete("/v2/api/profileManage/delete");
      if (resolve) {
        localStorage.removeItem("@accessToken");
      }
      return resolve;
    } catch (err) {
      console.error("[REST] 프로필 > 탈퇴하기 :: " + JSON.stringify(err));
      return null;
    }
  };
}

export const createStore = () => {
  return new MemberStore();
};

export type TMemberStore = ReturnType<typeof createStore>;
