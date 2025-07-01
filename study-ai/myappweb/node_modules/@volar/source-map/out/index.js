"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.SourceMapBase = exports.Mode = void 0;
var Mode;
(function (Mode) {
    /**
     * @case1
     * 123456 -> abcdef
     * ^    ^    ^    ^
     * @case2
     * 123456 -> abcdef
     *  ^  ^      ^  ^
     * @case3
     * 123456 -> abcdef
     *   ^^        ^^
     */
    Mode[Mode["Offset"] = 0] = "Offset";
    /**
     * @case1
     * 123456 -> abcdef
     * ^    ^    ^    ^
     * @case2
     * 123456 -> abcdef
     *  ^  ^     NOT_MATCH
     * @case3
     * 123456 -> abcdef
     *   ^^      NOT_MATCH
     */
    Mode[Mode["Totally"] = 1] = "Totally";
    /**
     * @case1
     * 123456 -> abcdef
     * ^    ^    ^    ^
     * @case2
     * 123456 -> abcdef
     *  ^  ^     ^    ^
     * @case3
     * 123456 -> abcdef
     *   ^^      ^    ^
     */
    Mode[Mode["Expand"] = 2] = "Expand";
})(Mode = exports.Mode || (exports.Mode = {}));
class SourceMapBase {
    constructor(_mappings) {
        this.mappings = _mappings !== null && _mappings !== void 0 ? _mappings : [];
    }
    getSourceRange(start, end, filter) {
        for (const mapped of this.getRanges(start, end !== null && end !== void 0 ? end : start, false, filter)) {
            return mapped;
        }
    }
    getMappedRange(start, end, filter) {
        for (const mapped of this.getRanges(start, end !== null && end !== void 0 ? end : start, true, filter)) {
            return mapped;
        }
    }
    getSourceRanges(start, end, filter) {
        return this.getRanges(start, end !== null && end !== void 0 ? end : start, false, filter);
    }
    getMappedRanges(start, end, filter) {
        return this.getRanges(start, end !== null && end !== void 0 ? end : start, true, filter);
    }
    *getRanges(startOffset, endOffset, sourceToTarget, filter) {
        for (const mapping of this.mappings) {
            if (filter && !filter(mapping.data))
                continue;
            const mapped = this.getRange(startOffset, endOffset, sourceToTarget, mapping.mode, mapping.sourceRange, mapping.mappedRange, mapping.data);
            if (mapped) {
                yield getMapped(mapped);
            }
            else if (mapping.additional) {
                for (const other of mapping.additional) {
                    const mapped = this.getRange(startOffset, endOffset, sourceToTarget, other.mode, other.sourceRange, other.mappedRange, mapping.data);
                    if (mapped) {
                        yield getMapped(mapped);
                        break; // only return first match additional range
                    }
                }
            }
        }
        function getMapped(mapped) {
            return mapped;
        }
    }
    getRange(start, end, sourceToTarget, mode, sourceRange, targetRange, data) {
        const mappedToRange = sourceToTarget ? targetRange : sourceRange;
        const mappedFromRange = sourceToTarget ? sourceRange : targetRange;
        if (mode === Mode.Totally) {
            if (start === mappedFromRange.start && end === mappedFromRange.end) {
                const _start = mappedToRange.start;
                const _end = mappedToRange.end;
                return [{
                        start: Math.min(_start, _end),
                        end: Math.max(_start, _end),
                    }, data];
            }
        }
        else if (mode === Mode.Offset) {
            if (start >= mappedFromRange.start && end <= mappedFromRange.end) {
                const _start = mappedToRange.start + start - mappedFromRange.start;
                const _end = mappedToRange.end + end - mappedFromRange.end;
                return [{
                        start: Math.min(_start, _end),
                        end: Math.max(_start, _end),
                    }, data];
            }
        }
        else if (mode === Mode.Expand) {
            if (start >= mappedFromRange.start && end <= mappedFromRange.end) {
                const _start = mappedToRange.start;
                const _end = mappedToRange.end;
                return [{
                        start: Math.min(_start, _end),
                        end: Math.max(_start, _end),
                    }, data];
            }
        }
    }
}
exports.SourceMapBase = SourceMapBase;
//# sourceMappingURL=index.js.map