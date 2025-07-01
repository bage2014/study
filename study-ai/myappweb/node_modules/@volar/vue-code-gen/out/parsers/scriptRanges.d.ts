import type { TextRange } from '../types';
export interface ScriptRanges extends ReturnType<typeof parseScriptRanges> {
}
export declare function parseScriptRanges(ts: typeof import('typescript/lib/tsserverlibrary'), ast: ts.SourceFile, hasScriptSetup: boolean, withComponentOption: boolean, withNode: boolean): {
    exportDefault: (TextRange & {
        expression: TextRange;
        args: TextRange;
        argsNode: ts.ObjectLiteralExpression | undefined;
        componentsOption: TextRange | undefined;
        componentsOptionNode: ts.ObjectLiteralExpression | undefined;
    }) | undefined;
    bindings: TextRange[];
};
