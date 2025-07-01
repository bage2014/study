"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.register = void 0;
function register(ts, context) {
    return {
        getRootFileNames,
        emit,
        getSyntacticDiagnostics,
        getSemanticDiagnostics,
        getGlobalDiagnostics,
        getBindAndCheckDiagnostics,
    };
    function getRootFileNames() {
        return getProgram().getRootFileNames().filter(fileName => { var _a, _b; return (_b = (_a = context.getTsLsHost()).fileExists) === null || _b === void 0 ? void 0 : _b.call(_a, fileName); });
    }
    // for vue-tsc --noEmit --watch
    function getBindAndCheckDiagnostics(sourceFile, cancellationToken) {
        return getSourceFileDiagnosticsWorker(sourceFile, cancellationToken, 'getBindAndCheckDiagnostics');
    }
    // for vue-tsc --noEmit
    function getSyntacticDiagnostics(sourceFile, cancellationToken) {
        return getSourceFileDiagnosticsWorker(sourceFile, cancellationToken, 'getSyntacticDiagnostics');
    }
    function getSemanticDiagnostics(sourceFile, cancellationToken) {
        return getSourceFileDiagnosticsWorker(sourceFile, cancellationToken, 'getSemanticDiagnostics');
    }
    function getSourceFileDiagnosticsWorker(sourceFile, cancellationToken, api) {
        var _a, _b;
        if (sourceFile) {
            const mapped = context.vueFiles.fromEmbeddedFileName(sourceFile.fileName);
            if (mapped) {
                if (!mapped.embedded.file.capabilities.diagnostics)
                    return [];
                const program = getProgram();
                const errors = transformDiagnostics((_a = program === null || program === void 0 ? void 0 : program[api](sourceFile, cancellationToken)) !== null && _a !== void 0 ? _a : []);
                return errors;
            }
        }
        return transformDiagnostics((_b = getProgram()[api](sourceFile, cancellationToken)) !== null && _b !== void 0 ? _b : []);
    }
    function getGlobalDiagnostics(cancellationToken) {
        var _a;
        return transformDiagnostics((_a = getProgram().getGlobalDiagnostics(cancellationToken)) !== null && _a !== void 0 ? _a : []);
    }
    function emit(targetSourceFile, _writeFile, cancellationToken, emitOnlyDtsFiles, customTransformers) {
        var _a;
        const scriptResult = getProgram().emit(targetSourceFile, ((_a = context.vueLsHost.writeFile) !== null && _a !== void 0 ? _a : ts.sys.writeFile), cancellationToken, emitOnlyDtsFiles, customTransformers);
        return {
            emitSkipped: scriptResult.emitSkipped,
            emittedFiles: scriptResult.emittedFiles,
            diagnostics: transformDiagnostics(scriptResult.diagnostics),
        };
    }
    function getProgram() {
        return context.getTsLs().getProgram();
    }
    // transform
    function transformDiagnostics(diagnostics) {
        var _a, _b, _c;
        const result = [];
        for (const diagnostic of diagnostics) {
            if (diagnostic.file !== undefined
                && diagnostic.start !== undefined
                && diagnostic.length !== undefined) {
                for (const tsOrVueLoc of context.vueFiles.fromEmbeddedLocation(diagnostic.file.fileName, diagnostic.start, diagnostic.start + diagnostic.length, data => !!data.capabilities.diagnostic)) {
                    if (!((_b = (_a = context.vueLsHost).fileExists) === null || _b === void 0 ? void 0 : _b.call(_a, tsOrVueLoc.fileName)))
                        continue;
                    let file = tsOrVueLoc.fileName === diagnostic.file.fileName
                        ? diagnostic.file
                        : undefined;
                    if (!file) {
                        let docText = (_c = tsOrVueLoc.mapped) === null || _c === void 0 ? void 0 : _c.vueFile.getContent();
                        if (docText === undefined) {
                            const snapshot = context.vueLsHost.getScriptSnapshot(tsOrVueLoc.fileName);
                            if (snapshot) {
                                docText = snapshot.getText(0, snapshot.getLength());
                            }
                        }
                        else {
                            file = ts.createSourceFile(tsOrVueLoc.fileName, docText, tsOrVueLoc.fileName.endsWith('.vue') ? ts.ScriptTarget.JSON : ts.ScriptTarget.Latest);
                        }
                    }
                    const newDiagnostic = Object.assign(Object.assign({}, diagnostic), { file, start: tsOrVueLoc.range.start, length: tsOrVueLoc.range.end - tsOrVueLoc.range.start });
                    const relatedInformation = diagnostic.relatedInformation;
                    if (relatedInformation) {
                        newDiagnostic.relatedInformation = transformDiagnostics(relatedInformation);
                    }
                    result.push(newDiagnostic);
                    break;
                }
            }
            else if (diagnostic.file === undefined) {
                result.push(diagnostic);
            }
        }
        return result;
    }
}
exports.register = register;
//# sourceMappingURL=apis.js.map