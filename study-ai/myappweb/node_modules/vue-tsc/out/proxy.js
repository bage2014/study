"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.createProgramProxy = void 0;
const ts = require("typescript/lib/tsserverlibrary");
const apis = require("./apis");
const vue_typescript_1 = require("@volar/vue-typescript");
const vue_typescript_2 = require("@volar/vue-typescript");
let projectVersion = 0;
function createProgramProxy(options, // rootNamesOrOptions: readonly string[] | CreateProgramOptions,
_options, _host, _oldProgram, _configFileParsingDiagnostics) {
    var _a, _b;
    if (!options.options.noEmit && !options.options.emitDeclarationOnly)
        return doThrow('js emit is not support');
    if (!options.host)
        return doThrow('!options.host');
    projectVersion++;
    const host = options.host;
    const vueCompilerOptions = getVueCompilerOptions();
    const scripts = new Map();
    const vueLsHost = Object.assign(Object.assign({}, host), { resolveModuleNames: undefined, writeFile: (fileName, content) => {
            if (fileName.indexOf('__VLS_') === -1) {
                host.writeFile(fileName, content, false);
            }
        }, getCompilationSettings: () => options.options, getVueCompilationSettings: () => vueCompilerOptions, getScriptFileNames: () => {
            return options.rootNames;
        }, getScriptVersion,
        getScriptSnapshot, getProjectVersion: () => {
            return projectVersion.toString();
        }, getProjectReferences: () => options.projectReferences });
    const tsRuntime = (_b = (_a = options.oldProgram) === null || _a === void 0 ? void 0 : _a.__VLS_tsRuntime) !== null && _b !== void 0 ? _b : (0, vue_typescript_1.createTypeScriptRuntime)({
        typescript: ts,
        baseCssModuleType: 'any',
        getCssClasses: () => ({}),
        vueLsHost: vueLsHost,
        isVueTsc: true,
    });
    tsRuntime.update(); // must update before getProgram() to update virtual scripts
    const tsProgram = tsRuntime.getTsLs().getProgram();
    if (!tsProgram)
        throw '!tsProgram';
    const proxyApis = apis.register(ts, tsRuntime);
    const program = new Proxy(tsProgram, {
        get: (target, property) => {
            tsRuntime.update();
            return proxyApis[property] || target[property];
        },
    });
    program.__VLS_tsRuntime = tsRuntime;
    for (const rootName of options.rootNames) {
        // register file watchers
        host.getSourceFile(rootName, ts.ScriptTarget.ESNext);
    }
    return program;
    function getVueCompilerOptions() {
        const tsConfig = options.options.configFilePath;
        if (typeof tsConfig === 'string') {
            return vue_typescript_2.tsShared.createParsedCommandLine(ts, ts.sys, tsConfig).vueOptions;
        }
        return {};
    }
    function getScriptVersion(fileName) {
        var _a, _b;
        return (_b = (_a = getScript(fileName)) === null || _a === void 0 ? void 0 : _a.version) !== null && _b !== void 0 ? _b : '';
    }
    function getScriptSnapshot(fileName) {
        var _a;
        return (_a = getScript(fileName)) === null || _a === void 0 ? void 0 : _a.scriptSnapshot;
    }
    function getScript(fileName) {
        var _a, _b, _c, _d, _e, _f;
        const script = scripts.get(fileName);
        if ((script === null || script === void 0 ? void 0 : script.projectVersion) === projectVersion) {
            return script;
        }
        const modifiedTime = (_d = (_c = (_b = (_a = ts.sys).getModifiedTime) === null || _b === void 0 ? void 0 : _b.call(_a, fileName)) === null || _c === void 0 ? void 0 : _c.valueOf()) !== null && _d !== void 0 ? _d : 0;
        if ((script === null || script === void 0 ? void 0 : script.modifiedTime) === modifiedTime) {
            return script;
        }
        if (host.fileExists(fileName)) {
            const fileContent = host.readFile(fileName);
            if (fileContent !== undefined) {
                const script = {
                    projectVersion,
                    modifiedTime,
                    scriptSnapshot: ts.ScriptSnapshot.fromString(fileContent),
                    version: (_f = (_e = host.createHash) === null || _e === void 0 ? void 0 : _e.call(host, fileContent)) !== null && _f !== void 0 ? _f : fileContent,
                };
                scripts.set(fileName, script);
                return script;
            }
        }
    }
}
exports.createProgramProxy = createProgramProxy;
function doThrow(msg) {
    console.error(msg);
    throw msg;
}
//# sourceMappingURL=proxy.js.map