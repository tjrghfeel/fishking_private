import { createStore as createRouteStore } from "./common/RouteStore.ts";
import { createStore as createPageStore } from "./common/PageStore";
import { createStore as createMemberStore } from "./common/MemberStore";

const RouteStore = createRouteStore();
const PageStore = createPageStore();
const MemberStore = createMemberStore();

export const Stores = { RouteStore, PageStore, MemberStore };
