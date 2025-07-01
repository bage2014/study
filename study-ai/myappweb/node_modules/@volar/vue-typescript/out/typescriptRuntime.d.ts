import type { TextRange } from '@volar/vue-code-gen';
import type * as ts from 'typescript/lib/tsserverlibrary';
import { LanguageServiceHost } from './types';
import { EmbeddedFile } from './vueFile';
export interface VueLanguagePlugin {
    compileTemplate?(tmplate: string, lang: string): {
        html: string;
        mapping(htmlStart: number, htmlEnd: number): {
            start: number;
            end: number;
        } | undefined;
    } | undefined;
}
export declare type TypeScriptRuntime = ReturnType<typeof createTypeScriptRuntime>;
export declare function createTypeScriptRuntime(options: {
    typescript: typeof import('typescript/lib/tsserverlibrary');
    vueLsHost: LanguageServiceHost;
    baseCssModuleType: string;
    getCssClasses: (cssEmbeddeFile: EmbeddedFile) => Record<string, TextRange[]>;
    isTsPlugin?: boolean;
    isVueTsc?: boolean;
}): {
    vueLsHost: LanguageServiceHost;
    vueFiles: {
        get: (fileName: string) => import("./vueFile").VueFile;
        delete: (fileName: string) => boolean;
        has: (fileName: string) => boolean;
        set: (fileName: string, vueFile: import("./vueFile").VueFile) => import("./vueFile").VueFile;
        getFileNames: () => string[];
        getDirs: () => string[];
        getAll: () => import("./vueFile").VueFile[];
        getTeleport: (fileName: string) => import(".").Teleport | undefined;
        getEmbeddeds: () => Generator<{
            vueFile: import("./vueFile").VueFile;
            embedded: import("./vueFile").Embedded;
        }, void, unknown>;
        fromEmbeddedLocation: (fileName: string, start: number, end?: number | undefined, filter?: ((data: import("@volar/vue-code-gen").EmbeddedFileMappingData) => boolean) | undefined, sourceMapFilter?: ((sourceMap: import(".").EmbeddedFileSourceMap) => boolean) | undefined) => Generator<{
            fileName: string;
            range: {
                start: number;
                end: number;
            };
            mapped: {
                vueFile: import("./vueFile").VueFile;
                embedded: import("./vueFile").Embedded;
            };
            data: import("@volar/vue-code-gen").EmbeddedFileMappingData;
        } | {
            fileName: string;
            range: {
                start: number;
                end: number;
            };
            mapped?: undefined;
            data?: undefined;
        }, void, unknown>;
        fromEmbeddedFile: (file: EmbeddedFile<unknown>) => import("./vueFile").VueFile | undefined;
        fromEmbeddedFileName: (fileName: string) => {
            vueFile: import("./vueFile").VueFile;
            embedded: import("./vueFile").Embedded;
        } | undefined;
    };
    getTsLs: () => ts.LanguageService;
    getTsLsHost: () => ts.LanguageServiceHost;
    update: () => void;
    dispose: () => void;
    getLocalTypesFiles: () => {
        fileNames: string[];
        code: string;
    };
};
