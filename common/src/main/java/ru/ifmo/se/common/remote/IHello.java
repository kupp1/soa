package ru.ifmo.se.common.remote;

import javax.ejb.Remote;

@Remote
public interface IHello {
    String sayHello();
}
