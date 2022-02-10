import React from "react";
import globalHook, { createGlobalState } from 'react-hooks-global-state';

const initialState = {
    status:null,
}

 
export const {useGlobalState} = createGlobalState(initialState);
