package com.flexible.authentications.db;

import java.util.ArrayList;
import java.util.List;

public class ClientsBuilder {
    public List<ClientCreator> clients;

    private ClientsBuilder(ClientCreator client) {
        this(new ArrayList<>(), client);
    }

    public ClientsBuilder() {
        this(new ArrayList<>(), null);
    }

    public ClientsBuilder(List<ClientCreator> clients) {
        this.clients = clients;
    }

    private ClientsBuilder(List<ClientCreator> clients, ClientCreator client) {
        this.clients = clients;
    }

    public Filters stream() {
        return new Filters(clients, null, false);
    }
}
