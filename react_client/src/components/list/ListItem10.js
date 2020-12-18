/* eslint-disable jsx-a11y/anchor-is-valid */
import React from "react";
import { inject, observer } from "mobx-react";
import { withRouter } from "react-router";

export default inject()(
  observer(
    withRouter(
      ({
        data: {
          text,
          content,
          contentChild,
          hasNext = true,
          navigateTo,
          history,
        },
      }) => {
        return (
          <>
            <a onClick={() => (navigateTo ? history.push(navigateTo) : null)}>
              <div className="row no-gutters align-items-center">
                <div className="col-3 pl-2">{text}</div>
                <div className="col-8 text-right">
                  {contentChild && (
                    <React.Fragment>{[contentChild]}</React.Fragment>
                  )}
                  {content && <React.Fragment>{content}</React.Fragment>}
                </div>
                <div className="col-1 text-right pl-1">
                  {hasNext && (
                    <img src="/assets/img/svg/cal-arrow-right.svg" alt="" />
                  )}
                </div>
              </div>
            </a>
            <hr className="full mt-3 mb-3" />
          </>
        );
      }
    )
  )
);
