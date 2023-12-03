package com.maingirder.oauth.process.config;

import java.util.List;
import java.util.Map;

/**
 * @author jason-guo-cr
 * @version 1.0.0
 * @since 2023/11/27 20:31
 */
public class DynamicEnumConfig {

    private List<String> fields;

    private List<Map<String, Map<String, String>>> enums;


    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public List<Map<String, Map<String, String>>> getEnums() {
        return enums;
    }

    public void setEnums(List<Map<String, Map<String, String>>> enums) {
        this.enums = enums;
    }


    public boolean isEnumsBlank() {
        return enums == null || enums.size() == 0;
    }
}
