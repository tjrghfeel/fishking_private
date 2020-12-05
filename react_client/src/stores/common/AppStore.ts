import { makeAutoObservable } from "mobx";

export class AppStore {
  constructor() {
    makeAutoObservable(this);
  }
  /** MemberId */
  memberId: number | null = null;
}

export const createStore = () => {
  return new AppStore();
};

export type TAppStore = ReturnType<typeof createStore>;
