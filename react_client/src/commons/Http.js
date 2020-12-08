import axios from "axios";

/** 공통 설정 */
const http = axios.create({
  baseURL: process.env.REACT_APP_HTTP_BASE_URL,
});

http.defaults.headers.common["Authorization"] = null;
http.defaults.headers.common["Accept"] = "application/json";

export default (() => {
  const request = (
    method,
    url,
    headers,
    params,
    onSuccess,
    onFail,
    onComplete
  ) => {
    http
      .request({
        url,
        method,
        headers,
        params: method === "GET" || method === "DELETE" ? params : null,
        data: method === "POST" || method === "PUT" ? params : null,
      })
      .then(async (response) => {
        if (onSuccess) await onSuccess(response.data);
        if (onComplete) onComplete(response.data);
      })
      .catch(async (err) => {
        if (onFail) await onFail(err);
        if (onComplete) onComplete(err);
      });
  };

  return { request: request };
})();
