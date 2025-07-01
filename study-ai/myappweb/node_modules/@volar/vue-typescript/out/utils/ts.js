"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.createParsedCommandLine = exports.injectCacheLogicToLanguageServiceHost = void 0;
const moduleSpecifierCache_1 = require("./moduleSpecifierCache");
const packageJsonCache_1 = require("./packageJsonCache");
const path = require("path");
function injectCacheLogicToLanguageServiceHost(ts, host, service) {
    const _createCacheableExportInfoMap = ts.createCacheableExportInfoMap;
    const _combinePaths = ts.combinePaths;
    const _forEachAncestorDirectory = ts.forEachAncestorDirectory;
    const _getDirectoryPath = ts.getDirectoryPath;
    const _toPath = ts.toPath;
    const _createGetCanonicalFileName = ts.createGetCanonicalFileName;
    if (!_createCacheableExportInfoMap
        || !_combinePaths
        || !_forEachAncestorDirectory
        || !_getDirectoryPath
        || !_toPath
        || !_createGetCanonicalFileName
        || !(0, packageJsonCache_1.canCreatePackageJsonCache)(ts))
        return;
    const moduleSpecifierCache = (0, moduleSpecifierCache_1.createModuleSpecifierCache)();
    const exportMapCache = _createCacheableExportInfoMap({
        getCurrentProgram() {
            return service.getProgram();
        },
        getPackageJsonAutoImportProvider() {
            return service.getProgram();
        },
    });
    const packageJsonCache = (0, packageJsonCache_1.createPackageJsonCache)(ts, Object.assign(Object.assign({}, host), { 
        // @ts-expect-error
        host: Object.assign({}, host), toPath }));
    // @ts-expect-error
    host.getCachedExportInfoMap = () => exportMapCache;
    // @ts-expect-error
    host.getModuleSpecifierCache = () => moduleSpecifierCache;
    // @ts-expect-error
    host.getPackageJsonsVisibleToFile = (fileName, rootDir) => {
        const rootPath = rootDir && toPath(rootDir);
        const filePath = toPath(fileName);
        const result = [];
        const processDirectory = (directory) => {
            switch (packageJsonCache.directoryHasPackageJson(directory)) {
                // Sync and check same directory again
                case 3 /* Maybe */:
                    packageJsonCache.searchDirectoryAndAncestors(directory);
                    return processDirectory(directory);
                // Check package.json
                case -1 /* True */:
                    const packageJsonFileName = _combinePaths(directory, "package.json");
                    // this.watchPackageJsonFile(packageJsonFileName as ts.Path); // TODO
                    const info = packageJsonCache.getInDirectory(directory);
                    if (info)
                        result.push(info);
            }
            if (rootPath && rootPath === directory) {
                return true;
            }
        };
        _forEachAncestorDirectory(_getDirectoryPath(filePath), processDirectory);
        return result;
    };
    function toPath(fileName) {
        var _a;
        return _toPath(fileName, host.getCurrentDirectory(), _createGetCanonicalFileName((_a = host.useCaseSensitiveFileNames) === null || _a === void 0 ? void 0 : _a.call(host)));
    }
}
exports.injectCacheLogicToLanguageServiceHost = injectCacheLogicToLanguageServiceHost;
function createParsedCommandLine(ts, parseConfigHost, tsConfig, extendsSet = new Set()) {
    var _a;
    const tsConfigPath = ts.sys.resolvePath(tsConfig);
    const config = ts.readJsonConfigFile(tsConfigPath, ts.sys.readFile);
    const content = ts.parseJsonSourceFileConfigFileContent(config, parseConfigHost, path.dirname(tsConfigPath), {}, path.basename(tsConfigPath));
    content.options.outDir = undefined; // TODO: patching ts server broke with outDir + rootDir + composite/incremental
    let baseVueOptions = {};
    const folder = path.dirname(tsConfig);
    extendsSet.add(tsConfig);
    if (content.raw.extends) {
        try {
            const extendsPath = require.resolve(content.raw.extends, { paths: [folder] });
            if (!extendsSet.has(extendsPath)) {
                baseVueOptions = createParsedCommandLine(ts, parseConfigHost, extendsPath, extendsSet).vueOptions;
            }
        }
        catch (error) {
            console.error(error);
        }
    }
    return Object.assign(Object.assign({}, content), { vueOptions: Object.assign(Object.assign({}, baseVueOptions), resolveVueCompilerOptions((_a = content.raw.vueCompilerOptions) !== null && _a !== void 0 ? _a : {}, folder)) });
}
exports.createParsedCommandLine = createParsedCommandLine;
function resolveVueCompilerOptions(rawOptions, rootPath) {
    const result = Object.assign({}, rawOptions);
    let templateOptionsPath = rawOptions.experimentalTemplateCompilerOptionsRequirePath;
    if (templateOptionsPath) {
        if (!path.isAbsolute(templateOptionsPath)) {
            templateOptionsPath = require.resolve(templateOptionsPath, { paths: [rootPath] });
        }
        try {
            result.experimentalTemplateCompilerOptions = require(templateOptionsPath).default;
        }
        catch (error) {
            console.warn('Failed to require "experimentalTemplateCompilerOptionsRequirePath":', templateOptionsPath);
            console.error(error);
        }
    }
    return result;
}
//# sourceMappingURL=ts.js.map