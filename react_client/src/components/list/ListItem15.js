/* eslint-disable jsx-a11y/anchor-is-valid */
import React, { useCallback, useRef } from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(({ data: { imgSrc } }) => {
    const ref = useRef(null);
    const remove = useCallback(() => {
      ref.current?.remove();
    }, [ref]);
    return (
      <div ref={ref} className="col-3">
        <div className="box-round-grey">
          <a onClick={remove} className="del">
            <img src="/assets/img/svg/icon_close_white.svg" alt="" />
          </a>
          <img src={imgSrc} className="d-block w-100 photo-img" alt="" />
        </div>
      </div>
    );
  })
);
