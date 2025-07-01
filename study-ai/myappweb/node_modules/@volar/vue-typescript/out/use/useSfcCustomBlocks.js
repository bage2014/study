"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.useSfcCustomBlocks = void 0;
const reactivity_1 = require("@vue/reactivity");
const SourceMaps = require("@volar/source-map");
const sourceMaps_1 = require("../utils/sourceMaps");
function useSfcCustomBlocks(fileName, customBlocks) {
    const files = (0, reactivity_1.computed)(() => {
        const _files = [];
        for (let i = 0; i < customBlocks.value.length; i++) {
            const customBlock = customBlocks.value[i];
            _files.push({
                fileName: fileName + '.' + i + '.' + customBlock.lang,
                lang: customBlock.lang,
                content: customBlock.content,
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
            });
        }
        return _files;
    });
    const embeddeds = (0, reactivity_1.computed)(() => {
        const _embeddeds = [];
        for (let i = 0; i < customBlocks.value.length && i < files.value.length; i++) {
            const file = files.value[i];
            const customBlock = customBlocks.value[i];
            const sourceMap = new sourceMaps_1.EmbeddedFileSourceMap();
            sourceMap.mappings.push({
                data: {
                    vueTag: 'customBlock',
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
                    start: customBlock.startTagEnd,
                    end: customBlock.startTagEnd + customBlock.content.length,
                },
                mappedRange: {
                    start: 0,
                    end: customBlock.content.length,
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
exports.useSfcCustomBlocks = useSfcCustomBlocks;
//# sourceMappingURL=useSfcCustomBlocks.js.map