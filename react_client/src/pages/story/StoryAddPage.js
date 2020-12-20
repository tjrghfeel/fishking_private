/* eslint-disable jsx-a11y/anchor-is-valid */
import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layout/Navigation";
import SelectFishModal from "../../components/modal/SelectFishModal";
import SelectDateModal from "../../components/modal/SelectDateModal";
import SelectTideModal from "../../components/modal/SelectTideModal";
import SelectLureModal from "../../components/modal/SelectLureModal";
import SelectTechnicModal from "../../components/modal/SelectTechnicModal";
import SelectPlaceModal from "../../components/modal/SelectPlaceModal";
import SearchLocationModal from "../../components/modal/SearchLocationModal";
import Clearfix from "../../components/layout/Clearfix";
import ListItem15 from "../../components/list/ListItem15";

export default inject(
  "ValidStore",
  "CodeStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.category = React.createRef(null);
        this.title = React.createRef(null);
        this.upload = React.createRef(null);
        this.state = {
          categoryOptions: [], // 카테고리 옵션
          category: "", // 카테고리
          title: "", // 제목
          fish: [], // 어종
          date: null, // 날짜
          tide: [], // 물때
          technic: [], // 낚시기법
          lure: [], // 미끼
          place: [], // 낚시 장소
          images: [
            { imgSrc: "/assets/img/sample/photo1.jpg" },
            { imgSrc: "/assets/img/sample/photo2.jpg" },
            { imgSrc: "/assets/img/sample/photo3.jpg" },
            { imgSrc: "/assets/img/sample/photo1.jpg" },
            { imgSrc: "/assets/img/sample/photo2.jpg" },
            { imgSrc: "/assets/img/sample/photo3.jpg" },
          ], // 이미지 목록
        };
      }
      /********** ********** ********** ********** **********/
      /** functions **/
      /********** ********** ********** ********** **********/
      async componentDidMount() {
        const {
          CodeStore: { REST_GET_commonCode_value },
        } = this.props;
        const resolve = await REST_GET_commonCode_value("postTitle");
        this.setState({ categoryOptions: resolve });
      }

      onClickUpload = () => {
        this.upload.current?.click();
      };
      onChangeUpload = async () => {
        if (this.upload.current?.files.length > 0) {
        }
      };

      /********** ********** ********** ********** **********/
      /** render **/
      /********** ********** ********** ********** **********/
      render() {
        const {
          ValidStore: { getDateString },
        } = this.props;
        return (
          <>
            {/** Navigation */}
            <Navigation title={"글쓰기"} visibleBackIcon={true} />

            {/** 입력 > 카테고리 & 제목 */}
            <div className="container nopadding">
              <div className="card">
                <form className="form-line mt-3">
                  <div className="form-group">
                    <select
                      ref={this.category}
                      className="form-control"
                      value={this.state.category}
                      onChange={(e) =>
                        this.setState({ category: e.target.value })
                      }
                    >
                      <option value={""}>카테고리를 선택해주세요.</option>
                      {this.state.categoryOptions.map((data, index) => (
                        <option key={index} value={data.key}>
                          {data.value}
                        </option>
                      ))}
                    </select>
                  </div>
                  <div className="form-group">
                    <input
                      ref={this.title}
                      type="text"
                      className="form-control"
                      placeholder="제목을 입력해 주세요. (30자 이하) "
                      value={this.state.title}
                      onChange={(e) =>
                        this.setState({ title: e.target.value.substr(0, 30) })
                      }
                    />
                  </div>
                </form>
              </div>
            </div>

            {/** 입력 > 어종 & 날짜 & 물때 & 낚시 기법 & 미끼 & 낚시장소 */}
            <div className="container nopadding">
              <div className="card-box-grey">
                <dl className="dl-horizontal-round dl-line">
                  <dt>
                    어종 <span className="red">*</span>
                  </dt>
                  <a
                    href="#none"
                    data-toggle="modal"
                    data-target="#selFishModal"
                  >
                    <dd>
                      {this.state.fish.map((data, index) => (
                        <React.Fragment key={index}>
                          {data.codeName}{" "}
                        </React.Fragment>
                      ))}
                      <img
                        src="/assets/img/svg/arrow-right.svg"
                        alt=""
                        className="add"
                      />
                    </dd>
                  </a>
                  <dt>
                    날짜 <span className="red">*</span>
                  </dt>
                  <a
                    href="#none"
                    data-toggle="modal"
                    data-target="#selDateModal"
                  >
                    <dd>
                      {this.state.date !== null && (
                        <React.Fragment>
                          {getDateString(this.state.date)}
                        </React.Fragment>
                      )}{" "}
                      <img
                        src="/assets/img/svg/arrow-right.svg"
                        alt=""
                        className="add"
                      />
                    </dd>
                  </a>
                  <dt>물때</dt>
                  <a data-toggle="modal" data-target="#selTideModal">
                    <dd>
                      {this.state.tide.map((data, index) => (
                        <React.Fragment key={index}>
                          {data.value}{" "}
                        </React.Fragment>
                      ))}
                      <img
                        src="/assets/img/svg/arrow-right.svg"
                        alt=""
                        className="add"
                      />
                    </dd>
                  </a>
                  <dt>낚시 기법</dt>
                  <a data-toggle="modal" data-target="#selTechnicModal">
                    <dd>
                      {this.state.technic.map((data, index) => (
                        <React.Fragment key={index}>
                          {data.value}{" "}
                        </React.Fragment>
                      ))}
                      <img
                        src="/assets/img/svg/arrow-right.svg"
                        alt=""
                        className="add"
                      />
                    </dd>
                  </a>
                  <dt>미끼</dt>
                  <a data-toggle="modal" data-target="#selLureModal">
                    <dd>
                      {this.state.lure.map((data, index) => (
                        <React.Fragment key={index}>
                          {data.codeName}{" "}
                        </React.Fragment>
                      ))}
                      <img
                        src="/assets/img/svg/arrow-right.svg"
                        alt=""
                        className="add"
                      />
                    </dd>
                  </a>
                  <dt>낚시 장소</dt>
                  <a data-toggle="modal" data-target="#selPlaceModal">
                    <dd>
                      {this.state.place.map((data, index) => (
                        <React.Fragment key={index}>
                          {data.value}{" "}
                        </React.Fragment>
                      ))}
                      <img
                        src="/assets/img/svg/arrow-right.svg"
                        alt=""
                        className="add"
                      />
                    </dd>
                  </a>
                </dl>
              </div>
            </div>

            {/** 입력 > 선상/업체 */}
            <div className="container nopadding mt-3">
              <a data-toggle="modal" data-target="#searchLocationModal">
                <div className="card-round-box pt-0 pb-0 pl-0">
                  <div className="row no-gutters d-flex align-items-center">
                    <div className="cardimgWrap">
                      <img
                        src="/assets/img/sample/boat2.jpg"
                        className="img-fluid"
                        alt=""
                      />
                    </div>
                    <div className="cardInfoWrap">
                      <div className="card-body">
                        <img
                          src="/assets/img/svg/arrow-right.svg"
                          alt=""
                          className="float-right-arrow"
                        />
                        <h6>어복황제3호</h6>
                        <p>인천 중구 축항대로 142</p>
                      </div>
                    </div>
                  </div>
                </div>
              </a>
            </div>

            {/** 입력 > 이미지 */}
            <div className="container nopadding mt-3">
              <div className="row no-gutters d-flex align-items-center">
                <div className="col-3">
                  <input
                    ref={this.upload}
                    type="file"
                    accept="image/*"
                    style={{ display: "none" }}
                    onChange={this.onChangeUpload}
                  />
                  <a onClick={this.onClickUpload}>
                    <div className="box-round-grey">
                      <img
                        src="/assets/img/svg/icon-plus-blue.svg"
                        alt=""
                        className="icon-sm icon-plus"
                      />
                    </div>
                  </a>
                </div>
                {this.state.images.map((data, index) => (
                  <ListItem15 key={index} data={data} />
                ))}
              </div>
            </div>

            {/** 입력 > 안내 */}
            <div className="container nopadding mt-5">
              <div className="card card-box">
                <h6 className="card-header-white text-center">
                  <img
                    src="/assets/img/svg/icon-alert.svg"
                    alt=""
                    className="vam"
                  />
                  &nbsp;알려드립니다.
                </h6>
                <div className="card-body">
                  <ul className="list mb-2">
                    <li>
                      대표 어종, 조황 날짜, 업체 또는 지역을 등록하지 않으면
                      글이 등록되지 않습니다.
                    </li>
                    <li>
                      사진(필수항목)은 최대 50장 까지, 동영상(선택항목)은 1개만
                      등록 가능합니다.
                    </li>
                    <li>
                      계좌번호, 전화번호, 홈페이지, 중복 게시글이 포함된 경우
                      삭제됩니다.
                    </li>
                  </ul>
                </div>
              </div>
            </div>

            <Clearfix>
              <br />
              <br />
            </Clearfix>

            {/** 하단버튼 */}
            <div className="fixed-bottom">
              <div className="row no-gutters">
                <div className="col-12">
                  <a href="#none" className="btn btn-primary btn-lg btn-block">
                    올리기
                  </a>
                </div>
              </div>
            </div>

            {/** 모달 팝업 */}
            <SelectFishModal
              id={"selFishModal"}
              onSelected={(selected) => this.setState({ fish: selected || [] })}
            />
            <SelectDateModal
              id={"selDateModal"}
              onSelected={(selected) =>
                this.setState({ date: selected || null })
              }
            />
            <SelectTideModal
              id={"selTideModal"}
              onSelected={(selected) => this.setState({ tide: selected || [] })}
            />
            <SelectTechnicModal
              id={"selTechnicModal"}
              onSelected={(selected) =>
                this.setState({ technic: selected || [] })
              }
            />
            <SelectLureModal
              id={"selLureModal"}
              onSelected={(selected) => this.setState({ lure: selected || [] })}
            />
            <SelectPlaceModal
              id={"selPlaceModal"}
              onSelected={(selected) =>
                this.setState({ place: selected || "" })
              }
            />
            <SearchLocationModal id={"searchLocationModal"} />
          </>
        );
      }
    }
  )
);
