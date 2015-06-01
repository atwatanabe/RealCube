package atwatanabe.realcube;

public class MiddleCommand implements Command
{
    private Cube3x3 cube;
    private boolean inverted;

    public MiddleCommand(Cube3x3 c, boolean invert)
    {
        cube = c;
        inverted = invert;
    }

    public void execute()
    {
        cube.middle(inverted);
    }

    public void unexecute()
    {
        cube.middle(!inverted);
    }
}
