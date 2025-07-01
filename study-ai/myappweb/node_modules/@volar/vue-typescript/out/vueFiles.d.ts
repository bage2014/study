import type { EmbeddedFileMappingData } from '@volar/vue-code-gen';
import type { EmbeddedFileSourceMap, Teleport } from './utils/sourceMaps';
import type { Embedded, EmbeddedFile, VueFile } from './vueFile';
export interface VueFiles extends ReturnType<typeof createVueFiles> {
}
export declare function createVueFiles(): {
    get: (fileName: string) => VueFile;
    delete: (fileName: string) => boolean;
    has: (fileName: string) => boolean;
    set: (fileName: string, vueFile: VueFile) => VueFile;
    getFileNames: () => string[];
    getDirs: () => string[];
    getAll: () => VueFile[];
    getTeleport: (fileName: string) => Teleport | undefined;
    getEmbeddeds: () => Generator<{
        vueFile: VueFile;
        embedded: Embedded;
    }, void, unknown>;
    fromEmbeddedLocation: (fileName: string, start: number, end?: number | undefined, filter?: ((data: EmbeddedFileMappingData) => boolean) | undefined, sourceMapFilter?: ((sourceMap: EmbeddedFileSourceMap) => boolean) | undefined) => Generator<{
        fileName: string;
        range: {
            start: number;
            end: number;
        };
        mapped: {
            vueFile: VueFile;
            embedded: Embedded;
        };
        data: EmbeddedFileMappingData;
    } | {
        fileName: string;
        range: {
            start: number;
            end: number;
        };
        mapped?: undefined;
        data?: undefined;
    }, void, unknown>;
    fromEmbeddedFile: (file: EmbeddedFile) => VueFile | undefined;
    fromEmbeddedFileName: (fileName: string) => {
        vueFile: VueFile;
        embedded: Embedded;
    } | undefined;
};
