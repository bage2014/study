"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.untrack = void 0;
const reactivity_1 = require("@vue/reactivity");
function untrack(source) {
    return ((...args) => {
        (0, reactivity_1.pauseTracking)();
        const result = source(...args);
        (0, reactivity_1.resetTracking)();
        return result;
    });
}
exports.untrack = untrack;
//# sourceMappingURL=untrack.js.map