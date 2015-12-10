package ch.fhnw.oop2.control;

/**
 * @import jcommander: Copyright 2010 Cedric Beust <cedric@beust.com>
 * @license apache gnu license 2
 */
import com.beust.jcommander.DynamicParameter;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.JCommander;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: make a generic commandline tool which is indipendent from the os

/**
 * Created by Lukas on 30.11.2015.
 */
public class MInventoryCmd {

    /*
    @Parameter
    public List<String> parameters = Lists.newArrayList();

    @Parameter(names = { "-log", "-verbose" }, description = "Level of verbosity")
    public Integer verbose = 1;

    @Parameter(names = "-groups", description = "Comma-separated list of group names to be run")
    public String groups;

    @Parameter(names = "-debug", description = "Debug mode")
    public boolean debug = false;

    @DynamicParameter(names = "-D", description = "Dynamic parameters go here")
    public Map<String, String> dynamicParams = new HashMap<String, String>();

    */

    public MInventoryCmd() {
        JCommander jCommander = new JCommander();
    }

}
