/** Modal Component Props */
export interface ModalProps {
  id: string;
  title?: string;
  innerHtml?: string;
  textOk?: string;
  textCancel?: string;
  onOk?: Function;
  onCancel?: Function;
}
