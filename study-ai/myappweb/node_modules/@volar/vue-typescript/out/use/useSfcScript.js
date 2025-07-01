"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.useSfcScript = void 0;
const reactivity_1 = require("@vue/reactivity");
const sourceMaps_1 = require("../utils/sourceMaps");
const SourceMaps = require("@volar/source-map");
function useSfcScript(fileName, script) {
    const file = (0, reactivity_1.computed)(() => {
        if (script.value) {
            const file = {
                fileName: fileName + '.__VLS_script.format.' + script.value.lang,
                lang: script.value.lang,
                content: script.value.content,
                capabilities: {
                    diagnostics: false,
                    foldingRanges: true,
                    formatting: true,
                    documentSymbol: true,
                    codeActions: false,
                    inlayHints: false,
                },
                data: undefined,
                isTsHostFile: false,
            };
            return file;
        }
    });
    const embedded = (0, reactivity_1.computed)(() => {
        if (script.value && file.value) {
            const sourceMap = new sourceMaps_1.EmbeddedFileSourceMap();
            sourceMap.mappings.push({
                data: {
                    vueTag: 'script',
                    capabilities: {},
                },
                mode: SourceMaps.Mode.Offset,
                sourceRange: {
                    start: script.value.startTagEnd,
                    end: script.value.startTagEnd + script.value.content.length,
                },
                mappedRange: {
                    start: 0,
                    end: script.value.content.length,
                },
            });
            return {
                file: file.value,
                sourceMap,
            };
        }
    });
    return {
        file,
        embedded,
    };
}
exports.useSfcScript = useSfcScript;
//# sourceMappingURL=useSfcScript.js.map