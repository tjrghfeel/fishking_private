import React, {
  useState,
  useCallback,
  useImperativeHandle,
  forwardRef,
} from "react";
import { inject, observer } from "mobx-react";

export default inject("APIStore")(
  observer(
    forwardRef(({ APIStore, id = "", type, onSelected }, ref) => {
      const [list, setList] = useState([]);
      const [selected, setSelected] = useState(null);
      const [searchKey, setSearchKey] = useState('');
      const load = useCallback(
        async (type) => {
          setList([]);
          const resolve = await APIStore._get(`/v2/api/searchPointList`, {
            type, searchKey
          });
          setList(resolve);
        },
        [setList]
      );
      useImperativeHandle(ref, () => ({ load }));
      const search = async (e)=>{
          e.preventDefault();
          setList([]);
          const resolve = await APIStore._get(`/v2/api/searchPointList`, {
              type,searchKey
          });
          setList(resolve);
      }

      return (
        <div
          className="modal fade modal-full"
          id={id}
          tabIndex="-1"
          aria-labelledby={id.concat("Label")}
          aria-hidden="true"
        >
          <div className="modal-dialog">
            <div className="modal-content">
              <div className="modal-header bg-primary d-flex justify-content-center">
                <a data-dismiss="modal" className="nav-left">
                  <img
                    src="/assets/cust/img/svg/navbar-back.svg"
                    alt="뒤로가기"
                  />
                </a>
                <h5 className="modal-title" id={id.concat("Label")}>
                  지역선택
                </h5>
              </div>
              <div className="modal-body">
                <div className="row-region-col-two">

                    <form style={{width:'100%'}} onSubmit={(e)=>search(e)}>
                        <label style={{width:'20%',textAlign:'center',margin:8, fontSize:15}}>위치선택</label>
                        <input style={{border:'1px solid #2b79c8',borderRadius:5,width:'50%'}} type="text" onChange={(e)=>{
                            setSearchKey(e.target.value)
                        }}/>
                        <input style={{width:'10%', border:'1px solid #2b79c8', borderRadius:5, backgroundColor:'#2b79c8', color:'white',
                            margin:8}} type="submit" value="검색"/>
                    </form>

                  <div className="col">
                    <ul className="region col2">
                      {(list===null || list.length===0)?
                          (<div style={{textAlign:'center'}}>검색 결과가 없습니다</div>)
                              :
                              list.map((data, index) => (
                        <li
                          key={index}
                          onClick={(e) => {
                            for (let li of document.querySelectorAll(
                              `#${id} li`
                            )) {
                              if (e.target.parentElement !== li)
                                li.classList.remove("active");
                            }
                            e.target.parentElement.classList.add("active");
                            setSelected(data);
                          }}
                        >
                          {data["isAlerted"] && (
                            <a>
                              <span className="icon icon-alarm on"></span>
                            </a>
                          )}
                          <a className="link">{data["observerName"]}</a>
                        </li>
                      ))}
                    </ul>
                  </div>
                </div>
              </div>
              <a
                onClick={() => (onSelected ? onSelected(selected) : null)}
                className="btn btn-primary btn-lg btn-block btn-btm"
                data-dismiss="modal"
              >
                적용하기
              </a>
            </div>
          </div>
        </div>
      );
    })
  )
);
