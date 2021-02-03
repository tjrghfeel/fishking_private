import { makeAutoObservable, runInAction } from "mobx";
import Http from "../Http";
import PageStore from "./PageStore";

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
  _get = (url, params, headers) => {
    return new Promise((resolve, reject) => {
      runInAction(async () => {
        this.isLoading = true;
        try {
          if (params) {
            const keys = Object.keys(params);
            for (let key of keys) {
              if (params[key] instanceof Array) {
              }
            }
          }

          const response = await Http.request(
            url,
            "GET",
            headers,
            params,
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
      runInAction(async () => {
        this.isLoading = true;
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
    });
  };

  _post = (url, data, headers) => {
    return new Promise((resolve, reject) => {
      runInAction(async () => {
        this.isLoading = true;
        try {
          const response = await Http.request(url, "POST", headers, null, data);
          this.isLoading = false;
          resolve(response);
        } catch (err) {
          this.isLoading = false;
          reject(err);
        }
      });
    });
  };

  _put = (url, data, headers) => {
    return new Promise((resolve, reject) => {
      runInAction(async () => {
        this.isLoading = true;
        try {
          const response = await Http.request(url, "PUT", headers, null, data);
          this.isLoading = false;
          resolve(response);
        } catch (err) {
          this.isLoading = false;
          reject(err);
        }
      });
    });
  };

  _put_upload = (url, form, headers) => {
    return new Promise((resolve, reject) => {
      runInAction(async () => {
        this.isLoading = true;
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
        this.isLoading = true;
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