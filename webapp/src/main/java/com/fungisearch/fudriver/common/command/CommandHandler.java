package com.fungisearch.fudriver.common.command;

/**
 * Created by marcin on 04.02.16.
 */
public interface CommandHandler<C extends Command> {
    CommandResult handle(C command);
}
