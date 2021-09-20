package com.game.exception_handling;

/*
* Класс отвечает за вывод JSON с сообщением ошибки, если что-то введено неправильно
* */
public class PlayerIncorrectData {
    //поле, которое содержит информацию что именно не так было в запросе
    private String info;

    public PlayerIncorrectData() {
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
