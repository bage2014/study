import { CodeGen } from '@volar/code-gen';
import * as SourceMaps from '@volar/source-map';
import type * as templateGen from '../generators/template';
import type { ScriptRanges } from '../parsers/scriptRanges';
import type { ScriptSetupRanges } from '../parsers/scriptSetupRanges';
import type { TeleportMappingData, EmbeddedFileMappingData } from '../types';
export declare function generate(lsType: 'template' | 'script', fileName: string, script: undefined | {
    src?: string;
    content: string;
}, scriptSetup: undefined | {
    content: string;
}, scriptRanges: ScriptRanges | undefined, scriptSetupRanges: ScriptSetupRanges | undefined, getHtmlGen: () => ReturnType<typeof templateGen['generate']> | undefined, getStyleBindTexts: () => string[], vueLibName: string, shimComponentOptions: boolean, downgradePropsAndEmitsToSetupReturnOnScriptSetup: boolean): {
    codeGen: CodeGen<EmbeddedFileMappingData>;
    teleports: SourceMaps.Mapping<TeleportMappingData>[];
};
export declare function genConstructorOverloads(name?: string, nums?: number): string;
