import * as SourceMaps from '@volar/source-map';
import { EmbeddedFileMappingData, TeleportMappingData, TeleportSideData } from '@volar/vue-code-gen/out/types';
export declare class EmbeddedFileSourceMap extends SourceMaps.SourceMapBase<EmbeddedFileMappingData> {
}
export declare class Teleport extends SourceMaps.SourceMapBase<TeleportMappingData> {
    findTeleports(start: number, end?: number, filter?: (data: TeleportSideData) => boolean): Generator<readonly [{
        start: number;
        end: number;
    }, TeleportSideData], void, unknown>;
}
