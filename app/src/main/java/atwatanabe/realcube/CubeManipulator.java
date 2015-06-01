package atwatanabe.realcube;

import java.util.Stack;

public class CubeManipulator
{
    private Stack<Command> undoHistory;
    private Stack<Command> redoHistory;

    public CubeManipulator()
    {
        undoHistory = new Stack<Command>();
        redoHistory = new Stack<Command>();
    }

    public void manipulateCube(Command c)
    {
        undoHistory.push(c);
        c.execute();
    }

    public void undo()
    {
        if (!undoHistory.isEmpty())
        {
            redoHistory.push(undoHistory.peek());
            undoHistory.pop().unexecute();
        }
    }

    public void redo()
    {
        if (!redoHistory.isEmpty())
        {
            undoHistory.push(redoHistory.peek());
            redoHistory.pop().execute();
        }
    }
}
