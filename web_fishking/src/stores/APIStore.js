import { makeAutoObservable, runInAction } from "mobx";
import imageCompression from "browser-image-compression";
import Http from "../Http";
import PageStore from "./PageStore";
import ModalStore from "./ModalStore";

const APIStore = new (class {
  constructor(props) {
    makeAutoObservable(this);
  }
  /********** ********** ********** ********** **********/
  /** observable */
  /********** ********** ********** ********** **********/
  isLoading = false;
  /********** ********** ********** ********** **********/
  /** action */
  /********** ********** ********** ********** **********/
  setLoading = (isLoading) => {
    this.isLoading = false;
  };
  _get = (url, params, headers) => {
    return new Promise((resolve, reject) => {
      runInAction(async () => {
        // this.isLoading = true;
        try {
          if (params) {
            const keys = Object.keys(params);
            url += "?";
            for (let key of keys) {
              if (params[key] === null) continue;

              if (params[key] instanceof Array) {
                for (let p of params[key]) {
                  url += `${encodeURI(key.concat("[]"))}=${encodeURI(p)}&`;
                }
              } else {
                url += `${encodeURI(key)}=${encodeURI(params[key])}&`;
              }
            }
          }
          const response = await Http.request(
            url,
            "GET",
            headers,
            // params,
            null
          );
          this.isLoading = false;
          resolve(response);
        } catch (err) {
          this.isLoading = false;
          reject(err);
        }
      });
    });
  };

  _delete = (url, params, headers) => {
    return new Promise((resolve, reject) => {
      if ((localStorage.getItem("@accessToken") || null) === null) {
        ModalStore.openModal("Alert", { body: "로그인이 필요합니다." });
        reject("NOT FOUND ACCESS TOKEN");
      } else {
        runInAction(async () => {
          // this.isLoading = true;
          try {
            const response = await Http.request(
              url,
              "DELETE",
              headers,
              null,
              params
            );
            this.isLoading = false;
            resolve(response);
          } catch (err) {
            this.isLoading = false;
            reject(err);
          }
        });
      }
    });
  };

  _post = (url, data, headers) => {
    return new Promise((resolve, reject) => {
      if (
        url.indexOf("/login") === -1 && url.indexOf('/findPw') === -1 && url.indexOf('/checkSmsAuth') === -1 &&
        (localStorage.getItem("@accessToken") || null) === null
      ) {
        ModalStore.openModal("Alert", { body: "로그인이 필요합니다." });
        reject("NOT FOUND ACCESS TOKEN");
      } else {
        runInAction(async () => {
          // this.isLoading = true;
          try {
            const response = await Http.request(
              url,
              "POST",
              headers,
              null,
              data
            );
            this.isLoading = false;
            resolve(response);
          } catch (err) {
            this.isLoading = false;
            reject(err);
          }
        });
      }
    });
  };

  _put = (url, data, headers) => {
    return new Promise((resolve, reject) => {
      if (url.indexOf('/findPw') === -1 && (localStorage.getItem("@accessToken") || null) === null) {
        ModalStore.openModal("Alert", { body: "로그인이 필요합니다." });
        reject("NOT FOUND ACCESS TOKEN");
      } else {
        runInAction(async () => {
          // this.isLoading = true;
          try {
            const response = await Http.request(
              url,
              "PUT",
              headers,
              null,
              data
            );
            this.isLoading = false;
            resolve(response);
          } catch (err) {
            this.isLoading = false;
            reject(err);
          }
        });
      }
    });
  };

  _put_upload = (url, form, headers) => {
    return new Promise((resolve, reject) => {
      runInAction(async () => {
        const file = form.get("file");
        // const compressedFile = await imageCompression(file, { maxSizeMB: 4 });
        form.set("file", file);
        // this.isLoading = true;
        try {
          const response = await Http.upload(url, "PUT", headers, form);
          this.isLoading = false;
          resolve(response);
        } catch (err) {
          this.isLoading = false;
          reject(err);
        }
      });
    });
  };

  _post_upload = (url, form, headers) => {
    return new Promise((resolve, reject) => {
      runInAction(async () => {
        const file = form.get("file");
        // const compressedFile = await imageCompression(file, { maxSizeMB: 4 });
        form.set("file", file);
        // this.isLoading = true;
        try {
          const response = await Http.upload(url, "POST", headers, form);
          this.isLoading = false;
          resolve(response);
        } catch (err) {
          this.isLoading = false;
          reject(err);
        }
      });
    });
  };
})();

export default APIStore;
