import type * as ts from 'typescript/lib/tsserverlibrary';
import type { TypeScriptRuntime } from '@volar/vue-typescript';
export declare function register(ts: typeof import('typescript/lib/tsserverlibrary'), context: TypeScriptRuntime): {
    getRootFileNames: () => string[];
    emit: (targetSourceFile?: ts.SourceFile | undefined, _writeFile?: ts.WriteFileCallback | undefined, cancellationToken?: ts.CancellationToken | undefined, emitOnlyDtsFiles?: boolean | undefined, customTransformers?: ts.CustomTransformers | undefined) => ts.EmitResult;
    getSyntacticDiagnostics: (sourceFile?: ts.SourceFile | undefined, cancellationToken?: ts.CancellationToken | undefined) => readonly ts.DiagnosticWithLocation[] | readonly ts.Diagnostic[];
    getSemanticDiagnostics: (sourceFile?: ts.SourceFile | undefined, cancellationToken?: ts.CancellationToken | undefined) => readonly ts.DiagnosticWithLocation[] | readonly ts.Diagnostic[];
    getGlobalDiagnostics: (cancellationToken?: ts.CancellationToken | undefined) => readonly ts.Diagnostic[];
    getBindAndCheckDiagnostics: (sourceFile?: ts.SourceFile | undefined, cancellationToken?: ts.CancellationToken | undefined) => readonly ts.DiagnosticWithLocation[] | readonly ts.Diagnostic[];
};
