"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.mergeCodeGen = exports.CodeGen = void 0;
class CodeGen {
    constructor() {
        this.text = '';
        this.mappings = [];
    }
    getText() {
        return this.text;
    }
    getMappings(sourceRangeParser) {
        if (!sourceRangeParser) {
            return this.mappings;
        }
        return this.mappings.map(mapping => (Object.assign(Object.assign({}, mapping), { sourceRange: sourceRangeParser(mapping.data, mapping.sourceRange), additional: mapping.additional
                ? mapping.additional.map(extraMapping => (Object.assign(Object.assign({}, extraMapping), { sourceRange: sourceRangeParser(mapping.data, extraMapping.sourceRange) })))
                : undefined })));
    }
    addCode(str, sourceRange, mode, data, extraSourceRanges) {
        const targetRange = this.addText(str);
        this.addMapping2({
            mappedRange: targetRange,
            sourceRange,
            mode,
            data,
            additional: extraSourceRanges ? extraSourceRanges.map(extraSourceRange => ({
                mappedRange: targetRange,
                mode,
                sourceRange: extraSourceRange,
            })) : undefined,
        });
        return targetRange;
    }
    addMapping(str, sourceRange, mode, data) {
        const targetRange = {
            start: this.text.length,
            end: this.text.length + str.length,
        };
        this.addMapping2({ mappedRange: targetRange, sourceRange, mode, data });
        return targetRange;
    }
    addMapping2(mapping) {
        this.mappings.push(mapping);
    }
    addText(str) {
        const range = {
            start: this.text.length,
            end: this.text.length + str.length,
        };
        this.text += str;
        return range;
    }
}
exports.CodeGen = CodeGen;
function mergeCodeGen(a, b) {
    const aLength = a.getText().length;
    for (const mapping of b.getMappings()) {
        a.addMapping2(Object.assign(Object.assign({}, mapping), { mappedRange: {
                start: mapping.mappedRange.start + aLength,
                end: mapping.mappedRange.end + aLength,
            }, additional: mapping.additional ? mapping.additional.map(mapping_2 => (Object.assign(Object.assign({}, mapping_2), { mappedRange: {
                    start: mapping_2.mappedRange.start + aLength,
                    end: mapping_2.mappedRange.end + aLength,
                } }))) : undefined }));
    }
    a.addText(b.getText());
}
exports.mergeCodeGen = mergeCodeGen;
//# sourceMappingURL=index.js.map