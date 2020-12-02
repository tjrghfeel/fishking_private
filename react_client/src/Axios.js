import axios from "axios";

/** 공통 설정 */
const http = axios.create({
  baseURL: "http://112.220.72.178:6081/fishkingV2/v1/api/",
});

http.defaults.headers.common["Authorization"] = null;
http.defaults.headers.common["Accept"] = "application/json";

export default (() => {
  const get = (url, params, onSuccess, onFail, onComplete) => {
    http
      .request({
        url,
        method: "GET",
        params,
      })
      .then(async (response) => {
        if (onSuccess) {
          await onSuccess(response.data);
        }
        if (onComplete) {
          onComplete(response.data);
        }
      })
      .catch(async (err) => {
        if (onFail) {
          await onFail(err);
        }
        if (onComplete) {
          onComplete(err);
        }
      });
  };

  return { get: get };
})();
