package atwatanabe.realcube;

public class RightCommand
{
    private Cube3x3 cube;
    private boolean inverted;

    public RightCommand(Cube3x3 c, boolean invert)
    {
        cube = c;
        inverted = invert;
    }

    public void execute()
    {
        cube.right(inverted);
    }

    public void unexecute()
    {
        cube.right(!inverted);
    }
}
