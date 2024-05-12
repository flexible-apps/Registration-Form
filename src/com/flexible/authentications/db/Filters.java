package com.flexible.authentications.db;

import com.flexible.implementions.MFAConsumer;
import com.flexible.implementions.MFANativeConsumer;

import java.util.List;
import java.util.function.Consumer;

public final class Filters implements Register, Present {
    private final List<ClientCreator> clients;
    private final ClientCreator client;
    private boolean isAvailable;

    public Filters(List<ClientCreator> clients, ClientCreator client, boolean isAvailable) {
        this.clients = clients;
        this.client = client;
        this.isAvailable = isAvailable;
    }

    public Filters filter(MFAConsumer<Boolean, ClientCreator> consumer) {
        for (int i = 0; i < clients.size(); i++) {
            ClientCreator clientCreator = clients.get(i);
            boolean accept = consumer.accept(clientCreator);
//            System.out.println("a: " +accept);
            if (accept) {
//                System.out.println("sss");
                return new Filters(clients, clientCreator, true);
            } else if (i == clients.size() - 1) {
//                System.out.println("bbb");
                return new Filters(clients, clientCreator, false);
            }
        }
        return this;
    }

    @Override
    public boolean register() {
        System.out.println("av:" +isAvailable);
        if (!isAvailable) {
            clients.add(client);
            return true;
        }
        return false;
    }



    @Override
    public  void ifPresentOrElse(Consumer<ClientCreator> ifPresent, MFANativeConsumer orElse) {
        if (isAvailable) {
            ifPresent.accept(client);
        } else {
            orElse.accept();
        }
    }

    public void register(Block<List<ClientCreator>, Boolean> block) {
        block.accept(clients, isAvailable);
    }
}
