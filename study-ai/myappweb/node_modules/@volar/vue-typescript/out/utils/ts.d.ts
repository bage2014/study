import type * as ts from 'typescript/lib/tsserverlibrary';
export declare function injectCacheLogicToLanguageServiceHost(ts: typeof import('typescript/lib/tsserverlibrary'), host: ts.LanguageServiceHost, service: ts.LanguageService): void;
export declare function createParsedCommandLine(ts: typeof import('typescript/lib/tsserverlibrary'), parseConfigHost: ts.ParseConfigHost, tsConfig: string, extendsSet?: Set<string>): ts.ParsedCommandLine & {
    vueOptions: {
        experimentalCompatMode?: 2 | 3;
        experimentalTemplateCompilerOptions?: any;
        experimentalTemplateCompilerOptionsRequirePath?: string;
    };
};
