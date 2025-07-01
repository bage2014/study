import type * as ts from 'typescript/lib/tsserverlibrary';
export declare function walkInterpolationFragment(ts: typeof import('typescript/lib/tsserverlibrary'), code: string, cb: (fragment: string, offset: number | undefined, isJustForErrorMapping?: boolean) => void, localVars: Record<string, number>, identifiers: Set<string>): void;
export declare function colletVars(ts: typeof import('typescript/lib/tsserverlibrary'), node: ts.Node, result: string[]): void;
