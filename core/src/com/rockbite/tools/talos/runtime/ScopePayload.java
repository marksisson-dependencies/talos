package com.rockbite.tools.talos.runtime;

import com.badlogic.gdx.utils.IntMap;
import com.rockbite.tools.talos.runtime.values.FloatValue;
import com.rockbite.tools.talos.runtime.values.Value;

public class ScopePayload {

    public static final int EMITTER_ALPHA = 0;
    public static final int PARTICLE_ALPHA = 1;
    public static final int PARTICLE_SEED = 2;

    private IntMap<Value> map = new IntMap<>();

    public ScopePayload() {
        for(int i = 0; i < 10; i++) {
            map.put(i, new FloatValue());
        }
    }

    public void set(int index, float value) {
        map.get(index).set(value);
    }

    public void set(int index, Value value) {
        map.get(index).set(value);
    }

    public void get(Value value, int index) {
        value.set(map.get(index));
    }

    public float getFloat(int index) {
        return (float) map.get(index).get();
    }
}