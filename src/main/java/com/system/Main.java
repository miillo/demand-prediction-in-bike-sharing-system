package com.system;

import akka.actor.typed.ActorSystem;

public class Main {
    public static void main(String[] args) {
        //todo add reading application.info + global settings class

        ActorSystem.create(SystemSupervisor.create(), "bike-sharing-system");
    }
}
