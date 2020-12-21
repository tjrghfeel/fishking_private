import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layouts/Navigation";
import Http from "../../Http";

export default inject("DataStore")(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.text = React.createRef(null);
        this.state = {
          text: "",
        };
      }
      /********** ********** ********** ********** **********/
      /** functions */
      /********** ********** ********** ********** **********/
      async componentDidMount() {
        const resolve = await Http._get("/v2/api/profileManage");
        this.setState({ text: resolve.email });
      }
      requestUpdate = async () => {
        const { text: email } = this.state;
        const {
          DataStore: { isValidEmail },
          history,
        } = this.props;

        if (email === "" || !isValidEmail(email)) {
          this.text.current?.classList.add("is-invalid");
          return;
        } else {
          this.text.current?.classList.remove("is-invalid");
        }

        const resolve = await Http._put("/v2/api/profileManage/email", {
          email,
        });
        if (resolve) {
          history.goBack();
        }
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <>
            {/** Navigation */}
            <Navigation title={"이메일 입력"} showBack={true} />

            {/** 입력 */}
            <div className="container nopadding mt-4">
              <div className="row no-gutters align-items-center">
                <div className="col-12 pl-2">
                  등록할 이메일을 입력해 주세요. <br />
                  등록하신 이메일로 다양한 소식을 받아보실 수 있습니다.
                </div>
              </div>
              <div className="card-round-box-grey mt-3">
                <form className="form-line mt-3">
                  <div className="form-group">
                    <label htmlFor="inputNickname" className="sr-only">
                      이메일
                    </label>
                    <input
                      ref={this.text}
                      type="text"
                      className="form-control"
                      placeholder="이메일"
                      value={this.state.text}
                      onChange={(e) => this.setState({ text: e.target.value })}
                    />
                  </div>
                </form>
              </div>
              <a
                onClick={this.requestUpdate}
                className="btn btn-primary btn-lg btn-block"
              >
                등록하기
              </a>
            </div>
          </>
        );
      }
    }
  )
);
