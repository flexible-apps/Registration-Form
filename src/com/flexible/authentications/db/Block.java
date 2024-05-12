package com.flexible.authentications.db;

@FunctionalInterface
public interface Block<V1, V2>{
    void accept(V1 v1, V2 v2);
}
