import axios from "axios";
import ModalStore from "./stores/ModalStore";
import React from "react";

/** 공통 설정 */
const http = axios.create({
  baseURL: process.env.REACT_APP_HTTP_BASE_URL,
  // baseURL: 'http://127.0.0.1:8083',
});

http.defaults.headers.common["Authorization"] =
  localStorage.getItem("@accessToken") || "";
http.defaults.headers.common["Accept"] = "application/json";
http.defaults.timeout = 10000;

export default (() => {
  const request = (url, method, headers = {}, params, data) => {
    return new Promise((resolve, reject) => {
      const startTime = new Date().getTime();
      if (process.env.NODE_ENV === "development") {
        if (params) {
          console.log(`PARAMS :\n${JSON.stringify(params)}`);
        }
        if (data) {
          console.log(`DATA :\n${JSON.stringify(data)}`);
        }
      }
      http
        .request({
          url,
          method,
          headers: {
            ...headers,
            Authorization: localStorage.getItem("@accessToken") || "",
          },
          params,
          data,
        })
        .then((response, xhr) => {
          console.log(
            `RESPONSE : ${method} : ${Intl.NumberFormat().format(
              new Date().getTime() - startTime
            )}ms\n${url}`
          );
          if (response && response["status"] == "204") {
            // ModalStore.openModal("Alert", {
            //   body: "요청하신 검색 결과가 없습니다.",
            // });
            resolve(null);
          } else if (response && response["status"] == "206") {
            ModalStore.openModal("Alert", {
              body: (
                <React.Fragment>
                  <p>
                    선택하신 알람시간에 해당하는
                    <br />
                    조위 정보가 없습니다.
                  </p>
                </React.Fragment>
              ),
            });
            resolve(null);
          } else {
            resolve(response.data);
          }
        })
        .catch((err) => {
          console.log(
            `${method} : ${Intl.NumberFormat().format(
              new Date().getTime() - startTime
            )}ms\n${url}`
          );
          if (err.message?.indexOf("500") !== -1) {
            ModalStore.openModal("Alert", {
              body: "데이터 수신 중입니다.",
            });
          }
          if (err.message?.indexOf("403") !== -1) {
            ModalStore.openModal("Alert", {
              body: "권한이 없습니다.",
            });
          }
          console.error(`message:${err.message}`);
          console.error(`stack:${err.stack}`);
          reject(err);
        });
    });
  };

  const upload = (url, method, headers = {}, form) => {
    return new Promise((resolve, reject) => {
      http
        .request({
          url,
          method,
          headers: {
            ...headers,
            "Content-Type": "multipart/form-data",
            Authorization: localStorage.getItem("@accessToken") || null,
          },
          data: form,
        })
        .then((response, xhr) => {
          resolve(response.data);
        })
        .catch((err) => {
          reject(err);
        });
    });
  };

  const _get = (url, params, headers) => {
    return new Promise(async (resolve, reject) => {
      try {
        const response = await request(url, "GET", headers, params, null);
        resolve(response);
      } catch (err) {
        reject(err);
      }
    });
  };

  const _delete = (url, params, headers) => {
    return new Promise(async (resolve, reject) => {
      try {
        const response = await request(url, "DELETE", headers, null, params);
        resolve(response);
      } catch (err) {
        reject(err);
      }
    });
  };

  const _post = (url, data, headers) => {
    return new Promise(async (resolve, reject) => {
      try {
        const response = await request(url, "POST", headers, null, data);
        resolve(response);
      } catch (err) {
        reject(err);
      }
    });
  };

  const _put = (url, data, headers) => {
    return new Promise(async (resolve, reject) => {
      try {
        const response = await request(url, "PUT", headers, null, data);
        resolve(response);
      } catch (err) {
        reject(err);
      }
    });
  };

  const _put_upload = (url, form, headers) => {
    return new Promise(async (resolve, reject) => {
      try {
        const response = await upload(url, "PUT", headers, form);
        resolve(response);
      } catch (err) {
        reject(err);
      }
    });
  };

  const _post_upload = (url, form, headers) => {
    return new Promise(async (resolve, reject) => {
      try {
        const response = await upload(url, "POST", headers, form);
        resolve(response);
      } catch (err) {
        reject(err);
      }
    });
  };

  return {
    request,
    upload,
    _get,
    _delete,
    _post,
    _put,
    _put_upload,
    _post_upload,
  };
})();
