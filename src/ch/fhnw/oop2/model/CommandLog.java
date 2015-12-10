package ch.fhnw.oop2.model;

import java.util.*;

/**
 * Created by Lukas on 30.11.2015.
 */
public class CommandLog {

    /**
     * stores actions in commandline syntax (See MInventoryCmd */
    private LinkedList<String> actionLog;
    /**
     * Internal Pointer to track the history
     */
    private int internalPointer;


    // --- CONSTRUCTORS ---
    public CommandLog(LinkedList<String> actionList, int internalPointer) {
        this.actionLog = new LinkedList<>();
        this.internalPointer = internalPointer;
    }


    // --- API ---
    public void addLast(String command) {
        this.internalPointer = this.actionLog.indexOf(this.actionLog.getLast());
        this.actionLog.add(command);
        ++internalPointer;
    }
    public void addAfterLastRequested(String command) {
        this.actionLog = (LinkedList<String>)this.actionLog.subList(0, internalPointer);
        this.actionLog.add(++internalPointer, command);
    }
    public String getLast() {
        return this.actionLog.getLast();
    }
    public String popLast() {
        return this.actionLog.pop();
    }
    public String getBeforeLastRequested(){
        return this.actionLog.get(--internalPointer);
    }
    public String popBeforeLastRequest() {
        String before = this.actionLog.get((internalPointer - 1));
        this.actionLog = (LinkedList<String>)this.actionLog.subList(0, internalPointer);
        --internalPointer;
        return before;
    }

}
