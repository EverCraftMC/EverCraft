package io.github.evercraftmc.evercraft.shared.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractConfig implements Config {
    private Map<String, Object> defaults = new HashMap<String, Object>();

    public String getString(String key) {
        return getSerializable(key, String.class);
    }

    public List<String> getStringList(String key) {
        return getSerializableList(key, String.class);
    }

    public Integer getInteger(String key) {
        try {
            return getSerializable(key, Integer.class);
        } catch (ClassCastException e) {
            try {
                return Math.round((float) ((double) getSerializable(key, Double.class)));
            } catch (ClassCastException e2) {
                try {
                    return Math.round(getSerializable(key, Float.class));
                } catch (ClassCastException e3) {
                    try {
                        return Math.round(getSerializable(key, Long.class));
                    } catch (ClassCastException e4) {
                        return null;
                    }
                }
            }
        }
    }

    public List<Integer> getIntegerList(String key) {
        return getSerializableList(key, Integer.class);
    }

    public Float getFloat(String key) {
        try {
            return getSerializable(key, Float.class);
        } catch (ClassCastException e) {
            try {
                return (float) ((double) getSerializable(key, Double.class));
            } catch (ClassCastException e2) {
                try {
                    return (float) ((double) getSerializable(key, Long.class));
                } catch (ClassCastException e3) {
                    try {
                        return (float) ((double) getSerializable(key, Integer.class));
                    } catch (ClassCastException e4) {
                        return null;
                    }
                }
            }
        }
    }

    public List<Float> getFloatList(String key) {
        return getSerializableList(key, Float.class);
    }

    public Double getDouble(String key) {
        try {
            return getSerializable(key, Double.class);
        } catch (ClassCastException e) {
            try {
                return (double) getSerializable(key, Float.class);
            } catch (ClassCastException e2) {
                try {
                    return (double) getSerializable(key, Long.class);
                } catch (ClassCastException e3) {
                    try {
                        return (double) getSerializable(key, Integer.class);
                    } catch (ClassCastException e4) {
                        return null;
                    }
                }
            }
        }
    }

    public List<Double> getDoubleList(String key) {
        return getSerializableList(key, Double.class);
    }

    public Long getLong(String key) {
        try {
            return getSerializable(key, Long.class);
        } catch (ClassCastException e) {
            try {
                return (long) ((int) getSerializable(key, Integer.class));
            } catch (ClassCastException e2) {
                try {
                    return (long) ((double) getSerializable(key, Double.class));
                } catch (ClassCastException e3) {
                    try {
                        return (long) ((double) getSerializable(key, Float.class));
                    } catch (ClassCastException e4) {
                        return null;
                    }
                }
            }
        }
    }

    public List<Long> getLongList(String key) {
        return getSerializableList(key, Long.class);
    }

    public Boolean getBoolean(String key) {
        Boolean value = getSerializable(key, Boolean.class);

        if (value != null) {
            return value;
        } else {
            return false;
        }
    }

    public List<Boolean> getBooleanList(String key) {
        return getSerializableList(key, Boolean.class);
    }

    public Map<String, Object> getDefaults() {
        return this.defaults;
    }

    public void addDefault(String key, Object value) {
        this.defaults.put(key, value);
    }

    public void removeDefault(String key) {
        this.defaults.remove(key);
    }

    public void copyDefaults() {
        for (Map.Entry<String, Object> entry : this.defaults.entrySet()) {
            if (!this.exists(entry.getKey())) {
                this.set(entry.getKey(), entry.getValue());
            }
        }

        this.save();
    }
}