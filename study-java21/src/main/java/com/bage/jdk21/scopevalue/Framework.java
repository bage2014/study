package com.bage.jdk21.scopevalue;

import com.bage.jdk21.scopevalue.model.FrameworkContext;
import com.bage.jdk21.scopevalue.model.PersistedObject;
import com.bage.jdk21.scopevalue.model.Request;
import com.bage.jdk21.scopevalue.model.Response;

class Framework {
    private final static ScopedValue<FrameworkContext> CONTEXT
                        = ScopedValue.newInstance();   // (1)

    void serve(Request request, Response response) {
        var context = createContext(request);
        ScopedValue.where(CONTEXT, context)            // (2)
                   .run(() -> Application.handle(request, response));
    }

    private FrameworkContext createContext(Request request) {
        return null;
    }

    public PersistedObject readKey(String key) {
        var context = CONTEXT.get();            // (3)
        var db = getDBConnection(context);
        return db.readKey(key);
    }

    private DBConnection getDBConnection(FrameworkContext context) {
        return null;
    }

}
