import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
    LAYOUT: { NavigationLayout },
} = Components;

export default inject("PageStore")(
  observer(
    class extends React.Component {
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/

      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
          const {PageStore} = this.props;
        return <React.Fragment>
            <NavigationLayout title={"검색"} showBackIcon={true} />

            <nav className="nav nav-pills nav-menu nav-justified">
                <a
                    className="nav-link active"
                    onClick={() => PageStore.push(`/search/all`)}
                >
                    전체
                </a>
                <a
                    className="nav-link"
                    onClick={() => PageStore.push(`/search/reserve`)}
                >
                    예약
                </a>
            </nav>

        {/** 검색 */}
            <div className="container nopadding mt-3 mb-0">
                <form className="form-search">
                    <a ><img src="/assets/img/svg/form-search.svg" alt=""
                                                   className="icon-search"/></a>
                    <input className="form-control mr-sm-2" type="search" placeholder="어떤 낚시를 찾고 있나요?"
                           aria-label="Search"/>
                        <a href="search.html"><img src="/assets/img/svg/navbar-search.svg" alt="Search"/></a>
                </form>
            </div>

        {/** 인기검색어 */}
            <div className="container nopadding mt-2 mb-0">
                <h5>인기 검색어 <small className="red"><strong>HOT</strong></small></h5>
                <ul className="list-search">
                    <li><a ><strong>1</strong> 문어</a></li>
                    <li><a ><strong>2</strong> 갈치</a></li>
                    <li><a ><strong>3</strong> 갑오징어</a></li>
                    <li><a ><strong>4</strong> 갈치 <span className="new float-right">NEW</span></a>
                    </li>
                    <li><a ><strong>5</strong> 신봉 <span className="new float-right">NEW</span></a>
                    </li>
                </ul>
                <div className="toggle-content">
                    <ul className="list-search">
                        <li><a ><strong>6</strong> 문어</a></li>
                        <li><a ><strong>7</strong> 갈치</a></li>
                        <li><a ><strong>8</strong> 갑오징어</a></li>
                        <li><a ><strong>9</strong> 갈치 <span className="new float-right">NEW</span></a>
                        </li>
                        <li><a ><strong>10</strong> 신봉 <span
                            className="new float-right">NEW</span></a></li>
                    </ul>
                </div>
                <div className="togglewrap"><a href="#" className="toggle-btn"></a></div>
            </div>

        {/** 어복황제 추천 */}
            <div className="container nopadding mt-2 mb-0">
                <h5>어복황제 추천 <strong className="text-primary">AD</strong></h5>
                <div className="slideList slideList-md">
                    <ul className="listWrap">
                        <li className="item">
                            <a >
                                <div className="imgWrap">
                                    <img src="/assets/cust/img/sample/boat1.jpg" className="img-fluid" alt=""/>
                                        <span className="icon-goods"></span>
                                </div>
                                <div className="InfoWrap">
                                    <h6>챔피온 1호</h6>
                                    <p><strong className="text-primary">돌문어</strong>전남 여수시</p>
                                    <h6>70,000<small>원</small></h6>
                                </div>
                            </a>
                        </li>

                    </ul>
                </div>
            </div>
        </React.Fragment>;
      }
    }
  )
);
