import { Ref, ComputedRef } from '@vue/reactivity';
import { Teleport } from '../utils/sourceMaps';
import type * as templateGen from '@volar/vue-code-gen/out/generators/template';
import type { parseScriptRanges } from '@volar/vue-code-gen/out/parsers/scriptRanges';
import type { parseScriptSetupRanges } from '@volar/vue-code-gen/out/parsers/scriptSetupRanges';
import type { TextRange } from '@volar/vue-code-gen';
import { Embedded, EmbeddedFile, Sfc } from '../vueFile';
import { VueCompilerOptions } from '../types';
export declare function useSfcScriptGen<T extends 'template' | 'script'>(lsType: T, fileName: string, vueFileContent: Ref<string>, lang: Ref<string>, script: Ref<Sfc['script']>, scriptSetup: Ref<Sfc['scriptSetup']>, scriptRanges: Ref<ReturnType<typeof parseScriptRanges> | undefined>, scriptSetupRanges: Ref<ReturnType<typeof parseScriptSetupRanges> | undefined>, htmlGen: Ref<ReturnType<typeof templateGen.generate> | undefined>, sfcStyles: ReturnType<(typeof import('./useSfcStyles'))['useSfcStyles']>['files'], compilerOptions: VueCompilerOptions, getCssVBindRanges: (cssEmbeddeFile: EmbeddedFile) => TextRange[]): {
    lang: Ref<string>;
    file: T extends "script" ? ComputedRef<EmbeddedFile<undefined>> : ComputedRef<EmbeddedFile<undefined> | undefined>;
    embedded: ComputedRef<Embedded | undefined>;
    teleport: ComputedRef<Teleport>;
};
