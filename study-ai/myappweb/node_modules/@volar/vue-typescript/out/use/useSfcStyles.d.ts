import { Ref } from '@vue/reactivity';
import { Embedded, EmbeddedFile, Sfc } from '../vueFile';
export declare function useSfcStyles(fileName: string, styles: Ref<Sfc['styles']>): {
    files: import("@vue/reactivity").ComputedRef<EmbeddedFile<{
        module: string | undefined;
        scoped: boolean;
    }>[]>;
    embeddeds: import("@vue/reactivity").ComputedRef<Embedded[]>;
};
