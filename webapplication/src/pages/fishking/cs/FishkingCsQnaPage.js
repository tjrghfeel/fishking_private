import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  Common: {
    Layout: { Navigation },
    View: { CommonCsQnaRegistView },
  },
  Fishking: {
    Layout: { CsTopTab, CsQnaTopTab },
  },
} = Components;

export default inject(
  "PageStore",
  "DataStore",
  "APIStore",
  "ModalStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.file = React.createRef(null);
        this.state = {
          options: [],
          questionType: "",
          contents: "",
          returnType: "tel",
          returnAddress: "",
          fileList: [],
        };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      async componentDidMount() {
        const { DataStore } = this.props;
        const options = await DataStore.getEnums("questionType");
        this.setState({ options });
      }
      uploadFile = async () => {
        const { APIStore } = this.props;
        if (this.file.current?.files.length > 0) {
          const file = this.file.current?.files[0];

          const form = new FormData();
          form.append("file", file);
          form.append("filePublish", "one2one");

          const { fileId } = await APIStore._post_upload(
            "/v2/api/filePreUpload",
            form
          );
          this.setState({ fileList: [fileId] });
        }
      };
      requestSubmit = async () => {
        const { APIStore, ModalStore, DataStore, PageStore } = this.props;
        const {
          questionType,
          contents,
          returnType,
          returnAddress,
          fileList,
        } = this.state;

        if (questionType === "") {
          ModalStore.openModal("Alert", {
            title: "알림",
            body: "카테고리를 선택해주세요.",
          });
          return;
        }
        if (contents === "") {
          ModalStore.openModal("Alert", {
            title: "알림",
            body: "내용을 입력해주세요.",
          });
          return;
        }
        if (returnType === "tel" && !DataStore.isMobile(returnAddress)) {
          ModalStore.openModal("Alert", {
            title: "알림",
            body: "연락처를 확인해주세요.",
          });
          return;
        } else if (
          returnType === "email" &&
          !DataStore.isEmail(returnAddress)
        ) {
          ModalStore.openModal("Alert", {
            title: "알림",
            body: "이메일을 확인해주세요.",
          });
          return;
        }
        if (fileList.length === 0) {
          ModalStore.openModal("Alert", {
            title: "알림",
            body: "첨부파일을 선택해주세요.",
          });
          return;
        }
        const resolve = await APIStore._post("/v2/api/post/one2one", {
          questionType,
          contents,
          returnType,
          returnAddress,
          fileList,
        });
        if (resolve) {
          PageStore.push(`/fishking/cs/qna/list`);
        }
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            <Navigation title={"고객센터"} showBackIcon={true} />

            <CsTopTab activeIndex={2} />

            <CommonCsQnaRegistView
              navigateTo={{
                tab1: `/fishking/cs/qna`,
                tab2: `/fishking/cs/qna/list`,
              }}
            />
          </React.Fragment>
        );
      }
    }
  )
);
