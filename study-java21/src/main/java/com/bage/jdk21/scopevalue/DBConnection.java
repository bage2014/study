package com.bage.jdk21.scopevalue;

import com.bage.jdk21.scopevalue.model.PersistedObject;

public interface DBConnection {
    PersistedObject readKey(String key);
}
