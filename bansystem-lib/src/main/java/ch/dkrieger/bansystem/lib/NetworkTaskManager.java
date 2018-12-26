package ch.dkrieger.bansystem.lib;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 07.07.18 22:25
 *
 */

import java.util.concurrent.TimeUnit;

public interface NetworkTaskManager {

    public void runTaskAsync(Runnable runnable);

    public void runTaskLater(Runnable runnable, Long delay, TimeUnit unit);

    public void scheduleTask(Runnable runnable, Long repeat, TimeUnit unit);

}
