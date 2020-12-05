import { createStore as createAppStore } from "./common/AppStore.ts";
import { createStore as createRouteStore } from "./common/RouteStore.ts";
import { createStore as createPageStore } from "./common/PageStore.ts";
import { createStore as createModalStore } from "./common/ModalStore";

import { createStore as createMyStore } from "./my/MyStore.ts";

const AppStore = createAppStore();
const RouteStore = createRouteStore();
const PageStore = createPageStore();
const ModalStore = createModalStore();

const MyStore = createMyStore();

const Stores = {
  AppStore,
  RouteStore,
  PageStore,
  MyStore,
  ModalStore,
};

export default Stores;
