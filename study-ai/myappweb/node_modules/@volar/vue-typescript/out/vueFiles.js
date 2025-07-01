"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.createVueFiles = void 0;
const reactivity_1 = require("@vue/reactivity");
const path = require("path");
const localTypes = require("./utils/localTypes");
const untrack_1 = require("./utils/untrack");
function createVueFiles() {
    const vueFiles = (0, reactivity_1.shallowReactive)({});
    const all = (0, reactivity_1.computed)(() => Object.values(vueFiles));
    const fileNames = (0, reactivity_1.computed)(() => all.value.map(sourceFile => sourceFile.fileName));
    const embeddedDocumentsMap = (0, reactivity_1.computed)(() => {
        const map = new WeakMap();
        for (const sourceFile of all.value) {
            for (const embedded of sourceFile.refs.allEmbeddeds.value) {
                map.set(embedded.file, sourceFile);
            }
        }
        return map;
    });
    const sourceMapsByFileName = (0, reactivity_1.computed)(() => {
        const map = new Map();
        for (const sourceFile of all.value) {
            for (const embedded of sourceFile.refs.allEmbeddeds.value) {
                map.set(embedded.file.fileName.toLowerCase(), { vueFile: sourceFile, embedded });
            }
        }
        return map;
    });
    const teleports = (0, reactivity_1.computed)(() => {
        const map = new Map();
        for (const key in vueFiles) {
            const sourceFile = vueFiles[key];
            for (const { file, teleport } of sourceFile.refs.teleports.value) {
                map.set(file.fileName.toLowerCase(), teleport);
            }
        }
        return map;
    });
    const dirs = (0, reactivity_1.computed)(() => [...new Set(fileNames.value.map(path.dirname))]);
    return {
        get: (0, untrack_1.untrack)((fileName) => vueFiles[fileName.toLowerCase()]),
        delete: (0, untrack_1.untrack)((fileName) => delete vueFiles[fileName.toLowerCase()]),
        has: (0, untrack_1.untrack)((fileName) => !!vueFiles[fileName.toLowerCase()]),
        set: (0, untrack_1.untrack)((fileName, vueFile) => vueFiles[fileName.toLowerCase()] = vueFile),
        getFileNames: (0, untrack_1.untrack)(() => fileNames.value),
        getDirs: (0, untrack_1.untrack)(() => dirs.value),
        getAll: (0, untrack_1.untrack)(() => all.value),
        getTeleport: (0, untrack_1.untrack)((fileName) => teleports.value.get(fileName.toLowerCase())),
        getEmbeddeds: (0, untrack_1.untrack)(function* () {
            for (const sourceMap of sourceMapsByFileName.value) {
                yield sourceMap[1];
            }
        }),
        fromEmbeddedLocation: (0, untrack_1.untrack)(function* (fileName, start, end, filter, sourceMapFilter) {
            if (fileName.endsWith(`/${localTypes.typesFileName}`))
                return;
            if (end === undefined)
                end = start;
            const mapped = sourceMapsByFileName.value.get(fileName.toLowerCase());
            if (mapped) {
                if (sourceMapFilter && !sourceMapFilter(mapped.embedded.sourceMap))
                    return;
                for (const vueRange of mapped.embedded.sourceMap.getSourceRanges(start, end, filter)) {
                    yield {
                        fileName: mapped.vueFile.fileName,
                        range: vueRange[0],
                        mapped: mapped,
                        data: vueRange[1],
                    };
                }
            }
            else {
                yield {
                    fileName,
                    range: {
                        start,
                        end,
                    },
                };
            }
        }),
        fromEmbeddedFile: (0, untrack_1.untrack)(function (file) {
            return embeddedDocumentsMap.value.get(file);
        }),
        fromEmbeddedFileName: (0, untrack_1.untrack)(function (fileName) {
            return sourceMapsByFileName.value.get(fileName.toLowerCase());
        }),
    };
}
exports.createVueFiles = createVueFiles;
//# sourceMappingURL=vueFiles.js.map