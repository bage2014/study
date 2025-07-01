"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.createTypeScriptRuntime = void 0;
const path = require("path");
const html_1 = require("./plugins/html");
const pug_1 = require("./plugins/pug");
const localTypes = require("./utils/localTypes");
const ts_1 = require("./utils/ts");
const vueFile_1 = require("./vueFile");
const vueFiles_1 = require("./vueFiles");
function createTypeScriptRuntime(options) {
    const { typescript: ts } = options;
    const isVue2 = options.vueLsHost.getVueCompilationSettings().experimentalCompatMode === 2;
    const tsFileVersions = new Map();
    const vueFiles = (0, vueFiles_1.createVueFiles)();
    const plugins = [
        (0, html_1.default)(),
        (0, pug_1.default)(),
    ];
    const tsLsHost = createTsLsHost();
    const tsLsRaw = ts.createLanguageService(tsLsHost);
    const localTypesScript = ts.ScriptSnapshot.fromString(localTypes.getTypesCode(isVue2));
    let lastProjectVersion;
    let tsProjectVersion = 0;
    (0, ts_1.injectCacheLogicToLanguageServiceHost)(ts, tsLsHost, tsLsRaw);
    return {
        vueLsHost: options.vueLsHost,
        vueFiles,
        getTsLs: () => tsLsRaw,
        getTsLsHost: () => tsLsHost,
        update,
        dispose: () => {
            tsLsRaw.dispose();
        },
        getLocalTypesFiles: () => {
            const fileNames = getLocalTypesFiles();
            const code = localTypes.getTypesCode(isVue2);
            return {
                fileNames,
                code,
            };
        },
    };
    function getLocalTypesFiles() {
        return vueFiles.getDirs().map(dir => path.join(dir, localTypes.typesFileName));
    }
    function update() {
        var _a, _b, _c, _d, _e, _f;
        const newProjectVersion = (_b = (_a = options.vueLsHost).getProjectVersion) === null || _b === void 0 ? void 0 : _b.call(_a);
        if (newProjectVersion === undefined || newProjectVersion !== lastProjectVersion) {
            lastProjectVersion = newProjectVersion;
            const fileNames = options.vueLsHost.getScriptFileNames();
            const vueFileNames = new Set(fileNames.filter(file => file.endsWith('.vue')));
            const tsFileNames = new Set(fileNames.filter(file => !file.endsWith('.vue')));
            const fileNamesToRemove = [];
            const fileNamesToCreate = [];
            const fileNamesToUpdate = [];
            let tsFileUpdated = false;
            // .vue
            for (const vueFile of vueFiles.getAll()) {
                if (!vueFileNames.has(vueFile.fileName) && !((_d = (_c = options.vueLsHost).fileExists) === null || _d === void 0 ? void 0 : _d.call(_c, vueFile.fileName))) {
                    // delete
                    fileNamesToRemove.push(vueFile.fileName);
                }
                else {
                    // update
                    const newVersion = options.vueLsHost.getScriptVersion(vueFile.fileName);
                    if (vueFile.getVersion() !== newVersion) {
                        fileNamesToUpdate.push(vueFile.fileName);
                    }
                }
            }
            for (const nowFileName of vueFileNames) {
                if (!vueFiles.get(nowFileName)) {
                    // add
                    fileNamesToCreate.push(nowFileName);
                }
            }
            // .ts / .js / .d.ts / .json ...
            for (const tsFileVersion of tsFileVersions) {
                if (!vueFileNames.has(tsFileVersion[0]) && !((_f = (_e = options.vueLsHost).fileExists) === null || _f === void 0 ? void 0 : _f.call(_e, tsFileVersion[0]))) {
                    // delete
                    tsFileVersions.delete(tsFileVersion[0]);
                    tsFileUpdated = true;
                }
                else {
                    // update
                    const newVersion = options.vueLsHost.getScriptVersion(tsFileVersion[0]);
                    if (tsFileVersion[1] !== newVersion) {
                        tsFileVersions.set(tsFileVersion[0], newVersion);
                        tsFileUpdated = true;
                    }
                }
            }
            for (const nowFileName of tsFileNames) {
                if (!tsFileVersions.has(nowFileName)) {
                    // add
                    const newVersion = options.vueLsHost.getScriptVersion(nowFileName);
                    tsFileVersions.set(nowFileName, newVersion);
                    tsFileUpdated = true;
                }
            }
            if (tsFileUpdated) {
                tsProjectVersion++;
            }
            const finalUpdateFileNames = fileNamesToCreate.concat(fileNamesToUpdate);
            if (fileNamesToRemove.length) {
                unsetSourceFiles(fileNamesToRemove);
            }
            if (finalUpdateFileNames.length) {
                updateSourceFiles(finalUpdateFileNames);
            }
        }
    }
    function createTsLsHost() {
        const scriptSnapshots = new Map();
        const fileVersions = new WeakMap();
        const _tsHost = {
            fileExists: options.vueLsHost.fileExists
                ? fileName => {
                    var _a, _b, _c, _d;
                    // .vue.js -> .vue
                    // .vue.ts -> .vue
                    // .vue.d.ts (never)
                    const fileNameTrim = fileName.substring(0, fileName.lastIndexOf('.'));
                    if (fileNameTrim.endsWith('.vue')) {
                        const vueFile = vueFiles.get(fileNameTrim);
                        if (!vueFile) {
                            const fileExists = !!((_b = (_a = options.vueLsHost).fileExists) === null || _b === void 0 ? void 0 : _b.call(_a, fileNameTrim));
                            if (fileExists) {
                                updateSourceFiles([fileNameTrim]); // create virtual files
                            }
                        }
                    }
                    if (!!vueFiles.fromEmbeddedFileName(fileName)) {
                        return true;
                    }
                    return !!((_d = (_c = options.vueLsHost).fileExists) === null || _d === void 0 ? void 0 : _d.call(_c, fileName));
                }
                : undefined,
            getProjectVersion: () => {
                return tsProjectVersion.toString();
            },
            getScriptFileNames,
            getScriptVersion,
            getScriptSnapshot,
            readDirectory: (_path, extensions, exclude, include, depth) => {
                var _a, _b, _c;
                const result = (_c = (_b = (_a = options.vueLsHost).readDirectory) === null || _b === void 0 ? void 0 : _b.call(_a, _path, extensions, exclude, include, depth)) !== null && _c !== void 0 ? _c : [];
                for (const vuePath of vueFiles.getFileNames()) {
                    const vuePath2 = path.join(_path, path.basename(vuePath));
                    if (path.relative(_path.toLowerCase(), vuePath.toLowerCase()).startsWith('..')) {
                        continue;
                    }
                    if (!depth && vuePath.toLowerCase() === vuePath2.toLowerCase()) {
                        result.push(vuePath2);
                    }
                    else if (depth) {
                        result.push(vuePath2); // TODO: depth num
                    }
                }
                return result;
            },
            getScriptKind(fileName) {
                switch (path.extname(fileName)) {
                    case '.vue': return ts.ScriptKind.TSX; // can't use External, Unknown
                    case '.js': return ts.ScriptKind.JS;
                    case '.jsx': return ts.ScriptKind.JSX;
                    case '.ts': return ts.ScriptKind.TS;
                    case '.tsx': return ts.ScriptKind.TSX;
                    case '.json': return ts.ScriptKind.JSON;
                    default: return ts.ScriptKind.Unknown;
                }
            },
        };
        const tsHost = new Proxy(_tsHost, {
            get: (target, property) => {
                return target[property] || options.vueLsHost[property];
            },
        });
        return tsHost;
        function getScriptFileNames() {
            const tsFileNames = getLocalTypesFiles();
            for (const mapped of vueFiles.getEmbeddeds()) {
                if (mapped.embedded.file.isTsHostFile) {
                    tsFileNames.push(mapped.embedded.file.fileName); // virtual .ts
                }
            }
            for (const fileName of options.vueLsHost.getScriptFileNames()) {
                if (options.isTsPlugin) {
                    tsFileNames.push(fileName); // .vue + .ts
                }
                else if (!fileName.endsWith('.vue')) {
                    tsFileNames.push(fileName); // .ts
                }
            }
            return tsFileNames;
        }
        function getScriptVersion(fileName) {
            var _a, _b, _c;
            const basename = path.basename(fileName);
            if (basename === localTypes.typesFileName) {
                return '';
            }
            let mapped = vueFiles.fromEmbeddedFileName(fileName);
            if (mapped) {
                if (fileVersions.has(mapped.embedded.file)) {
                    return fileVersions.get(mapped.embedded.file);
                }
                else {
                    let version = (_c = (_b = (_a = ts.sys).createHash) === null || _b === void 0 ? void 0 : _b.call(_a, mapped.embedded.file.content)) !== null && _c !== void 0 ? _c : mapped.embedded.file.content;
                    if (options.isVueTsc) {
                        // fix https://github.com/johnsoncodehk/volar/issues/1082
                        version = mapped.vueFile.getVersion() + ':' + version;
                    }
                    fileVersions.set(mapped.embedded.file, version);
                    return version;
                }
            }
            return options.vueLsHost.getScriptVersion(fileName);
        }
        function getScriptSnapshot(fileName) {
            const version = getScriptVersion(fileName);
            const cache = scriptSnapshots.get(fileName.toLowerCase());
            if (cache && cache[0] === version) {
                return cache[1];
            }
            const basename = path.basename(fileName);
            if (basename === localTypes.typesFileName) {
                return localTypesScript;
            }
            const mapped = vueFiles.fromEmbeddedFileName(fileName);
            if (mapped) {
                const text = mapped.embedded.file.content;
                const snapshot = ts.ScriptSnapshot.fromString(text);
                scriptSnapshots.set(fileName.toLowerCase(), [version, snapshot]);
                return snapshot;
            }
            let tsScript = options.vueLsHost.getScriptSnapshot(fileName);
            if (tsScript) {
                if (basename === 'runtime-dom.d.ts') {
                    // allow arbitrary attributes
                    let tsScriptText = tsScript.getText(0, tsScript.getLength());
                    tsScriptText = tsScriptText.replace('type ReservedProps = {', 'type ReservedProps = { [name: string]: any');
                    tsScript = ts.ScriptSnapshot.fromString(tsScriptText);
                }
                scriptSnapshots.set(fileName.toLowerCase(), [version, tsScript]);
                return tsScript;
            }
        }
    }
    function updateSourceFiles(fileNames) {
        let vueScriptsUpdated = false;
        for (const fileName of fileNames) {
            const sourceFile = vueFiles.get(fileName);
            const scriptSnapshot = options.vueLsHost.getScriptSnapshot(fileName);
            if (!scriptSnapshot) {
                continue;
            }
            const scriptText = scriptSnapshot.getText(0, scriptSnapshot.getLength());
            const scriptVersion = options.vueLsHost.getScriptVersion(fileName);
            if (!sourceFile) {
                vueFiles.set(fileName, (0, vueFile_1.createVueFile)(fileName, scriptText, scriptVersion, plugins, options.vueLsHost.getVueCompilationSettings(), options.typescript, options.baseCssModuleType, options.getCssClasses, tsLsRaw, tsLsHost));
                vueScriptsUpdated = true;
            }
            else {
                const updates = sourceFile.update(scriptText, scriptVersion);
                if (updates.scriptUpdated) {
                    vueScriptsUpdated = true;
                }
            }
        }
        if (vueScriptsUpdated) {
            tsProjectVersion++;
        }
    }
    function unsetSourceFiles(uris) {
        let updated = false;
        for (const uri of uris) {
            if (vueFiles.delete(uri)) {
                updated = true;
            }
        }
        if (updated) {
            tsProjectVersion++;
        }
    }
}
exports.createTypeScriptRuntime = createTypeScriptRuntime;
//# sourceMappingURL=typescriptRuntime.js.map