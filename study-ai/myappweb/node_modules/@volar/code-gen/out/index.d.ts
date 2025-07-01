import type { Mapping, Mode, Range } from '@volar/source-map';
export declare class CodeGen<T = undefined> {
    private text;
    private mappings;
    getText(): string;
    getMappings(sourceRangeParser?: (data: T, range: Range) => Range): Mapping<T>[];
    addCode(str: string, sourceRange: Range, mode: Mode, data: T, extraSourceRanges?: Range[]): {
        start: number;
        end: number;
    };
    addMapping(str: string, sourceRange: Range, mode: Mode, data: T): {
        start: number;
        end: number;
    };
    addMapping2(mapping: Mapping<T>): void;
    addText(str: string): {
        start: number;
        end: number;
    };
}
export declare function mergeCodeGen<T extends CodeGen<any>>(a: T, b: T): void;
