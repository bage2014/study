"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
function default_1() {
    return {
        compileTemplate(template, lang) {
            if (lang === 'html') {
                return {
                    html: template,
                    mapping: (htmlStart, htmlEnd) => ({ start: htmlStart, end: htmlEnd }),
                };
            }
        }
    };
}
exports.default = default_1;
//# sourceMappingURL=html.js.map