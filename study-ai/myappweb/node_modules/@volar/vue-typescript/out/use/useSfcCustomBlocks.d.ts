import { Ref } from '@vue/reactivity';
import { Embedded, EmbeddedFile, Sfc } from '../vueFile';
export declare function useSfcCustomBlocks(fileName: string, customBlocks: Ref<Sfc['customBlocks']>): {
    files: import("@vue/reactivity").ComputedRef<EmbeddedFile<unknown>[]>;
    embeddeds: import("@vue/reactivity").ComputedRef<Embedded[]>;
};
