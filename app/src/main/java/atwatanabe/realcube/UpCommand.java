package atwatanabe.realcube;

public class UpCommand implements Command
{
    private Cube3x3 cube;
    private boolean inverted;

    public UpCommand(Cube3x3 c, boolean invert)
    {
        cube = c;
        inverted = invert;
    }

    public void execute()
    {
        cube.up(inverted);
    }

    public void unexecute()
    {
        cube.up(!inverted);
    }
}
