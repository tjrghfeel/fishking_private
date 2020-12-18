/* eslint-disable jsx-a11y/anchor-is-valid */
import React, { useCallback } from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(({ data: { sectionTitle, imgSrc, title, content, time } }) => {
    const ref1 = React.useRef(null);
    const remove = useCallback(() => {
      ref1.current.remove();
    }, [ref1]);
    return (
      <>
        {sectionTitle && <h5 className="text-center">{sectionTitle}</h5>}
        <div ref={ref1} className="card-round-box-grey mt-3 pt-3 pb-3">
          <div className="row no-gutters">
            <div className="col-2 text-center">
              <a className="btn btn-circle pt-0">
                <img src={imgSrc} alt="" className="icon-md" />
              </a>
            </div>
            <div className="col-9">
              <strong className="blue">[{title}]</strong> {content}
              <br />
              <small className="grey">{time}</small>
            </div>
            <div className="col-1 text-right">
              <a onClick={remove}>
                <img src="/assets/img/svg/icon_close_grey.svg" alt="" />
              </a>
            </div>
          </div>
        </div>
      </>
    );
  })
);
