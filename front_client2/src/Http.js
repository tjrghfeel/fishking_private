import axios from "axios";

/** 공통 설정 */
const http = axios.create({
  baseURL: process.env.REACT_APP_HTTP_BASE_URL,
});

http.defaults.headers.common["Authorization"] =
  localStorage.getItem("@accessToken") || null;
http.defaults.headers.common["Accept"] = "application/json";

export default (() => {
  const request = (url, method, headers = {}, params, data) => {
    return new Promise((resolve, reject) => {
      http
        .request({
          url,
          method,
          headers: {
            ...headers,
            Authorization: localStorage.getItem("@accessToken") || null,
          },
          params,
          data,
        })
        .then((response, xhr) => {
          resolve(response.data);
        })
        .catch((err) => {
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
    _get,
    _delete,
    _post,
    _put,
    _put_upload,
    _post_upload,
  };
})();
