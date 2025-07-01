"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __exportStar = (this && this.__exportStar) || function(m, exports) {
    for (var p in m) if (p !== "default" && !Object.prototype.hasOwnProperty.call(exports, p)) __createBinding(exports, m, p);
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.usePugPlugin = exports.useHtmlPlugin = exports.tsShared = exports.localTypes = void 0;
__exportStar(require("./utils/sourceMaps"), exports);
__exportStar(require("./utils/string"), exports);
__exportStar(require("./vueFile"), exports);
__exportStar(require("./vueFiles"), exports);
__exportStar(require("./types"), exports);
__exportStar(require("./typescriptRuntime"), exports);
exports.localTypes = require("./utils/localTypes");
exports.tsShared = require("./utils/ts");
var html_1 = require("./plugins/html");
Object.defineProperty(exports, "useHtmlPlugin", { enumerable: true, get: function () { return html_1.default; } });
var pug_1 = require("./plugins/pug");
Object.defineProperty(exports, "usePugPlugin", { enumerable: true, get: function () { return pug_1.default; } });
//# sourceMappingURL=index.js.map