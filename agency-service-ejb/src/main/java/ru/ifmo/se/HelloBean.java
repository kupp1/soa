package ru.ifmo.se;

import ru.ifmo.se.common.remote.IHello;

import javax.ejb.Stateless;

@Stateless
public class HelloBean implements IHello {
    public String sayHello() {
        return "Hello World!!!!";
    }
}
