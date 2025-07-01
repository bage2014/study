"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.useSfcTemplate = void 0;
const reactivity_1 = require("@vue/reactivity");
const sourceMaps_1 = require("../utils/sourceMaps");
const SourceMaps = require("@volar/source-map");
function useSfcTemplate(fileName, template) {
    const file = (0, reactivity_1.computed)(() => {
        if (template.value) {
            const file = {
                fileName: fileName + '.' + template.value.lang,
                lang: template.value.lang,
                content: template.value.content,
                capabilities: {
                    diagnostics: true,
                    foldingRanges: true,
                    formatting: true,
                    documentSymbol: true,
                    codeActions: true,
                    inlayHints: true,
                },
                data: undefined,
                isTsHostFile: false,
            };
            return file;
        }
    });
    const embedded = (0, reactivity_1.computed)(() => {
        if (template.value && file.value) {
            const sourceMap = new sourceMaps_1.EmbeddedFileSourceMap();
            sourceMap.mappings.push({
                data: {
                    vueTag: 'template',
                    capabilities: {
                        basic: true,
                        references: true,
                        definitions: true,
                        diagnostic: true,
                        rename: true,
                        completion: true,
                        semanticTokens: true,
                    },
                },
                mode: SourceMaps.Mode.Offset,
                sourceRange: {
                    start: template.value.startTagEnd,
                    end: template.value.startTagEnd + template.value.content.length,
                },
                mappedRange: {
                    start: 0,
                    end: template.value.content.length,
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
exports.useSfcTemplate = useSfcTemplate;
//# sourceMappingURL=useSfcTemplate.js.map