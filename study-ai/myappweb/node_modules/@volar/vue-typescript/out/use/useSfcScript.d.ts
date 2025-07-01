import { Ref } from '@vue/reactivity';
import { Embedded, EmbeddedFile, Sfc } from '../vueFile';
export declare function useSfcScript(fileName: string, script: Ref<Sfc['script'] | Sfc['scriptSetup']>): {
    file: import("@vue/reactivity").ComputedRef<EmbeddedFile<unknown> | undefined>;
    embedded: import("@vue/reactivity").ComputedRef<Embedded | undefined>;
};
