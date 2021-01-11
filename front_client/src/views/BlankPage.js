/* global daum */
import React, { useEffect } from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(() => {
    useEffect(() => {
      // var container = document.getElementById("map");
      // var options = {
      //   center: new daum.maps.LatLng(36.252932, 127.724734),
      //   level: 7,
      // };
      // var map = new daum.maps.Map(container, options);
    });
    return (
      <div id={"map"} style={{ width: "100%", height: "100vh" }}>
        Blank Page
      </div>
    );
  })
);
