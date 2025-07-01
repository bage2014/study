"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.createVueFile = void 0;
const vue_code_gen_1 = require("@volar/vue-code-gen");
const refSugarRanges_1 = require("@volar/vue-code-gen/out/parsers/refSugarRanges");
const scriptRanges_1 = require("@volar/vue-code-gen/out/parsers/scriptRanges");
const scriptSetupRanges_1 = require("@volar/vue-code-gen/out/parsers/scriptSetupRanges");
const compiler_sfc_1 = require("@vue/compiler-sfc");
const reactivity_1 = require("@vue/reactivity");
const useSfcCustomBlocks_1 = require("./use/useSfcCustomBlocks");
const useSfcScript_1 = require("./use/useSfcScript");
const useSfcScriptGen_1 = require("./use/useSfcScriptGen");
const useSfcStyles_1 = require("./use/useSfcStyles");
const useSfcTemplate_1 = require("./use/useSfcTemplate");
const useSfcTemplateScript_1 = require("./use/useSfcTemplateScript");
const parseCssVars_1 = require("./utils/parseCssVars");
const string_1 = require("./utils/string");
const untrack_1 = require("./utils/untrack");
;
function createVueFile(fileName, _content, _version, plugins, compilerOptions, ts, baseCssModuleType, getCssClasses, tsLs, tsHost) {
    // refs
    const content = (0, reactivity_1.ref)('');
    const version = (0, reactivity_1.ref)('');
    const sfc = (0, reactivity_1.reactive)({
        template: null,
        script: null,
        scriptSetup: null,
        styles: [],
        customBlocks: [],
    }) /* avoid Sfc unwrap in .d.ts by reactive */;
    const lastUpdated = {
        template: false,
        script: false,
        scriptSetup: false,
    };
    let templateScriptData = {
        projectVersion: undefined,
        components: [],
        componentItems: [],
    };
    const cssVars = new WeakMap();
    // computeds
    const parsedSfc = (0, reactivity_1.computed)(() => (0, compiler_sfc_1.parse)(content.value, { sourceMap: false, ignoreEmpty: false }));
    // use
    const sfcStyles = (0, useSfcStyles_1.useSfcStyles)(fileName, (0, reactivity_1.computed)(() => sfc.styles));
    const sfcCustomBlocks = (0, useSfcCustomBlocks_1.useSfcCustomBlocks)(fileName, (0, reactivity_1.computed)(() => sfc.customBlocks));
    const sfcTemplate = (0, useSfcTemplate_1.useSfcTemplate)(fileName, (0, reactivity_1.computed)(() => sfc.template));
    const sfcTemplateCompiled = (0, reactivity_1.computed)(() => {
        var _a;
        if (sfc.template) {
            for (const plugin of plugins) {
                const compiledHtml = (_a = plugin.compileTemplate) === null || _a === void 0 ? void 0 : _a.call(plugin, sfc.template.content, sfc.template.lang);
                if (compiledHtml) {
                    return {
                        lang: sfc.template.lang,
                        htmlText: compiledHtml.html,
                        htmlToTemplate: compiledHtml.mapping,
                    };
                }
                ;
            }
        }
    });
    const sfcTemplateCompileResult = (0, reactivity_1.computed)(() => {
        var _a;
        if (sfcTemplateCompiled.value) {
            return (0, vue_code_gen_1.compileSFCTemplate)(sfcTemplateCompiled.value.htmlText, compilerOptions.experimentalTemplateCompilerOptions, (_a = compilerOptions.experimentalCompatMode) !== null && _a !== void 0 ? _a : 3);
        }
    });
    const scriptAst = (0, reactivity_1.computed)(() => {
        if (sfc.script) {
            return ts.createSourceFile(fileName + '.' + sfc.script.lang, sfc.script.content, ts.ScriptTarget.Latest);
        }
    });
    const scriptSetupAst = (0, reactivity_1.computed)(() => {
        if (sfc.scriptSetup) {
            return ts.createSourceFile(fileName + '.' + sfc.scriptSetup.lang, sfc.scriptSetup.content, ts.ScriptTarget.Latest);
        }
    });
    const sfcScript = (0, useSfcScript_1.useSfcScript)(fileName, (0, reactivity_1.computed)(() => sfc.script));
    const sfcScriptSetup = (0, useSfcScript_1.useSfcScript)(fileName, (0, reactivity_1.computed)(() => sfc.scriptSetup));
    const scriptRanges = (0, reactivity_1.computed)(() => scriptAst.value
        ? (0, scriptRanges_1.parseScriptRanges)(ts, scriptAst.value, !!sfc.scriptSetup, false, false)
        : undefined);
    const scriptSetupRanges = (0, reactivity_1.computed)(() => scriptSetupAst.value
        ? (0, scriptSetupRanges_1.parseScriptSetupRanges)(ts, scriptSetupAst.value)
        : undefined);
    const scriptLang = (0, reactivity_1.computed)(() => {
        return !sfc.script && !sfc.scriptSetup ? 'ts'
            : sfc.scriptSetup && sfc.scriptSetup.lang !== 'js' ? sfc.scriptSetup.lang
                : sfc.script && sfc.script.lang !== 'js' ? sfc.script.lang
                    : 'js';
    });
    const sfcTemplateScript = (0, useSfcTemplateScript_1.useSfcTemplateScript)(ts, fileName, (0, reactivity_1.computed)(() => sfc.template), (0, reactivity_1.computed)(() => sfc.script), (0, reactivity_1.computed)(() => sfc.scriptSetup), (0, reactivity_1.computed)(() => scriptSetupRanges.value), (0, reactivity_1.computed)(() => sfc.styles), sfcStyles.files, sfcStyles.embeddeds, sfcTemplateCompiled, sfcTemplateCompileResult, sfcStyles.files, scriptLang, compilerOptions, baseCssModuleType, getCssVBindRanges, getCssClasses, compilerOptions.experimentalCompatMode === 2, !!compilerOptions.experimentalDisableTemplateSupport || !((tsHost === null || tsHost === void 0 ? void 0 : tsHost.getCompilationSettings().jsx) === ts.JsxEmit.Preserve));
    const sfcScriptForTemplateLs = (0, useSfcScriptGen_1.useSfcScriptGen)('template', fileName, content, scriptLang, (0, reactivity_1.computed)(() => sfc.script), (0, reactivity_1.computed)(() => sfc.scriptSetup), (0, reactivity_1.computed)(() => scriptRanges.value), (0, reactivity_1.computed)(() => scriptSetupRanges.value), sfcTemplateScript.templateCodeGens, (0, reactivity_1.computed)(() => sfcStyles.files.value), compilerOptions, getCssVBindRanges);
    const sfcScriptForScriptLs = (0, useSfcScriptGen_1.useSfcScriptGen)('script', fileName, content, scriptLang, (0, reactivity_1.computed)(() => sfc.script), (0, reactivity_1.computed)(() => sfc.scriptSetup), (0, reactivity_1.computed)(() => scriptRanges.value), (0, reactivity_1.computed)(() => scriptSetupRanges.value), sfcTemplateScript.templateCodeGens, (0, reactivity_1.computed)(() => sfcStyles.files.value), compilerOptions, getCssVBindRanges);
    const sfcRefSugarRanges = (0, reactivity_1.computed)(() => (scriptSetupAst.value ? {
        refs: (0, refSugarRanges_1.parseRefSugarDeclarationRanges)(ts, scriptSetupAst.value, ['$ref', '$computed', '$shallowRef', '$fromRefs']),
        raws: (0, refSugarRanges_1.parseRefSugarCallRanges)(ts, scriptSetupAst.value, ['$raw', '$fromRefs']),
    } : undefined));
    // getters
    const teleports = (0, reactivity_1.computed)(() => {
        const _all = [];
        if (sfcScriptForTemplateLs.file.value && sfcScriptForTemplateLs.teleport.value) {
            _all.push({
                file: sfcScriptForTemplateLs.file.value,
                teleport: sfcScriptForTemplateLs.teleport.value,
            });
        }
        return _all;
    });
    const embeddeds = (0, reactivity_1.computed)(() => {
        const embeddeds = [];
        // template
        embeddeds.push({
            self: sfcTemplate.embedded.value,
            embeddeds: [
                {
                    self: sfcTemplateScript.embedded.value,
                    inheritParentIndent: true,
                    embeddeds: [],
                },
                {
                    self: sfcTemplateScript.formatEmbedded.value,
                    inheritParentIndent: true,
                    embeddeds: [],
                },
                {
                    self: sfcTemplateScript.inlineCssEmbedded.value,
                    inheritParentIndent: true,
                    embeddeds: [],
                },
            ],
        });
        // scripts - format
        embeddeds.push({
            self: sfcScript.embedded.value,
            embeddeds: [],
        });
        embeddeds.push({
            self: sfcScriptSetup.embedded.value,
            embeddeds: [],
        });
        // scripts - script ls
        embeddeds.push({
            self: sfcScriptForScriptLs.embedded.value,
            embeddeds: [],
        });
        // scripts - template ls
        embeddeds.push({
            self: sfcScriptForTemplateLs.embedded.value,
            embeddeds: [],
        });
        // styles
        for (const style of sfcStyles.embeddeds.value) {
            embeddeds.push({
                self: style,
                embeddeds: [],
            });
        }
        // customBlocks
        for (const customBlock of sfcCustomBlocks.embeddeds.value) {
            embeddeds.push({
                self: customBlock,
                embeddeds: [],
            });
        }
        return embeddeds;
    });
    const allEmbeddeds = (0, reactivity_1.computed)(() => {
        const all = [];
        visitEmbedded(embeddeds.value, embedded => all.push(embedded));
        return all;
        function visitEmbedded(embeddeds, cb) {
            for (const embedded of embeddeds) {
                visitEmbedded(embedded.embeddeds, cb);
                if (embedded.self) {
                    cb(embedded.self);
                }
            }
        }
    });
    update(_content, _version);
    return {
        fileName,
        getContent: (0, untrack_1.untrack)(() => content.value),
        getSfcTemplateLanguageCompiled: (0, untrack_1.untrack)(() => sfcTemplateCompiled.value),
        getSfcVueTemplateCompiled: (0, untrack_1.untrack)(() => sfcTemplateCompileResult.value),
        getVersion: (0, untrack_1.untrack)(() => version.value),
        getTemplateTagNames: (0, untrack_1.untrack)(() => { var _a; return (_a = sfcTemplateScript.templateCodeGens.value) === null || _a === void 0 ? void 0 : _a.tagNames; }),
        getTemplateAttrNames: (0, untrack_1.untrack)(() => { var _a; return (_a = sfcTemplateScript.templateCodeGens.value) === null || _a === void 0 ? void 0 : _a.attrNames; }),
        update: (0, untrack_1.untrack)(update),
        getTemplateData: (0, untrack_1.untrack)(getTemplateData),
        getScriptTsFile: (0, untrack_1.untrack)(() => sfcScriptForScriptLs.file.value),
        getEmbeddedTemplate: (0, untrack_1.untrack)(() => sfcTemplate.embedded.value),
        getDescriptor: (0, untrack_1.untrack)(() => (0, reactivity_1.unref)(sfc)),
        getScriptAst: (0, untrack_1.untrack)(() => scriptAst.value),
        getScriptSetupAst: (0, untrack_1.untrack)(() => scriptSetupAst.value),
        getTemplateFormattingScript: (0, untrack_1.untrack)(() => sfcTemplateScript.formatEmbedded.value),
        getSfcRefSugarRanges: (0, untrack_1.untrack)(() => sfcRefSugarRanges.value),
        getEmbeddeds: (0, untrack_1.untrack)(() => embeddeds.value),
        getAllEmbeddeds: (0, untrack_1.untrack)(() => allEmbeddeds.value),
        getLastUpdated: (0, untrack_1.untrack)(() => (0, reactivity_1.unref)(lastUpdated)),
        getScriptSetupRanges: (0, untrack_1.untrack)(() => scriptSetupRanges.value),
        getSfcTemplateDocument: (0, untrack_1.untrack)(() => sfcTemplate.file.value),
        isJsxMissing: () => !compilerOptions.experimentalDisableTemplateSupport && !((tsHost === null || tsHost === void 0 ? void 0 : tsHost.getCompilationSettings().jsx) === ts.JsxEmit.Preserve),
        refs: {
            content,
            allEmbeddeds,
            teleports,
            sfcTemplateScript,
            sfcScriptForScriptLs,
        },
    };
    function update(newContent, newVersion) {
        var _a, _b;
        const scriptLang_1 = sfcScriptForScriptLs.file.value.lang;
        const scriptText_1 = sfcScriptForScriptLs.file.value.content;
        const templateScriptContent = (_a = sfcTemplateScript.file.value) === null || _a === void 0 ? void 0 : _a.content;
        content.value = newContent;
        version.value = newVersion;
        updateTemplate(parsedSfc.value.descriptor.template);
        updateScript(parsedSfc.value.descriptor.script);
        updateScriptSetup(parsedSfc.value.descriptor.scriptSetup);
        updateStyles(parsedSfc.value.descriptor.styles);
        updateCustomBlocks(parsedSfc.value.descriptor.customBlocks);
        const scriptLang_2 = sfcScriptForScriptLs.file.value.lang;
        const scriptText_2 = sfcScriptForScriptLs.file.value.content;
        const templateScriptContent_2 = (_b = sfcTemplateScript.file.value) === null || _b === void 0 ? void 0 : _b.content;
        return {
            scriptUpdated: scriptLang_1 !== scriptLang_2 || scriptText_1 !== scriptText_2 || templateScriptContent !== templateScriptContent_2, // TODO
        };
        function updateTemplate(block) {
            var _a, _b, _c;
            const newData = block ? {
                start: newContent.substring(0, block.loc.start.offset).lastIndexOf('<'),
                end: block.loc.end.offset + newContent.substring(block.loc.end.offset).indexOf('>') + 1,
                startTagEnd: block.loc.start.offset,
                endTagStart: block.loc.end.offset,
                content: block.content,
                lang: (_a = block.lang) !== null && _a !== void 0 ? _a : 'html',
            } : null;
            lastUpdated.template = ((_b = sfc.template) === null || _b === void 0 ? void 0 : _b.lang) !== (newData === null || newData === void 0 ? void 0 : newData.lang)
                || ((_c = sfc.template) === null || _c === void 0 ? void 0 : _c.content) !== (newData === null || newData === void 0 ? void 0 : newData.content);
            if (sfc.template && newData) {
                updateBlock(sfc.template, newData);
            }
            else {
                sfc.template = newData;
            }
        }
        function updateScript(block) {
            var _a, _b, _c;
            const newData = block ? {
                start: newContent.substring(0, block.loc.start.offset).lastIndexOf('<'),
                end: block.loc.end.offset + newContent.substring(block.loc.end.offset).indexOf('>') + 1,
                startTagEnd: block.loc.start.offset,
                endTagStart: block.loc.end.offset,
                content: block.content,
                lang: getValidScriptSyntax((_a = block.lang) !== null && _a !== void 0 ? _a : 'js'),
                src: block.src,
            } : null;
            lastUpdated.script = ((_b = sfc.script) === null || _b === void 0 ? void 0 : _b.lang) !== (newData === null || newData === void 0 ? void 0 : newData.lang)
                || ((_c = sfc.script) === null || _c === void 0 ? void 0 : _c.content) !== (newData === null || newData === void 0 ? void 0 : newData.content);
            if (sfc.script && newData) {
                updateBlock(sfc.script, newData);
            }
            else {
                sfc.script = newData;
            }
        }
        function updateScriptSetup(block) {
            var _a, _b, _c;
            const newData = block ? {
                start: newContent.substring(0, block.loc.start.offset).lastIndexOf('<'),
                end: block.loc.end.offset + newContent.substring(block.loc.end.offset).indexOf('>') + 1,
                startTagEnd: block.loc.start.offset,
                endTagStart: block.loc.end.offset,
                content: block.content,
                lang: getValidScriptSyntax((_a = block.lang) !== null && _a !== void 0 ? _a : 'js'),
            } : null;
            lastUpdated.scriptSetup = ((_b = sfc.scriptSetup) === null || _b === void 0 ? void 0 : _b.lang) !== (newData === null || newData === void 0 ? void 0 : newData.lang)
                || ((_c = sfc.scriptSetup) === null || _c === void 0 ? void 0 : _c.content) !== (newData === null || newData === void 0 ? void 0 : newData.content);
            if (sfc.scriptSetup && newData) {
                updateBlock(sfc.scriptSetup, newData);
            }
            else {
                sfc.scriptSetup = newData;
            }
        }
        function updateStyles(blocks) {
            var _a;
            for (let i = 0; i < blocks.length; i++) {
                const block = blocks[i];
                const newData = {
                    start: newContent.substring(0, block.loc.start.offset).lastIndexOf('<'),
                    end: block.loc.end.offset + newContent.substring(block.loc.end.offset).indexOf('>') + 1,
                    startTagEnd: block.loc.start.offset,
                    endTagStart: block.loc.end.offset,
                    content: block.content,
                    lang: (_a = block.lang) !== null && _a !== void 0 ? _a : 'css',
                    module: typeof block.module === 'string' ? block.module : block.module ? '$style' : undefined,
                    scoped: !!block.scoped,
                };
                if (sfc.styles.length > i) {
                    updateBlock(sfc.styles[i], newData);
                }
                else {
                    sfc.styles.push(newData);
                }
            }
            while (sfc.styles.length > blocks.length) {
                sfc.styles.pop();
            }
        }
        function updateCustomBlocks(blocks) {
            var _a;
            for (let i = 0; i < blocks.length; i++) {
                const block = blocks[i];
                const newData = {
                    start: newContent.substring(0, block.loc.start.offset).lastIndexOf('<'),
                    end: block.loc.end.offset + newContent.substring(block.loc.end.offset).indexOf('>') + 1,
                    startTagEnd: block.loc.start.offset,
                    endTagStart: block.loc.end.offset,
                    content: block.content,
                    lang: (_a = block.lang) !== null && _a !== void 0 ? _a : 'txt',
                    type: block.type,
                };
                if (sfc.customBlocks.length > i) {
                    updateBlock(sfc.customBlocks[i], newData);
                }
                else {
                    sfc.customBlocks.push(newData);
                }
            }
            while (sfc.customBlocks.length > blocks.length) {
                sfc.customBlocks.pop();
            }
        }
        function updateBlock(oldBlock, newBlock) {
            for (let key in newBlock) {
                oldBlock[key] = newBlock[key];
            }
        }
    }
    function getTemplateData() {
        var _a, _b, _c, _d;
        if (!tsHost)
            return templateScriptData;
        if (!tsLs)
            return templateScriptData;
        const newVersion = (_a = tsHost.getProjectVersion) === null || _a === void 0 ? void 0 : _a.call(tsHost);
        if (templateScriptData.projectVersion === newVersion) {
            return templateScriptData;
        }
        templateScriptData.projectVersion = newVersion;
        const options = {
            includeCompletionsWithInsertText: true, // if missing, { 'aaa-bbb': any, ccc: any } type only has result ['ccc']
        };
        const file = sfcTemplateScript.file.value;
        const hasFile = file &&
            file.content.indexOf(string_1.SearchTexts.Components) >= 0 &&
            // getSourceFile return undefined for lang=js with allowJs=false;
            !!((_b = tsLs.getProgram()) === null || _b === void 0 ? void 0 : _b.getSourceFile(file.fileName));
        let components = hasFile ? (_d = (_c = tsLs.getCompletionsAtPosition(file.fileName, file.content.indexOf(string_1.SearchTexts.Components), options)) === null || _c === void 0 ? void 0 : _c.entries.filter(entry => entry.kind !== ts.ScriptElementKind.warning)) !== null && _d !== void 0 ? _d : [] : [];
        components = components.filter(entry => {
            return entry.name.indexOf('$') === -1 && !entry.name.startsWith('_');
        });
        const componentNames = components.map(entry => entry.name);
        templateScriptData = {
            projectVersion: newVersion,
            components: componentNames,
            componentItems: components,
        };
        return templateScriptData;
    }
    function getCssVBindRanges(embeddedFile) {
        let binds = cssVars.get(embeddedFile);
        if (!binds) {
            binds = [...(0, parseCssVars_1.parseCssVars)(embeddedFile.content)];
            cssVars.set(embeddedFile, binds);
        }
        return binds;
    }
}
exports.createVueFile = createVueFile;
const validScriptSyntaxs = ['js', 'jsx', 'ts', 'tsx'];
function getValidScriptSyntax(syntax) {
    if (validScriptSyntaxs.includes(syntax)) {
        return syntax;
    }
    return 'js';
}
//# sourceMappingURL=vueFile.js.map