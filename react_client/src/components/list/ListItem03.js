import React from "react";
import { inject, observer } from "mobx-react";

// 메인 > 홈 > 비주얼 캐러셀 이미지 아이템

export default inject()(
  observer(({ data: { imgSrc } }) => {
    return <img src={imgSrc} className="d-block w-100" alt="" />;
  })
);
