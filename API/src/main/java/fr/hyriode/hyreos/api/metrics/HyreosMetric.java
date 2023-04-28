package fr.hyriode.hyreos.api.metrics;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by AstFaster
 * on 14/10/2022 at 20:00
 */
public class HyreosMetric {

    /** The name of the metric */
    private final String name;
    /** The tags linked to the metric */
    private final Map<String, String> tags = new TreeMap<>();
    /** The fields linked to the metric */
    private final Map<String, Object> fields = new TreeMap<>();

    /**
     * Main constructor of a {@link HyreosMetric}
     *
     * @param name The name of the metric
     */
    public HyreosMetric(String name) {
        this.name = name;
    }

    /**
     * Get the name of the metric
     *
     * @return A name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Add a tag to the metric
     *
     * @param key The key of the tag
     * @param value The value of the tag
     * @return The {@link HyreosMetric} instance
     */
    public HyreosMetric addTag(String key, String value) {
        this.tags.put(key, value);
        return this;
    }

    /**
     * Add a field to the metric
     *
     * @param key The key of the field
     * @param number The value of the field
     * @return The {@link HyreosMetric} instance
     */
    public HyreosMetric addField(String key, Number number) {
        this.fields.put(key, number);
        return this;
    }

    /**
     * Add a field to the metric
     *
     * @param key The key of the field
     * @param value The value of the field
     * @return The {@link HyreosMetric} instance
     */
    public HyreosMetric addField(String key, boolean value) {
        this.fields.put(key, value);
        return this;
    }

    /**
     * Add a field to the metric
     *
     * @param key The key of the field
     * @param value The value of the field
     * @return The {@link HyreosMetric} instance
     */
    public HyreosMetric addField(String key, String value) {
        this.fields.put(key, value);
        return this;
    }

    /**
     * Get all tags of the metric
     *
     * @return A map of tag
     */
    public Map<String, String> getTags() {
        return this.tags;
    }

    /**
     * Get all fields of the metric
     *
     * @return A map of field
     */
    public Map<String, Object> getFields() {
        return this.fields;
    }

}
