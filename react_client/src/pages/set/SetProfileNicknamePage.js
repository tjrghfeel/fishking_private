import React, { useCallback, useRef, useState } from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layout/Navigation";

export default inject()(
  observer(() => {
    const input = useRef(null);
    const [text, setText] = useState("");
    /** 변경하기 */
    const submit = useCallback(() => {
      if (text.length === 0) {
        input.current?.classList.add("is-invalid");
        return;
      } else {
        input.current?.classList.remove("is-invalid");
      }
      console.log(text);
    }, [input, text]);
    return (
      <>
        {/** Navigation */}
        <Navigation title={"닉네임 변경"} visibleBackIcon={true} />

        {/** 입력 */}
        <div className="container nopadding mt-4">
          <div className="row no-gutters align-items-center">
            <div className="col-9 pl-2">사용할 닉네임을 입력해주세요.</div>
            <div className="col-3 text-right pr-2">
              <small className="grey">{text.length} / 7</small>
            </div>
          </div>
          <div className="card-round-box-grey mt-3">
            <form className="form-line mt-3">
              <div className="form-group">
                <label htmlFor="inputNickname" className="sr-only">
                  닉네임
                </label>
                <input
                  ref={input}
                  type="text"
                  className="form-control"
                  id="inputNickname"
                  placeholder="닉네임"
                  value={text}
                  onChange={(e) => setText(e.target.value.substr(0, 7))}
                />
              </div>
            </form>
          </div>
          <a onClick={submit} className="btn btn-primary btn-lg btn-block">
            변경하기
          </a>
        </div>
      </>
    );
  })
);
