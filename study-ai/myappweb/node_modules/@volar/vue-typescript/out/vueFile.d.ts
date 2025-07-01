import { TextRange } from '@volar/vue-code-gen';
import { EmbeddedFileSourceMap } from '@volar/vue-typescript';
import { ITemplateScriptData, VueCompilerOptions } from './types';
import { VueLanguagePlugin } from './typescriptRuntime';
import { Teleport } from './utils/sourceMaps';
import type * as _0 from 'typescript/lib/tsserverlibrary';
export interface VueFile extends ReturnType<typeof createVueFile> {
}
export interface EmbeddedStructure {
    self: Embedded | undefined;
    embeddeds: EmbeddedStructure[];
    inheritParentIndent?: boolean;
}
export interface Embedded {
    file: EmbeddedFile;
    sourceMap: EmbeddedFileSourceMap;
}
export interface SfcBlock {
    start: number;
    end: number;
    startTagEnd: number;
    endTagStart: number;
    lang: string;
    content: string;
}
export interface Sfc {
    template: SfcBlock | null;
    script: (SfcBlock & {
        src: string | undefined;
    }) | null;
    scriptSetup: SfcBlock | null;
    styles: (SfcBlock & {
        module: string | undefined;
        scoped: boolean;
    })[];
    customBlocks: (SfcBlock & {
        type: string;
    })[];
}
export interface EmbeddedFile<T = unknown> {
    fileName: string;
    lang: string;
    content: string;
    isTsHostFile: boolean;
    capabilities: {
        diagnostics: boolean;
        foldingRanges: boolean;
        formatting: boolean;
        documentSymbol: boolean;
        codeActions: boolean;
        inlayHints: boolean;
    };
    data: T;
}
export declare function createVueFile(fileName: string, _content: string, _version: string, plugins: VueLanguagePlugin[], compilerOptions: VueCompilerOptions, ts: typeof import('typescript/lib/tsserverlibrary'), baseCssModuleType: string, getCssClasses: (cssEmbeddeFile: EmbeddedFile) => Record<string, TextRange[]>, tsLs: ts.LanguageService | undefined, tsHost: ts.LanguageServiceHost | undefined): {
    fileName: string;
    getContent: () => string;
    getSfcTemplateLanguageCompiled: () => {
        lang: string;
        htmlText: string;
        htmlToTemplate: (start: number, end: number) => {
            start: number;
            end: number;
        } | undefined;
    } | undefined;
    getSfcVueTemplateCompiled: () => {
        errors: import("@volar/vue-code-gen").CompilerError[];
        warnings: import("@volar/vue-code-gen").CompilerError[];
        ast: import("@volar/vue-code-gen").RootNode | undefined;
    } | undefined;
    getVersion: () => string;
    getTemplateTagNames: () => Record<string, {
        rawComponent: string;
        slotsComponent: string;
        emit: string;
        slots: string;
        offsets: number[];
    }> | undefined;
    getTemplateAttrNames: () => Set<string> | undefined;
    update: (newContent: string, newVersion: string) => {
        scriptUpdated: boolean;
    };
    getTemplateData: () => ITemplateScriptData;
    getScriptTsFile: () => EmbeddedFile<undefined>;
    getEmbeddedTemplate: () => Embedded | undefined;
    getDescriptor: () => Sfc;
    getScriptAst: () => _0.SourceFile | undefined;
    getScriptSetupAst: () => _0.SourceFile | undefined;
    getTemplateFormattingScript: () => Embedded | undefined;
    getSfcRefSugarRanges: () => {
        refs: {
            flag: TextRange;
            leftBindings: TextRange[];
            rightFn: TextRange;
        }[];
        raws: {
            fullRange: TextRange;
            argsRange: TextRange;
        }[];
    } | undefined;
    getEmbeddeds: () => EmbeddedStructure[];
    getAllEmbeddeds: () => Embedded[];
    getLastUpdated: () => {
        template: boolean;
        script: boolean;
        scriptSetup: boolean;
    };
    getScriptSetupRanges: () => {
        importSectionEndOffset: number;
        notOnTopTypeExports: TextRange[];
        bindings: TextRange[];
        typeBindings: TextRange[];
        withDefaultsArg: TextRange | undefined;
        propsAssignName: string | undefined;
        propsRuntimeArg: TextRange | undefined;
        propsTypeArg: TextRange | undefined;
        emitsAssignName: string | undefined;
        emitsRuntimeArg: TextRange | undefined;
        emitsTypeArg: TextRange | undefined;
        emitsTypeNums: number;
        exposeRuntimeArg: TextRange | undefined;
        exposeTypeArg: TextRange | undefined;
    } | undefined;
    getSfcTemplateDocument: () => EmbeddedFile<unknown> | undefined;
    isJsxMissing: () => boolean;
    refs: {
        content: import("@vue/reactivity").Ref<string>;
        allEmbeddeds: import("@vue/reactivity").ComputedRef<Embedded[]>;
        teleports: import("@vue/reactivity").ComputedRef<{
            file: EmbeddedFile;
            teleport: Teleport;
        }[]>;
        sfcTemplateScript: {
            templateCodeGens: import("@vue/reactivity").ComputedRef<{
                codeGen: import("@volar/code-gen").CodeGen<import("@volar/vue-code-gen").EmbeddedFileMappingData>;
                formatCodeGen: import("@volar/code-gen").CodeGen<import("@volar/vue-code-gen").EmbeddedFileMappingData>;
                cssCodeGen: import("@volar/code-gen").CodeGen<import("@volar/vue-code-gen").EmbeddedFileMappingData>;
                tagNames: Record<string, {
                    rawComponent: string;
                    slotsComponent: string;
                    emit: string;
                    slots: string;
                    offsets: number[];
                }>;
                attrNames: Set<string>;
                identifiers: Set<string>;
            } | undefined>;
            embedded: import("@vue/reactivity").ComputedRef<Embedded | undefined>;
            file: import("@vue/reactivity").ComputedRef<EmbeddedFile<unknown> | undefined>;
            formatFile: import("@vue/reactivity").ComputedRef<EmbeddedFile<unknown> | undefined>;
            formatEmbedded: import("@vue/reactivity").ComputedRef<Embedded | undefined>;
            inlineCssFile: import("@vue/reactivity").ComputedRef<EmbeddedFile<unknown> | undefined>;
            inlineCssEmbedded: import("@vue/reactivity").ComputedRef<Embedded | undefined>;
        };
        sfcScriptForScriptLs: {
            lang: import("@vue/reactivity").Ref<string>;
            file: import("@vue/reactivity").ComputedRef<EmbeddedFile<undefined>>;
            embedded: import("@vue/reactivity").ComputedRef<Embedded | undefined>;
            teleport: import("@vue/reactivity").ComputedRef<Teleport>;
        };
    };
};
