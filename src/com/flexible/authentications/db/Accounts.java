package com.flexible.authentications.db;

import java.util.ArrayList;
import java.util.List;

public class Accounts {
    public final static List<ClientCreator> CLIENTS = new ArrayList<>(
            List.of(ClientCreator.getInstance("Halim", "supp.flexibleapps@gmail.com", "1234"))
    );

    public static List<ClientCreator> getClients() {
        return CLIENTS;
    }
}




