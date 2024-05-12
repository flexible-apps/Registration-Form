package com.flexible.authentications.db;

import com.flexible.implementions.MFANativeGetterConsumer;

import java.util.List;

public class Clients {

    public static ClientsBuilder of(MFANativeGetterConsumer<List<ClientCreator>> clients) {
        return new ClientsBuilder(clients.get());
    }


    public static void main(String[] args) {

        Clients.of(Accounts::getClients)
                .stream()
                .filter(client -> (!client.getUsername().equals("Abdel Halim") && !client.getEmail().equals("supp.@gmail.com")) )
                .register((availableClients, aBoolean) -> {
                    System.out.println("test: "+aBoolean);
                    if (aBoolean) {
                        availableClients.add(ClientCreator.getInstance("Mbdel Halim", ".@gmail.com", "1234"));
                    }
                });

    }
}
