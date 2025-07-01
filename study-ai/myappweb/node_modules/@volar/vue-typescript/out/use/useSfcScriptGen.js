"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.useSfcScriptGen = void 0;
const reactivity_1 = require("@vue/reactivity");
const sourceMaps_1 = require("../utils/sourceMaps");
const script_1 = require("@volar/vue-code-gen/out/generators/script");
const localTypes_1 = require("../utils/localTypes");
function useSfcScriptGen(lsType, fileName, vueFileContent, lang, script, scriptSetup, scriptRanges, scriptSetupRanges, htmlGen, sfcStyles, compilerOptions, getCssVBindRanges) {
    const codeGen = (0, reactivity_1.computed)(() => {
        var _a, _b, _c, _d;
        return (0, script_1.generate)(lsType, fileName, (_a = script.value) !== null && _a !== void 0 ? _a : undefined, (_b = scriptSetup.value) !== null && _b !== void 0 ? _b : undefined, scriptRanges.value, scriptSetupRanges.value, () => htmlGen.value, () => {
            const bindTexts = [];
            for (const style of sfcStyles.value) {
                const binds = getCssVBindRanges(style);
                for (const cssBind of binds) {
                    const bindText = style.content.substring(cssBind.start, cssBind.end);
                    bindTexts.push(bindText);
                }
            }
            return bindTexts;
        }, (0, localTypes_1.getVueLibraryName)(compilerOptions.experimentalCompatMode === 2), ((_c = compilerOptions.experimentalImplicitWrapComponentOptionsWithDefineComponent) !== null && _c !== void 0 ? _c : 'onlyJs') === 'onlyJs'
            ? lang.value === 'js' || lang.value === 'jsx'
            : !!compilerOptions.experimentalImplicitWrapComponentOptionsWithDefineComponent, ((_d = compilerOptions.experimentalDowngradePropsAndEmitsToSetupReturnOnScriptSetup) !== null && _d !== void 0 ? _d : 'onlyJs') === 'onlyJs'
            ? lang.value === 'js' || lang.value === 'jsx'
            : !!compilerOptions.experimentalDowngradePropsAndEmitsToSetupReturnOnScriptSetup);
    });
    const file = (0, reactivity_1.computed)(() => {
        var _a, _b, _c;
        if (lsType === 'script') {
            const file = {
                fileName: fileName + '.' + lang.value,
                lang: lang.value,
                content: codeGen.value.codeGen.getText(),
                capabilities: {
                    diagnostics: !((_a = script.value) === null || _a === void 0 ? void 0 : _a.src),
                    foldingRanges: false,
                    formatting: false,
                    documentSymbol: false,
                    codeActions: !((_b = script.value) === null || _b === void 0 ? void 0 : _b.src),
                    inlayHints: !((_c = script.value) === null || _c === void 0 ? void 0 : _c.src),
                },
                data: undefined,
                isTsHostFile: true,
            };
            return file;
        }
        else if (script.value || scriptSetup.value) {
            const file = {
                fileName: fileName + '.__VLS_script.' + lang.value,
                lang: lang.value,
                content: codeGen.value.codeGen.getText(),
                capabilities: {
                    diagnostics: false,
                    foldingRanges: false,
                    formatting: false,
                    documentSymbol: false,
                    codeActions: false,
                    inlayHints: false,
                },
                data: undefined,
                isTsHostFile: true,
            };
            return file;
        }
    });
    const embedded = (0, reactivity_1.computed)(() => {
        if (file.value) {
            return {
                sourceMap: new sourceMaps_1.EmbeddedFileSourceMap(codeGen.value.codeGen.getMappings(parseMappingSourceRange)),
                file: file.value,
            };
        }
    });
    const teleport = (0, reactivity_1.computed)(() => {
        const teleport = new sourceMaps_1.Teleport();
        for (const mapping of codeGen.value.teleports) {
            teleport.mappings.push(mapping);
        }
        return teleport;
    });
    return {
        lang,
        file: file,
        embedded,
        teleport,
    };
    // TODO
    function parseMappingSourceRange(data, sourceRange) {
        var _a;
        if (data.vueTag === 'scriptSrc' && ((_a = script.value) === null || _a === void 0 ? void 0 : _a.src)) {
            const vueStart = vueFileContent.value.substring(0, script.value.startTagEnd).lastIndexOf(script.value.src);
            const vueEnd = vueStart + script.value.src.length;
            return {
                start: vueStart - 1,
                end: vueEnd + 1,
            };
        }
        else if (data.vueTag === 'script' && script.value) {
            return {
                start: script.value.startTagEnd + sourceRange.start,
                end: script.value.startTagEnd + sourceRange.end,
            };
        }
        else if (data.vueTag === 'scriptSetup' && scriptSetup.value) {
            return {
                start: scriptSetup.value.startTagEnd + sourceRange.start,
                end: scriptSetup.value.startTagEnd + sourceRange.end,
            };
        }
        else {
            return sourceRange;
        }
    }
}
exports.useSfcScriptGen = useSfcScriptGen;
//# sourceMappingURL=useSfcScriptGen.js.map