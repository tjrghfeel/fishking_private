import React from "react";
import { inject, observer } from "mobx-react";

import ConfirmModal from "./ConfirmModal";

const Modals = inject("ModalStore")(
  observer(() => {
    return <ConfirmModal />;
  })
);

export default Modals;
