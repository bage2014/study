import { CodeGen } from '@volar/code-gen';
import type { parseScriptSetupRanges } from '@volar/vue-code-gen/out/parsers/scriptSetupRanges';
import { Ref } from '@vue/reactivity';
import { VueCompilerOptions } from '../types';
import type { TextRange } from '@volar/vue-code-gen';
import { Embedded, EmbeddedFile, Sfc } from '../vueFile';
import { useSfcStyles } from './useSfcStyles';
import { EmbeddedFileMappingData } from '@volar/vue-code-gen';
export declare function useSfcTemplateScript(ts: typeof import('typescript/lib/tsserverlibrary'), fileName: string, template: Ref<Sfc['template']>, script: Ref<Sfc['script']>, scriptSetup: Ref<Sfc['scriptSetup']>, scriptSetupRanges: Ref<ReturnType<typeof parseScriptSetupRanges> | undefined>, styles: Ref<Sfc['styles']>, styleFiles: ReturnType<typeof useSfcStyles>['files'], styleEmbeddeds: ReturnType<typeof useSfcStyles>['embeddeds'], templateData: Ref<{
    lang: string;
    htmlToTemplate: (start: number, end: number) => {
        start: number;
        end: number;
    } | undefined;
} | undefined>, sfcTemplateCompileResult: Ref<ReturnType<(typeof import('@volar/vue-code-gen'))['compileSFCTemplate']> | undefined>, sfcStyles: ReturnType<(typeof import('./useSfcStyles'))['useSfcStyles']>['files'], scriptLang: Ref<string>, compilerOptions: VueCompilerOptions, baseCssModuleType: string, getCssVBindRanges: (cssEmbeddeFile: EmbeddedFile) => TextRange[], getCssClasses: (cssEmbeddeFile: EmbeddedFile) => Record<string, TextRange[]>, isVue2: boolean, disableTemplateScript: boolean): {
    templateCodeGens: import("@vue/reactivity").ComputedRef<{
        codeGen: CodeGen<EmbeddedFileMappingData>;
        formatCodeGen: CodeGen<EmbeddedFileMappingData>;
        cssCodeGen: CodeGen<EmbeddedFileMappingData>;
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
