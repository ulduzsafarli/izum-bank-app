package org.matrix.izumbankapp.enumeration.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public enum Permission {

    @JsonProperty("admin:read")
    ADMIN_READ("admin:read"),

    @JsonProperty("admin:update")
    ADMIN_UPDATE("admin:update"),

    @JsonProperty("admin:create")
    ADMIN_CREATE("admin:create"),

    @JsonProperty("admin:delete")
    ADMIN_DELETE("admin:delete"),

    @JsonProperty("management:read")
    MANAGER_READ("management:read"),

    @JsonProperty("management:update")
    MANAGER_UPDATE("management:update"),

    @JsonProperty("management:create")
    MANAGER_CREATE("management:create"),

    @JsonProperty("management:delete")
    MANAGER_DELETE("management:delete");


    private final String value;
}
