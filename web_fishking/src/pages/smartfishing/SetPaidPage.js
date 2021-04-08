import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout },
} = Components;

export default inject(
  "PageStore",
  "APIStore",
  "DataStore",
  "ModalStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {
          bankCode: null,
          bank: null,
          accountNum: null,
          name: null,
          arr_bank: [],
        };
        this.selBank = React.createRef(null);
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        this.loadPageData();
      }

      loadPageData = async () => {
        const { DataStore, APIStore } = this.props;
        const resolve = await APIStore._get(`/v2/api/fishing/account`);
        console.log(JSON.stringify(resolve));
        const arr_bank = await DataStore.getCodes("164");
        this.setState({ ...resolve, arr_bank });
        this.selBank.current.value = resolve["bankCode"];
      };
      submit = async () => {
        const { APIStore, ModalStore } = this.props;
        const { name, bankCode, accountNum } = this.state;
        const resolve = await APIStore._put(`/v2/api/fishing/account/update`, {
          name,
          bankCode,
          accountNum,
        });
        if (resolve && resolve["status"] === "success") {
          ModalStore.openModal("Alert", { body: "저장되었습니다." });
          this.loadPageData();
        } else {
          ModalStore.openModal("Alert", { body: resolve["message"] });
        }
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            <NavigationLayout title={"정산 계좌 설정"} showBackIcon={true} />

            <div className="container nopadding mt-4">
              <div className="row no-gutters align-items-center">
                <div className="col-12 pl-2">정산 계좌 정보를 입력합니다.</div>
              </div>
              <div className="card-round-box-grey mt-3">
                <form className="form-line mt-3">
                  <div className="form-group">
                    <select
                      ref={this.selBank}
                      className="form-control"
                      onChange={(e) =>
                        this.setState({
                          bankCode: e.target.selectedOptions[0].value,
                          bank: e.target.selectedOptions[0].text,
                        })
                      }
                    >
                      <option value={""}>은행을 선택해주세요.</option>
                      {this.state.arr_bank.map((data, index) => (
                        <option key={index} value={data["code"]}>
                          {data["codeName"]}
                        </option>
                      ))}
                    </select>
                  </div>
                  <div className="form-group">
                    <input
                      type="text"
                      className="form-control"
                      id="inputPassword"
                      placeholder="계좌번호를 입력해 주세요."
                      value={this.state.accountNum}
                      onChange={(e) =>
                        this.setState({ accountNum: e.target.value })
                      }
                    />
                  </div>
                  <div className="form-group">
                    <input
                      type="text"
                      className="form-control"
                      id="inputPasswordC"
                      placeholder="예금주 명을 입력해 주세요."
                      value={this.state.name}
                      onChange={(e) => this.setState({ name: e.target.value })}
                    />
                  </div>
                </form>
              </div>
              <a
                className="btn btn-primary btn-lg btn-block"
                onClick={this.submit}
              >
                확인
              </a>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
