package com.fungisearch.fudriver.reclassification.command;

import com.fungisearch.fudriver.common.command.Command;

/**
 * Przeklasyfikowanie całej palety wykonywane przez administratora
 */
public class LocalReclassifyCommand implements Command {
    public Long supplierId;
    public Long paletaNr;
    public Long rodzajId;
    public Long cycleId;
    public Integer trolleyManId;
}
