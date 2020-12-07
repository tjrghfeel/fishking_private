import { makeAutoObservable } from "mobx";

export interface ModalProps {
  id: "" | "confirmModal" | "selDateModal" | "selAreaModal"; // modal ID
  title?: string | null; // 제목
  innerHtml?: string | null; // 본문 html
  textOk?: string | null; // OK 버튼 텍스트
  textCancel?: string | null; // Cancel 버튼 텍스트
  onClickOk?: Function; // OK 버튼 콜백
  onClickCancel?: Function; // Cancel 버튼 콜백
  onSelect?: Function; // 선택 콜백
}

export class ModalStore {
  constructor() {
    makeAutoObservable(this);
  }

  /** 모달 데이터 */
  data: ModalProps = { id: "" };

  /** 모달 열기 */
  open(data: ModalProps) {
    this.data = data;
    const { id } = this.data;
    // @ts-ignore
    $("#".concat(id)).modal("show");
  }
}

export const createStore = () => {
  return new ModalStore();
};

export type TModalStore = ReturnType<typeof createStore>;
