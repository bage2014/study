import { Ref } from '@vue/reactivity';
import { Embedded, EmbeddedFile, Sfc } from '../vueFile';
export declare function useSfcTemplate(fileName: string, template: Ref<Sfc['template']>): {
    file: import("@vue/reactivity").ComputedRef<EmbeddedFile<unknown> | undefined>;
    embedded: import("@vue/reactivity").ComputedRef<Embedded | undefined>;
};
