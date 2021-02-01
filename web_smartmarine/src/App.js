import React from "react";
import { BrowserRouter, Route } from "react-router-dom";
import Routers from "./routes";

//
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
