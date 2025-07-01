"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.Teleport = exports.EmbeddedFileSourceMap = void 0;
const SourceMaps = require("@volar/source-map");
class EmbeddedFileSourceMap extends SourceMaps.SourceMapBase {
}
exports.EmbeddedFileSourceMap = EmbeddedFileSourceMap;
class Teleport extends SourceMaps.SourceMapBase {
    *findTeleports(start, end, filter) {
        for (const [teleRange, data] of this.getMappedRanges(start, end, filter ? data => filter(data.toTarget) : undefined)) {
            yield [teleRange, data.toTarget];
        }
        for (const [teleRange, data] of this.getSourceRanges(start, end, filter ? data => filter(data.toTarget) : undefined)) {
            yield [teleRange, data.toTarget];
        }
    }
}
exports.Teleport = Teleport;
//# sourceMappingURL=sourceMaps.js.map