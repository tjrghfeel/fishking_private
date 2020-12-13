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
        <Navigation title={"이메일 입력"} visibleBackIcon={true} />

        {/** 입력 */}
        <div className="container nopadding mt-4">
          <div className="row no-gutters align-items-center">
            <div className="col-12 pl-2">
              등록할 이메일을 입력해 주세요.
              <br />
              등록하신 이메일로 다양한 소식을 받아보실 수 있습니다.
            </div>
          </div>
          <div className="card-round-box-grey mt-3">
            <form className="form-line mt-3">
              <div className="form-group">
                <label htmlFor="input" className="sr-only">
                  이메일
                </label>
                <input
                  ref={input}
                  type="email"
                  className="form-control"
                  id="input"
                  placeholder="이메일"
                  value={text}
                  onChange={(e) => setText(e.target.value)}
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
