import React from "react";
import { inject, observer } from "mobx-react";

import ConfirmModal from "./ConfirmModal";
import SelDateModal from "./SelDateModal";
import SelAreaModal from "./SelAreaModal";

const Modals = inject("ModalStore")(
  observer(() => {
    return (
      <>
        <ConfirmModal />
        <SelDateModal />
        <SelAreaModal />
      </>
    );
  })
);

export default Modals;
