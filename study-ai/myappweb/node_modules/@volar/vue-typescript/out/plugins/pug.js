"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
function default_1() {
    return {
        compileTemplate(template, lang) {
            if (lang === 'pug') {
                let pug;
                try {
                    pug = require('@volar/pug-language-service');
                }
                catch (_a) { }
                const pugDoc = pug === null || pug === void 0 ? void 0 : pug.baseParse(template);
                if (pugDoc) {
                    return {
                        html: pugDoc.htmlCode,
                        mapping: (htmlStart, htmlEnd) => {
                            var _a, _b, _c, _d, _e;
                            const pugRange = (_a = pugDoc.sourceMap.getSourceRange(htmlStart, htmlEnd, data => !(data === null || data === void 0 ? void 0 : data.isEmptyTagCompletion))) === null || _a === void 0 ? void 0 : _a[0];
                            if (pugRange) {
                                return pugRange;
                            }
                            else {
                                const pugStart = (_c = (_b = pugDoc.sourceMap.getSourceRange(htmlStart, htmlStart, data => !(data === null || data === void 0 ? void 0 : data.isEmptyTagCompletion))) === null || _b === void 0 ? void 0 : _b[0]) === null || _c === void 0 ? void 0 : _c.start;
                                const pugEnd = (_e = (_d = pugDoc.sourceMap.getSourceRange(htmlEnd, htmlEnd, data => !(data === null || data === void 0 ? void 0 : data.isEmptyTagCompletion))) === null || _d === void 0 ? void 0 : _d[0]) === null || _e === void 0 ? void 0 : _e.end;
                                if (pugStart !== undefined && pugEnd !== undefined) {
                                    return { start: pugStart, end: pugEnd };
                                }
                            }
                        },
                    };
                }
            }
        }
    };
}
exports.default = default_1;
//# sourceMappingURL=pug.js.map