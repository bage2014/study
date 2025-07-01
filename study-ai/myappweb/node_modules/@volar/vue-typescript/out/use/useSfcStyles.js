"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.useSfcStyles = void 0;
const reactivity_1 = require("@vue/reactivity");
const SourceMaps = require("@volar/source-map");
const sourceMaps_1 = require("../utils/sourceMaps");
function useSfcStyles(fileName, styles) {
    const files = (0, reactivity_1.computed)(() => {
        const _files = [];
        for (let i = 0; i < styles.value.length; i++) {
            const style = styles.value[i];
            _files.push({
                fileName: fileName + '.' + i + '.' + style.lang,
                lang: style.lang,
                content: style.content,
                capabilities: {
                    diagnostics: true,
                    foldingRanges: true,
                    formatting: true,
                    documentSymbol: true,
                    codeActions: true,
                    inlayHints: true,
                },
                data: {
                    module: style.module,
                    scoped: style.scoped,
                },
                isTsHostFile: false,
            });
        }
        return _files;
    });
    const embeddeds = (0, reactivity_1.computed)(() => {
        const _embeddeds = [];
        for (let i = 0; i < styles.value.length && i < files.value.length; i++) {
            const file = files.value[i];
            const style = styles.value[i];
            const sourceMap = new sourceMaps_1.EmbeddedFileSourceMap();
            sourceMap.mappings.push({
                data: {
                    vueTag: 'style',
                    vueTagIndex: i,
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
                    start: style.startTagEnd,
                    end: style.startTagEnd + style.content.length,
                },
                mappedRange: {
                    start: 0,
                    end: style.content.length,
                },
            });
            _embeddeds.push({ file, sourceMap });
        }
        return _embeddeds;
    });
    return {
        files,
        embeddeds,
    };
}
exports.useSfcStyles = useSfcStyles;
//# sourceMappingURL=useSfcStyles.js.map