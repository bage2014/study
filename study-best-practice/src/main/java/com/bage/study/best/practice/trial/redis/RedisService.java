package com.bage.study.best.practice.trial.redis;

import java.util.List;

public interface RedisService {

    boolean set(String key, Object value);

    boolean delete(String key, Object value);

    boolean listSet(String key, List<Object> value);

    boolean listAdd(String key, Object value);

    boolean listRemove(String key, int inded);

}
