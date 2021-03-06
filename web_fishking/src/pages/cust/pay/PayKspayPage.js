/* global $ */
import React from "react";
import { inject, observer } from "mobx-react";

export default inject("PageStore")(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {
          sndOrdernumber: "",
          sndGoodname: "",
          sndAmount: "",
          sndOrdername: "",
          sndEmail: "",
          sndMobile: "",
          sndShowcard: "",
          sndInstallmenttype: "",
          sndInteresttype: "",
          sndReply: "",
          sndStoreid: "",
          sndPaymethod: "",
        };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      async componentDidMount() {
        const { PageStore } = this.props;
        const qp = PageStore.getQueryParams();
        const {
          orderNumber: sndOrdernumber,
          goodsName: sndGoodname,
          amount: sndAmount,
          orderName: sndOrdername,
          email: sndEmail,
          phoneNumber: sndMobile,
          showCard: sndShowcard,
          installMentType: sndInstallmenttype,
          interestType: sndInteresttype,
          reply: sndReply,
          shopNumber: sndStoreid,
          payMethod: sndPaymethod,
        } = qp;
        await this.setState({
          sndOrdernumber,
          sndGoodname,
          sndAmount,
          sndOrdername,
          sndEmail,
          sndMobile,
          sndShowcard,
          sndInstallmenttype,
          sndInteresttype,
          sndReply,
          sndStoreid,
          sndPaymethod,
        });

        document.querySelector("body").style.display = "none";
        setTimeout(() => {
          this._pay(document.KSPayWeb);
        }, 800);
        // document.querySelector("body").style.fontSize = "9pt";
        // document.querySelector("body").style.lineHeight = "100%";
        // document.querySelector("body").style.paddingLeft = ".6rem";
        // document.querySelector("body").style.paddingRight = ".6rem";
        // document.querySelector("body").style.paddingTop = "0px !important";
        // for (let ele of document.querySelectorAll("td")) {
        //   ele.style.fontSize = "9pt";
        //   ele.style.lineHeight = "100%";
        // }
        // for (let ele of document.querySelectorAll("a")) {
        //   ele.style.color = "blue";
        //   ele.style.lineHeight = "100%";
        //   ele.style.backgroundColor = "#E0EFFE";
        // }
        // for (let ele of document.querySelectorAll("input")) {
        //   ele.style.fontSize = "9pt";
        // }
        // for (let ele of document.querySelectorAll("select")) {
        //   ele.style.fontSize = "9pt";
        // }
      }

      _pay = (_frm) => {
        $('[name="sndPaymethod"]').val(this.state.sndPaymethod);
        // _frm.sndReply.value = "http://112.220.72.178:8083/payresult";
        _frm.sndReply.value = this.state.sndReply;
        var agent = navigator.userAgent;
        var midx = agent.indexOf("MSIE");
        var out_size = midx != -1 && agent.charAt(midx + 5) < "7";

        // _frm.action =
        //   "https://kspay.ksnet.to/store/KSPayMobileV1.4/KSPayPWeb.jsp";

        _frm.submit();
      };

      getLocalUrl = (mypage) => {
        const myloc = window.location.href;
        return myloc.substring(0, myloc.lastIndexOf("/")) + "/" + mypage;
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            <form
              name="KSPayWeb"
              method="post"
              action="https://kspay.ksnet.to/store/KSPayMobileV1.4/KSPayPWeb.jsp"
            >
              <table border="0" width="100%">
                <tr>
                  <td>
                    <hr noshade size="1" />
                    <b>KSPay ?????? ??????</b>
                    <hr noshade size="1" />
                  </td>
                </tr>
              </table>
              <br />
              <table border="0" width="100%">
                <tr>
                  <td align="center">
                    <table
                      width="100%"
                      cellspacing="0"
                      cellpadding="0"
                      border="0"
                      bgcolor="#4F9AFF"
                    >
                      <tr>
                        <td>
                          <table
                            width="100%"
                            cellspacing="1"
                            cellpadding="2"
                            border="0"
                          >
                            <tr bgcolor="#4F9AFF" height="25">
                              <td align="center">
                                <font color="#FFFFFF">
                                  ????????? ???????????? ??? ??????????????? ??????????????????
                                </font>
                              </td>
                            </tr>
                            <tr bgcolor="#FFFFFF">
                              <td valign="top">
                                <table
                                  width="100%"
                                  cellspacing="0"
                                  cellpadding="2"
                                  border="0"
                                >
                                  <tr>
                                    <td align="center">
                                      <br />
                                      <table>
                                        <tr>
                                          <td colspan="2">
                                            ???????????? ???????????? ????????? ?????? ?????????
                                            ??????
                                          </td>
                                        </tr>
                                        <tr>
                                          <td>???????????? : </td>
                                          <td>
                                            <select
                                              name="sndPaymethod"
                                              onChange={(e) =>
                                                this.setState({
                                                  sndPaymethod:
                                                    e.target.selectedOptions[0]
                                                      .value,
                                                })
                                              }
                                            >
                                              <option value="1000000000">
                                                ????????????
                                              </option>
                                              <option value="0100000000">
                                                ????????????
                                              </option>
                                              <option value="0010000000">
                                                ????????????
                                              </option>
                                              <option value="0000010000">
                                                ???????????????
                                              </option>
                                            </select>
                                          </td>
                                        </tr>
                                        <tr>
                                          <td>??????????????? : </td>
                                          <td>
                                            <input
                                              type="text"
                                              name="sndStoreid"
                                              size="10"
                                              maxlength="10"
                                              value={this.state.sndStoreid}
                                            />
                                          </td>
                                        </tr>
                                        <tr>
                                          <td>???????????? : </td>
                                          <td>
                                            <input
                                              type="text"
                                              name="sndCurrencytype"
                                              size="30"
                                              maxlength="3"
                                              value="WON"
                                            />
                                          </td>
                                        </tr>
                                        <tr>
                                          <td>???????????? : </td>
                                          <td>
                                            <input
                                              type="text"
                                              name="sndOrdernumber"
                                              size="30"
                                              maxlength="30"
                                              value={this.state.sndOrdernumber}
                                            />
                                          </td>
                                        </tr>
                                        <tr>
                                          <td>???????????? : </td>
                                          <td>
                                            <input
                                              type="text"
                                              name="sndAllregid"
                                              size="30"
                                              maxlength="13"
                                              value=""
                                            />
                                          </td>
                                        </tr>
                                        <tr>
                                          <td colspan="2">
                                            <hr />
                                          </td>
                                        </tr>
                                        <tr>
                                          <td colspan="2">???????????? ????????????</td>
                                        </tr>
                                        <tr>
                                          <td>??????????????? : </td>
                                          <td>
                                            <input
                                              type="text"
                                              name="sndInstallmenttype"
                                              size="30"
                                              maxlength="30"
                                              value={
                                                this.state.sndInstallmenttype
                                              }
                                            />
                                          </td>
                                        </tr>
                                        <tr>
                                          <td>??????????????? : </td>
                                          <td>
                                            <input
                                              type="text"
                                              name="sndInteresttype"
                                              size="30"
                                              maxlength="30"
                                              value={this.state.sndInteresttype}
                                            />
                                          </td>
                                        </tr>
                                        <tr>
                                          <td>???????????????????????? : </td>
                                          <td>
                                            <input
                                              type="text"
                                              name="sndShowcard"
                                              size="30"
                                              maxlength="30"
                                              value={this.state.sndShowcard}
                                            />
                                          </td>
                                        </tr>
                                        <tr>
                                          <td colspan="2">
                                            <hr />
                                          </td>
                                        </tr>
                                        <tr>
                                          <td colspan="2">
                                            ???????????? ???????????? ??????
                                          </td>
                                        </tr>
                                        <tr>
                                          <td>????????? : </td>
                                          <td>
                                            <input
                                              type="text"
                                              name="sndGoodname"
                                              size="30"
                                              maxlength="30"
                                              value={this.state.sndGoodname}
                                            />
                                          </td>
                                        </tr>
                                        <tr>
                                          <td>?????? : </td>
                                          <td>
                                            <input
                                              type="text"
                                              name="sndAmount"
                                              size="30"
                                              maxlength="9"
                                              value={this.state.sndAmount}
                                            />
                                          </td>
                                        </tr>
                                        <tr>
                                          <td>?????? : </td>
                                          <td>
                                            <input
                                              type="text"
                                              name="sndOrdername"
                                              size="30"
                                              maxlength="20"
                                              value={this.state.sndOrdername}
                                            />
                                          </td>
                                        </tr>
                                        <tr>
                                          <td>???????????? : </td>
                                          <td>
                                            <input
                                              type="text"
                                              name="sndEmail"
                                              size="30"
                                              maxlength="50"
                                              value={this.state.sndEmail}
                                            />
                                          </td>
                                        </tr>
                                        <tr>
                                          <td>???????????? : </td>
                                          <td>
                                            <input
                                              type="text"
                                              name="sndMobile"
                                              size="30"
                                              maxlength="12"
                                              value={this.state.sndMobile}
                                            />
                                          </td>
                                        </tr>

                                        <tr>
                                          <td colspan="2" align="center">
                                            <br />
                                            <input
                                              type="button"
                                              value=" ??? ??? "
                                              onClick={() =>
                                                this._pay(document.KSPayWeb)
                                              }
                                            />
                                            <br />
                                            <br />
                                          </td>
                                        </tr>
                                      </table>
                                    </td>
                                  </tr>
                                </table>
                              </td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
              <br />

              <table border="0" width="100%">
                <tr>
                  <td>
                    <hr noshade size="1" />
                  </td>
                </tr>
                <input type="hidden" name="sndCharSet" value="utf-8" />
                <input type="hidden" name="sndReply" value="" />
                <input type="hidden" name="sndEscrow" value="0" />
                <input type="hidden" name="sndVirExpDt" value="" />
                <input type="hidden" name="sndVirExpTm" value="" />
                <input
                  type="hidden"
                  name="sndStoreName"
                  value="???????????? ??????"
                />
                <input type="hidden" name="sndStoreNameEng" value="tobe" />
                <input
                  type="hidden"
                  name="sndStoreDomain"
                  value="http://www.to-be.kr/"
                />
                <input type="hidden" name="sndGoodType" value="1" />
                <input type="hidden" name="sndUseBonusPoint" value="" />
                <input type="hidden" name="sndRtApp" value="fishking://" />
                <input type="hidden" name="sndStoreCeoName" value="?????????" />
                <input type="hidden" name="sndStorePhoneNo" value="15662996" />
                <input type="hidden" name="sndStoreAddress" value="???????????? ????????? ??????????????? 1?????? 8-11" />
              </table>
            </form>
          </React.Fragment>
        );
      }
    }
  )
);
