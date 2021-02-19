import React from "react";
import { BrowserRouter, Route } from "react-router-dom";
import crypto from "crypto";
import Routers from "./routes";

Date.prototype.format = function (join = "-") {
  const year = this.getFullYear();
  const month =
    this.getMonth() + 1 < 10
      ? "0".concat(this.getMonth() + 1)
      : this.getMonth() + 1;
  const date =
    this.getDate() + 1 < 10 ? "0".concat(this.getDate()) : this.getDate();
  return year + join + month + join + date;
};
Date.prototype.toString = function () {
  const year = this.getFullYear();
  const month =
    this.getMonth() + 1 < 10
      ? "0".concat(this.getMonth() + 1)
      : this.getMonth() + 1;
  const date =
    this.getDate() + 1 < 10 ? "0".concat(this.getDate()) : this.getDate();
  let week = this.getDay();
  switch (week) {
    case 0:
      week = "일";
      break;
    case 1:
      week = "월";
      break;
    case 2:
      week = "화";
      break;
    case 3:
      week = "수";
      break;
    case 4:
      week = "목";
      break;
    case 5:
      week = "금";
      break;
    case 6:
      week = "토";
      break;
  }

  return `${year}년 ${month}월 ${date}일 (${week})`;
};
// # >>>>> 문자열 암호화
String.prototype.encrypt = function () {
  const ciphers = crypto.createCipheriv(
    "aes-128-cbc",
    process.env.REACT_APP_ENC_KEY.substr(0, 16),
    process.env.REACT_APP_ENC_KEY.substr(0, 16)
  );
  let result = ciphers.update(this, "utf8", "base64");
  result += ciphers.final("base64");
  return result;
};
// # >>>>> 문자열 복호화
String.prototype.decrypt = function () {
  console.log("before -> " + this);
  const ciphers = crypto.createDecipheriv(
    "aes-128-cbc",
    process.env.REACT_APP_ENC_KEY.substr(0, 16),
    process.env.REACT_APP_ENC_KEY.substr(0, 16)
  );
  let result = ciphers.update(this, "base64", "utf8");
  result += ciphers.final("utf8");
  return result;
};
// # >>>>> HHmm => 오전|오후 0시 0분
String.prototype.formatTime01 = function () {
  if (this.length === 0) return "";
  let hour = this.substr(0, 2);
  let min = this.substr(2, 2);
  let ampm = "";
  if (hour < 12) ampm = "오전";
  else {
    ampm = "오후";
    hour = hour - 12;
  }
  return `${ampm} ${hour}시 ${min}분`;
};
String.prototype.maskEmail = function () {
  const index = this.indexOf("@");
  return this.substr(0, 3)
    .concat("****")
    .concat(this.substr(index, this.length));
};
// yyyyMMdd => (week)
String.prototype.getWeek = function () {
  try {
    const year = this.substr(0, 4);
    const month = this.substr(4, 2);
    const date = this.substr(6, 2);
    const d = new Date(year, new Number(month) - 1, date);

    switch (d.getDay()) {
      case 0:
        return "일";
      case 1:
        return "월";
      case 2:
        return "화";
      case 3:
        return "수";
      case 4:
        return "목";
      case 5:
        return "금";
      case 6:
        return "토";
    }
  } catch (err) {
    return "";
  }
};
//
String.prototype.betweenTime = function () {
  try {
    const second = 1000;
    const minute = second * 60;
    const hour = minute * 60;

    const when = new Date(this);
    const when_year = when.getFullYear();
    const when_month =
      when.getMonth() + 1 < 10
        ? "0".concat(when.getMonth() + 1)
        : when.getMonth() + 1;
    const when_date =
      when.getDate() < 10 ? "0".concat(when.getDate()) : when.getDate();

    const prev = when.getTime();
    const now = new Date().getTime();
    return now - prev;
  } catch (err) {
    return "";
  }
};
//
String.prototype.dday = function () {
  try {
    const year = this.substr(0, 4);
    const month = this.substr(4, 2);
    const date = this.substr(6, 2);

    const dday = new Date(year, new Number(month) - 1, date);
    const now = new Date();

    const between = new Date().getTime() - dday.getTime();
    return Math.floor(between / (1000 * 60 * 60 * 24)) * -1;
  } catch (err) {
    return "";
  }
};
//
String.prototype.latest = function () {
  try {
    const second = 1000;
    const minute = second * 60;
    const hour = minute * 60;

    const when = new Date(this);
    const when_year = when.getFullYear();
    const when_month =
      when.getMonth() + 1 < 10
        ? "0".concat(when.getMonth() + 1)
        : when.getMonth() + 1;
    const when_date =
      when.getDate() < 10 ? "0".concat(when.getDate()) : when.getDate();

    const prev = when.getTime();
    const now = new Date().getTime();
    const between = now - prev;

    if (between > hour * 2) {
      // 2시간 ~ :: 기준 날짜
      return when_year + "." + when_month + "." + when_date + "";
    } else if (between <= hour * 2 && between >= hour) {
      // 1시간 ~ 2시간 :: 1시간전
      return "1시간 전";
    } else if (between < minute) {
      // ~ 1분 :: 방금 전
      return "방금 전";
    } else if (between < hour) {
      // ~ 1시간 :: n분 전
      return Math.round(between / minute) + "분 전";
    } else {
      return "";
    }
  } catch (err) {
    return "";
  }
};

const App = () => {
  return (
    <BrowserRouter>
      <Route path={`/*`} component={Routers} />
    </BrowserRouter>
  );
};

export default App;
