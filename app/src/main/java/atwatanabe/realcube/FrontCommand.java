package atwatanabe.realcube;

public class FrontCommand implements Command
{
    private Cube3x3 cube;
    private boolean inverted;

    public FrontCommand(Cube3x3 c, boolean invert)
    {
        cube = c;
        inverted = invert;
    }

    public void execute()
    {
        cube.front(inverted);
    }

    public void unexecute()
    {
        cube.front(!inverted);
    }

    @Override
    public String toString()
    {
        return "Front " + inverted;
    }
}
